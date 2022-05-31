package com.th.ds.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: data_scope
 * @description:
 * @author: xiaokaixin
 * @create: 2022-05-31 07:29
 **/
public class BaseEntity {

    @TableField(exist = false)
    private Map<String,String> params = new HashMap<>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
