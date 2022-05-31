package com.th.ds.service.impl;

import com.th.ds.annotation.DataScope;
import com.th.ds.entity.Dept;
import com.th.ds.mapper.DeptMapper;
import com.th.ds.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Autowired
    DeptMapper deptMapper;


    @Override
    @DataScope(deptAlias = "d")
    public List<Dept> getAllDepts(Dept dept) {
        return deptMapper.getAllDepts(dept);
    }
}
