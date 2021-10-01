package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("userList")
    @ResponseBody
    public List<UserInfo> userList(){
        List<UserInfo> userInfos = userService.userList();
        return userInfos;
    }
//    @RequestMapping("saveUser")
//    @ResponseBody
//    public int saveUser(){
//        UserInfo userInfo = new UserInfo("1", "ww", "ss", "123", "ws", "1364322", "1231@qq.com", null, null);
//        return userService.saveUser(userInfo);
//    }
}
