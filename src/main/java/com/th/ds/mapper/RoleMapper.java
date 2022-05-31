package com.th.ds.mapper;

import com.th.ds.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */

public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getAllRoles(Role role);

}
