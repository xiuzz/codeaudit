package org.async.codeaudit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.async.codeaudit.entiy.User;
import org.async.codeaudit.mapper.UserMapper;
import org.async.codeaudit.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
}
