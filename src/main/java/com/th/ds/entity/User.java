package com.th.ds.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@TableName("sys_user")
// @ApiModel(value = "User对象", description = "用户信息表")
public class User extends BaseEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    // ////@ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    // ////@ApiModelProperty("部门ID")
    private Long deptId;

    // ////@ApiModelProperty("用户账号")
    private String userName;

    // ////@ApiModelProperty("用户昵称")
    private String nickName;

    ////@ApiModelProperty("用户类型（00系统用户）")
    private String userType;

    ////@ApiModelProperty("用户邮箱")
    private String email;

    ////@ApiModelProperty("手机号码")
    private String phonenumber;

    ////@ApiModelProperty("用户性别（0男 1女 2未知）")
    private String sex;

    // ////@ApiModelProperty("头像地址")
    private String avatar;

    // ////@ApiModelProperty("密码")
    private String password;

    // ////@ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;

    // ////@ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    // ////@ApiModelProperty("最后登录IP")
    private String loginIp;

    // ////@ApiModelProperty("最后登录时间")
    private LocalDateTime loginDate;

    // ////@ApiModelProperty("创建者")
    private String createBy;

    // ////@ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    // ////@ApiModelProperty("更新者")
    private String updateBy;

    // ////@ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    //todo JsonIgnore
    @TableField(exist = false)
    @JsonIgnore
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    // ////@ApiModelProperty("备注")
    private String remark;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r->new SimpleGrantedAuthority(r.getRoleKey())).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", deptId=" + deptId +
            ", userName=" + userName +
            ", nickName=" + nickName +
            ", userType=" + userType +
            ", email=" + email +
            ", phonenumber=" + phonenumber +
            ", sex=" + sex +
            ", avatar=" + avatar +
            ", password=" + password +
            ", status=" + status +
            ", delFlag=" + delFlag +
            ", loginIp=" + loginIp +
            ", loginDate=" + loginDate +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", updateBy=" + updateBy +
            ", updateTime=" + updateTime +
            ", remark=" + remark +
        "}";
    }
}
