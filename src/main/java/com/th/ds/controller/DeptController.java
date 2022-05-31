package com.th.ds.controller;


import com.th.ds.annotation.DataScope;
import com.th.ds.entity.Dept;
import com.th.ds.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.BindingType;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    IDeptService deptService;

    @GetMapping("/")
    public List<Dept> getAllDepts(Dept dept){
        return deptService.getAllDepts(dept);
    }
}
