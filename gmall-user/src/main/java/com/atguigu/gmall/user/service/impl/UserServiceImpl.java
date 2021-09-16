package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.user.bean.UserInfo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> userList() {
        return userInfoMapper.selectAllUserAndAddress();
    }

    @Override
    public int saveUser(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }
}
