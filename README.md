# 数据权限，一个注解搞定！

# 注意点

## 占位符

```sql
sql.append(String.format(" OR %s.dept_id in(select rd.dept_id from sys_role_dept rd where rd.role_id=%d)", dataScope.deptAlias(), role.getRoleId()));
```



## mysql中的函数

## [mysql中find_in_set()函数的使用](https://www.cnblogs.com/xiaoxi/p/5889486.html)

https://www.cnblogs.com/xiaoxi/p/5889486.html



# 搭建项目

会引入Spring Security、Aop、mybatis-plus、mybatis等依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!--aop的依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.1</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```



## 犯下的错误

没有导入mybatis依赖，会抛出异常

`Invalid bound statement (not found):`



## 代码生成器

```java
@SpringBootTest
class DataScopeApplicationTests {

    @Test
    void contextLoads() {
        FastAutoGenerator.create("jdbc:mysql:///tianchin?serverTimezone=Asia/Shanghai", "root", "th123456")
                .globalConfig(builder -> {
                    builder.author("th") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/xiaokaixin/Desktop/tianchi/demo/data_scope/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.th.ds") // 设置父包名
                            // .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/xiaokaixin/Desktop/tianchi/demo/data_scope/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_dept","sys_role","sys_user") // 设置需要生成的表名
                            .addTablePrefix("sys_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }

}

```



## 引入SpringSecurity的细节

#### user类

重写他的方法

![image-20220531090230607](img/image-20220531090230607.png)

<img src="img/image-20220531090259005.png" alt="image-20220531090259005" style="zoom:50%;" />

![image-20220531090317844](img/image-20220531090317844.png)



### UserServiceImpl

![image-20220531090506537](img/image-20220531090506537.png)



# 1. 思路分析

首先我们先来捋一捋这里的权限实现的思路。

`@DataScope` 注解处理的内容叫做数据权限，就是说你这个用户登录后能够访问哪些数据。传统的做法就是根据当前认证用户的 id 或者角色或者权限等信息去查询，但是这种做法比较麻烦比较费事，每次查询都要写大量 SQL，而这些 SQL 中又有大量雷同的地方，所以我们希望能够将之进行统一处理，进而就引出了 `@DataScope` 注解。

在 RuoYi-Vue 脚手架中，将用户的数据权限分为了五类，分别如下：

- 1：这个表示全部数据权限，也就是这个用户登录上来之后可以访问所有的数据，一般来说只有超级管理员具备此权限。
- 2：这个表示自定义数据权限，自定义数据权限就表示根据用户的角色查找到这个用户可以操作哪个部门的数据，以此为依据进行数据查询。
- 3：这个表示部门数据权限，这个简单，就是这个用户只能查询到本部门的数据。
- 4：这个表示本部门及以下数据权限，这个意思就是用户可以查询到本部门以及本部门下面子部门的数据。
- 5：这个就表示这个用户仅仅只能查看自己的数据。

在 TienChin 这个项目中，数据权限也基本上是按照这个脚手架的设计来的，我们只需要搞懂这里的实现思路，将来就可以随心所欲的去自定义数据权限注解了。



# 2. 表结构分析

捋清楚了思路，再来看看表结构。

这里涉及到如下几张表：

- sys_user：用户表
- sys_role：角色表
- sys_dept：部门表
- sys_user_role：用户角色关联表
- sys_role_dept：角色部门关联表

这几个表中有一些细节我来和大家梳理下。一个一个来看。

用户表中有一个 `dept_id` 表示这个用户所属的部门 id，一个用户属于一个部门。

角色表中有一个字段叫做 `data_scope`，表示这个角色所对应的数据权限，取值就是 1-5，含义就是我们上面所列出来的含义，这个很重要哦。

部门表在设计的时候，有一个 ancestors 字段，通过这个字段可以非常方便的查询一个部门的子部门。

最后两张关联表就没啥好说了。

好了，这些都分析完了，我们就来看看具体的实现。



# 3. 具体实现

## 3.1 @DataScope

先来看数据权限注解的定义：

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表的别名
     */
    public String deptAlias() default "";

    /**
     * 用户表的别名
     */
    public String userAlias() default "";
}
```



这个注解中有两个属性，一个是 deptAlias 和 userAlias。由于数据权限实现的核心思路就是在要执行的 SQL 上动态追加查询条件，那么动态追加的 SQL 必须要考虑到原本 SQL 定义时的部门表别名和用户表别名。这两个属性就是用来干这事的。

所以小伙伴们可能也看出来，这个 `@DataScope` 跟我们以前的注解还不太一样，以前自定义的其他注解跟业务耦合度比较小，这个 `@DataScope` 跟业务的耦合度则比较高，必须要看一下你业务 SQL 中对于部门表和用户表取的别名是啥，然后配置到这个注解上。

因此，`@DataScope` 注解不算是一个特别灵活的注解，咱们就抱着学习的态度了解下他的实现方式就行了。



## 3.2 切面分析

注解定义好了，接下来就是切面分析了。

```java
@Aspect
@Component
public class DataScopeAspect {

    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";


    public static final String DATA_SCOPE = "data_scope";

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint jp, DataScope dataScope) {
        clearDataScope(jp);
        //1、获取当前登陆用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserId() == 1L) {
            //说明是超级管理员，不用进行权限过滤
            return;
        }
        StringBuilder sql = new StringBuilder();
        List<Role> roles = user.getRoles();
        //select * from sys_dept d where d.del_flag='0' and (xxx OR xxx OR xxx)
        //d.dept_id in(select rd.dept_id from sys_user_role ur,sys_role_dept rd where ur.user_id=2 and ur.role_id=rd.role_id) 代表一个 xxx
        for (Role role : roles) {
            //获取角色对应的数据权限
            String ds = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(ds)) {
                //如果用户能够查看所有数据权限，这里什么都不用做
                return;
            } else if (DATA_SCOPE_CUSTOM.equals(ds)) {
                //自定义的数据权限，那么就根据 用户角色去查找到部门 id
                //todo 占位符
                sql.append(String.format(" OR %s.dept_id in(select rd.dept_id from sys_role_dept rd where rd.role_id=%d)", dataScope.deptAlias(), role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(ds)) {
                sql.append(String.format(" OR %s.dept_id=%d", dataScope.deptAlias(), user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(ds)) {
                //todo find_in_set
                sql.append(String.format(" OR %s.dept_id in(select dept_id from sys_dept where dept_id=%d or find_in_set(%d,`ancestors`))", dataScope.deptAlias(), user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(ds)) {
                String s = dataScope.userAlias();
                if ("".equals(s)) {
                    //数据权限仅限于本人
                    sql.append(" OR 1=0");
                } else {
                    sql.append(String.format(" OR %s.user_id=%d", dataScope.userAlias(), user.getUserId()));
                }
            }
        }

        // and( xxx or xxx or xxx)
        Object arg = jp.getArgs()[0];
        if (arg != null && arg instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE, " AND ("+sql.substring(4)+")");
        }

    }

    /**
     * 如果 params 中已经有参数了，则删除掉，防止 sql 注入
     *
     * @param jp
     */
    private void clearDataScope(JoinPoint jp) {
        Object arg = jp.getArgs()[0];
        if (arg != null && arg instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }

}
```



1. 首先一上来就定义了五种不同的数据权限类型，这五种类型咱们前面已经介绍过了，这里就不再赘述了。
2. 接下来的 doBefore 方法是一个前置通知。由于 `@DataScope` 注解是加在 service 层的方法上，所以这里使用前置通知，为方法的执行补充 SQL 参数，具体思路是这样：加了数据权限注解的 service 层方法的参数必须是对象，并且这个对象必须继承自 BaseEntity，BaseEntity 中则有一个 Map 类型的 params 属性，我们如果需要为 service 层方法的执行补充一句 SQL，那么就把补充的内容放到这个 params 变量中，补充内容的 key 就是前面声明的 `dataScope`，value 则是一句 SQL。在 doBefore 方法中先执行 clearDataScope 去清除 params 变量中已有的内容，防止 SQL 注入（因为这个 params 的内容也可以从前端传来）；然后执行 handleDataScope 方法进行数据权限的过滤。
3. 在 handleDataScope 方法中，主要是查询到当前的用户，然后调用 dataScopeFilter 方法进行数据过滤，这个就是过滤的核心方法了。
4. 由于一个用户可能有多个角色，所以在 dataScopeFilter 方法中要先遍历角色，不同的角色有不同的数据权限，这些不同的数据权限之间通过 OR 相连，最终生成的补充 SQL 的格式类似这样 `AND(xxx OR xxx OR xxx)` 这样，每一个 xxx 代表一个角色生成的过滤条件。
5. 接下来就是根据各种不同的数据权限生成补充 SQL 了：如果数据权限为 1，则生成的 SQL 为空，即查询 SQL 不添加限制条件；如果数据权限为 2，表示自定义数据权限，此时根据用户的角色查询出用户的部门，生成查询限制的 SQL；如果数据权限为 3，表示用户的数据权限仅限于自己所在的部门，那么将用户所属的部门拎出来作为查询限制；如果数据权限为 4，表示用户的权限是自己的部门和他的子部门，那么就将用户所属的部门以及其子部门拎出来作为限制查询条件；如果数据权限为 5，表示用户的数据权限仅限于自己，即只能查看自己的数据，那么就用用户自身的 id 作为查询的限制条件。最后，再把生成的 SQL 稍微处理下，变成 `AND(xxx OR xxx OR xxx)` 格式，这个就比较简单了，就是字符串截取+字符串拼接。



# 4. 案例分析

我们来看一下 `@DataScope` 注解三个具体的应用大家就明白了。

在 RuoYi-Vue 脚手架中，这个注解主要有三个使用场景：

1. 查询部门。
2. 查询角色。
3. 查询用户。

假设我现在以 ry 这个用户登录，这个用户的角色是普通角色，普通角色的数据权限是 2，即自定义数据权限，我们就来看看这个用户是如何查询数据的。

我们分别来看。

## 4.1 查询部门

首先查询部门的方法位于 `org.javaboy.tienchin.system.service.impl.SysDeptServiceImpl#selectDeptList` 位置，具体方法如下：

```java
@Override
@DataScope(deptAlias = "d")
public List<SysDept> selectDeptList(SysDept dept) {
    return deptMapper.selectDeptList(dept);
}
```

这个参数 SysDept 继承自 BaseEntity，而 BaseEntity 中有一个 params 属性，这个咱们前面也已经介绍过了，不再赘述。

我们来看下这个 selectDeptList 方法对应的 SQL：

```sql
<sql id="selectDeptVo">
    select d.dept_id, d.parent_id, d.ancestors, d.dept_name, d.order_num, d.leader, d.phone, d.email, d.status, d.del_flag, d.create_by, d.create_time 
    from sys_dept d
</sql>
<select id="selectDeptList" parameterType="SysDept" resultMap="SysDeptResult">
    <include refid="selectDeptVo"/>
    where d.del_flag = '0'
 <if test="deptId != null and deptId != 0">
  AND dept_id = #{deptId}
 </if>
    <if test="parentId != null and parentId != 0">
  AND parent_id = #{parentId}
 </if>
 <if test="deptName != null and deptName != ''">
  AND dept_name like concat('%', #{deptName}, '%')
 </if>
 <if test="status != null and status != ''">
  AND status = #{status}
 </if>
 <!-- 数据范围过滤 -->
 ${params.dataScope}
 order by d.parent_id, d.order_num
</select>
```

大家可以看到，在 SQL 的最后面有一句 `${params.dataScope}`，就是把在 DataScopeAspect 切面中拼接的 SQL 追加进来。

所以这个 SQL 最终的形式类似下面这样：

```sql
SELECT
	d.dept_id,
	d.parent_id,
	d.ancestors,
	d.dept_name,
	d.order_num,
	d.leader,
	d.phone,
	d.email,
	d.STATUS,
	d.del_flag,
	d.create_by,
	d.create_time 
FROM
	sys_dept d 
WHERE
	d.del_flag = '0' 
	AND ( d.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 2 ) ) 
ORDER BY
	d.parent_id,
	d.order_num
```



**可以看到，追加了最后面的 SQL 之后，就实现了数据过滤（这里是根据自定义数据权限进行过滤）。**那么这里还涉及到一个细节，前面 SQL 在定义时，用的表别名是什么，我们在 `@DataScope` 中指定的别名就要是什么。



## 4.2 查询角色

首先查询角色的方法位于 `org.javaboy.tienchin.system.service.impl.SysRoleServiceImpl#selectRoleList` 位置，具体方法如下：

```java
@Override
@DataScope(deptAlias = "d")
public List<SysRole> selectRoleList(SysRole role) {
    return roleMapper.selectRoleList(role);
}
```

这个参数 SysRole 继承自 BaseEntity，而 BaseEntity 中有一个 params 属性，这个咱们前面也已经介绍过了，不再赘述。

我们来看下这个 selectRoleList 方法对应的 SQL：

```sql
<sql id="selectRoleVo">
    select distinct r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope, r.menu_check_strictly, r.dept_check_strictly,
        r.status, r.del_flag, r.create_time, r.remark 
    from sys_role r
        left join sys_user_role ur on ur.role_id = r.role_id
        left join sys_user u on u.user_id = ur.user_id
        left join sys_dept d on u.dept_id = d.dept_id
</sql>
<select id="selectRoleList" parameterType="SysRole" resultMap="SysRoleResult">
 <include refid="selectRoleVo"/>
 where r.del_flag = '0'
 <if test="roleId != null and roleId != 0">
  AND r.role_id = #{roleId}
 </if>
 <if test="roleName != null and roleName != ''">
  AND r.role_name like concat('%', #{roleName}, '%')
 </if>
 <if test="status != null and status != ''">
  AND r.status = #{status}
 </if>
 <if test="roleKey != null and roleKey != ''">
  AND r.role_key like concat('%', #{roleKey}, '%')
 </if>
 <if test="params.beginTime != null and params.beginTime != ''"><!-- 开始时间检索 -->
  and date_format(r.create_time,'%y%m%d') &gt;= date_format(#{params.beginTime},'%y%m%d')
 </if>
 <if test="params.endTime != null and params.endTime != ''"><!-- 结束时间检索 -->
  and date_format(r.create_time,'%y%m%d') &lt;= date_format(#{params.endTime},'%y%m%d')
 </if>
 <!-- 数据范围过滤 -->
 ${params.dataScope}
 order by r.role_sort
</select>

```

大家可以看到，在 SQL 的最后面有一句 `${params.dataScope}`，就是把在 DataScopeAspect 切面中拼接的 SQL 追加进来。

所以这个 SQL 最终的形式类似下面这样：

```sql
SELECT DISTINCT
	r.role_id,
	r.role_name,
	r.role_key,
	r.role_sort,
	r.data_scope,
	r.menu_check_strictly,
	r.dept_check_strictly,
	r.STATUS,
	r.del_flag,
	r.create_time,
	r.remark 
FROM
	sys_role r
	LEFT JOIN sys_user_role ur ON ur.role_id = r.role_id
	LEFT JOIN sys_user u ON u.user_id = ur.user_id
	LEFT JOIN sys_dept d ON u.dept_id = d.dept_id 
WHERE
	r.del_flag = '0' 
	AND ( d.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 2 ) ) 
ORDER BY
	r.role_sort 
	LIMIT ?
```

**可以看到，追加了最后面的 SQL 之后，就实现了数据过滤（这里是根据自定义数据权限进行过滤）。**

过滤的逻辑就是根据用户所属的部门 id 找到用户 id，然后根据用户 id 找到对应的角色 id，最后再把查询到的角色返回。

其实我觉得查询部门和查询用户进行数据过滤，这个都好理解，当前登录用户能够操作哪些部门，能够操作哪些用户，这些都容易理解，能操作哪些角色该如何理解呢？特别是上面这个查询 SQL 绕了一大圈，有的小伙伴可能会说，系统本来不就有一个 `sys_role_dept` 表吗？这个表就是关联角色信息和部门信息的，直接拿着用户的部门 id 来这张表中查询用户能操作的角色 id 不就行行了？此言差矣！这里我觉得大家应该这样来理解：用户所属的部门这是用户所属的部门，用户能操作的部门是能操作的部门，这两个之间没有必然联系。sys_user 表中的 dept_id 字段是表示这个用户所属的部门 id，而 sys_role_dept 表中是描述某一个角色能够操作哪些部门，这是不一样的，把这个捋清楚了，上面的 SQL 就好懂了。



## 4.3 查询用户

最后再来看查询用户。

查询用户的方法在 `org.javaboy.tienchin.system.service.impl.SysUserServiceImpl#selectUserList` 位置，对应的 SQL 如下：

```sql
<select id="selectUserList" parameterType="SysUser" resultMap="SysUserResult">
    select u.user_id, u.dept_id, u.nick_name, u.user_name, u.email, u.avatar, u.phonenumber, u.password, u.sex, u.status, u.del_flag, u.login_ip, u.login_date, u.create_by, u.create_time, u.remark, d.dept_name, d.leader from sys_user u
 left join sys_dept d on u.dept_id = d.dept_id
 where u.del_flag = '0'
 <if test="userId != null and userId != 0">
  AND u.user_id = #{userId}
 </if>
 <if test="userName != null and userName != ''">
  AND u.user_name like concat('%', #{userName}, '%')
 </if>
 <if test="status != null and status != ''">
  AND u.status = #{status}
 </if>
 <if test="phonenumber != null and phonenumber != ''">
  AND u.phonenumber like concat('%', #{phonenumber}, '%')
 </if>
 <if test="params.beginTime != null and params.beginTime != ''"><!-- 开始时间检索 -->
  AND date_format(u.create_time,'%y%m%d') &gt;= date_format(#{params.beginTime},'%y%m%d')
 </if>
 <if test="params.endTime != null and params.endTime != ''"><!-- 结束时间检索 -->
  AND date_format(u.create_time,'%y%m%d') &lt;= date_format(#{params.endTime},'%y%m%d')
 </if>
 <if test="deptId != null and deptId != 0">
  AND (u.dept_id = #{deptId} OR u.dept_id IN ( SELECT t.dept_id FROM sys_dept t WHERE find_in_set(#{deptId}, ancestors) ))
 </if>
 <!-- 数据范围过滤 -->
 ${params.dataScope}
</select>
```

这个就比较容易了，根据部门查询用户或者就是查询当前用户。最终生成的 SQL 类似下面这样（这是自定义数据权限，即根据用户部门查找用户）：

```sql
SELECT
	u.user_id,
	u.dept_id,
	u.nick_name,
	u.user_name,
	u.email,
	u.avatar,
	u.phonenumber,
	u.PASSWORD,
	u.sex,
	u.STATUS,
	u.del_flag,
	u.login_ip,
	u.login_date,
	u.create_by,
	u.create_time,
	u.remark,
	d.dept_name,
	d.leader 
FROM
	sys_user u
	LEFT JOIN sys_dept d ON u.dept_id = d.dept_id 
WHERE
	u.del_flag = '0' 
	AND ( d.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 2 ) ) 
	LIMIT ?
```
