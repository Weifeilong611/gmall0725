package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UserAddressMapper;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserAddressMapper userAddressMapper;

    @Override
    public List<UserInfo> userList() {
        return userInfoMapper.selectAllUserAndAddress();
    }

    @Override
    public List<UserInfo> getUserList() {

        List<UserInfo> userInfos = userInfoMapper.selectAll();
        return userInfos;
    }

    @Override
    public List<UserAddress> getAddressList() {
        List<UserAddress> userAddresses = userAddressMapper.selectAll();
        return userAddresses;
    }

    @Override
    public List<UserAddress> getAddressListByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> userAddressList = userAddressMapper.select(userAddress);
        return userAddressList;
    }

    @Override
    public UserAddress getAddressListById(String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        UserAddress userAddress1 = userAddressMapper.selectOne(userAddress);
        return userAddress1;
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        UserInfo userInfo1 = userInfoMapper.selectOne(userInfo);

        if(null!=userInfo1){
            // 用户数据放入redis
        }

        return userInfo1;
    }
}
