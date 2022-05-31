package com.th.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.th.ds.annotation.DataScope;
import com.th.ds.entity.User;
import com.th.ds.mapper.UserMapper;
import com.th.ds.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUsername,username);
        User user = getOne(qw);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        user.setRoles(userMapper.getRolesByUid(user.getUserId()));
        return user;
    }

    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<User> getAllUsers(User user) {
        return userMapper.getAllUsers(user);
    }
}
