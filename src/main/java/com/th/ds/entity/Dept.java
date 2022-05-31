package com.th.ds.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@TableName("sys_dept")

public class Dept extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty("部门id")
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    //@ApiModelProperty("父部门id")
    private Long parentId;

    //@ApiModelProperty("祖级列表")
    private String ancestors;

    //@ApiModelProperty("部门名称")
    private String deptName;

    //@ApiModelProperty("显示顺序")
    private Integer orderNum;

    //@ApiModelProperty("负责人")
    private String leader;

    //@ApiModelProperty("联系电话")
    private String phone;

    //@ApiModelProperty("邮箱")
    private String email;

    //@ApiModelProperty("部门状态（0正常 1停用）")
    private String status;

    //@ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    //@ApiModelProperty("创建者")
    private String createBy;

    //@ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    //@ApiModelProperty("更新者")
    private String updateBy;

    //@ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "Dept{" +
            "deptId=" + deptId +
            ", parentId=" + parentId +
            ", ancestors=" + ancestors +
            ", deptName=" + deptName +
            ", orderNum=" + orderNum +
            ", leader=" + leader +
            ", phone=" + phone +
            ", email=" + email +
            ", status=" + status +
            ", delFlag=" + delFlag +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", updateBy=" + updateBy +
            ", updateTime=" + updateTime +
        "}";
    }
}
