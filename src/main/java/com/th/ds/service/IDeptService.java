package com.th.ds.service;

import com.th.ds.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
public interface IDeptService extends IService<Dept> {

    List<Dept> getAllDepts(Dept dept);
}
