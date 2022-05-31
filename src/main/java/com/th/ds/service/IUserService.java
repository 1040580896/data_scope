package com.th.ds.service;

import com.th.ds.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */
public interface IUserService extends IService<User> {

    List<User> getAllUsers(User user);
}
