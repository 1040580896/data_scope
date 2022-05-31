package com.th.ds;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

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
                    builder.addInclude("sys_dept", "sys_role", "sys_user") // 设置需要生成的表名
                            .addTablePrefix("sys_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }

}
