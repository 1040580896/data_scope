package com.th.ds.service;

import com.th.ds.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
public interface IRoleService extends IService<Role> {

    List<Role> getAllRoles(Role role);
}
