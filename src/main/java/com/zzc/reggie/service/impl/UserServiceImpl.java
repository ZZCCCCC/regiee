package com.zzc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.reggie.entity.User;
import com.zzc.reggie.mapper.UserMapper;
import com.zzc.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author: 赵智超
 * @date: 2023/07/03/20:44
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
