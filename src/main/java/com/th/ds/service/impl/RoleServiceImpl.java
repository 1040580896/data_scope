package com.th.ds.service.impl;

import com.th.ds.annotation.DataScope;
import com.th.ds.entity.Role;
import com.th.ds.mapper.RoleMapper;
import com.th.ds.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    @DataScope(deptAlias = "d")
    public List<Role> getAllRoles(Role role) {
        return roleMapper.getAllRoles(role);
    }
}
