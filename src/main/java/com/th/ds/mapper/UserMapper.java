package com.th.ds.mapper;

import com.th.ds.entity.Role;
import com.th.ds.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
public interface UserMapper extends BaseMapper<User> {

    List<Role> getRolesByUid(Long userId);

    List<User> getAllUsers(User user);
}
