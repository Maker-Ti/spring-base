package com.maker.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maker.demo.entity.User;
import com.maker.demo.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Maker
 * @since 2021-04-18
 */
@Api
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/image",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImages(String name) throws Exception {

        File file = new File("D:\\img\\"+name);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;

    }

    @GetMapping("/login")
    public String login(String username,String password){
        Map<String,Object> resMap = new HashMap<>();
        String res = "";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User u = userService.userLogin(user);
        if (u!=null){
            resMap.put("data","登录成功");
            resMap.put("status",200);
        }else {
            resMap.put("data","登录失败，请检查用户名与密码是否对应");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/registerUser")
    public String registerUser(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        User u = JSON.toJavaObject(params,User.class);
        System.out.println("user register:"+params.toString());
        User isExist = userService.getUserByUserName(u.getUsername());
        System.out.println("user register searching user:"+params.toString());
        if(isExist!=null){
            resMap.put("data","注册失败，该用户名已被注册");
            resMap.put("status",400);
        }else {
            boolean flag = userService.save(u);
            if (flag){
                resMap.put("data","注册成功");
                resMap.put("status",200);
            }else {
                resMap.put("data","注册失败");
                resMap.put("status",400);
            }
        }

        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getUserInfo")
    public String getUserInfo(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        String username = params.getString("username");
        User u = userService.getUserByUserName(username);
        if (u!=null){
            resMap.put("data",u);
            resMap.put("status",200);
            resMap.put("msg","获取用户信息成功");
        }else {
            resMap.put("data","null");
            resMap.put("status",500);
            resMap.put("msg","获取用户信息失败，请检查用户名是否正确");
        }

        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/updateUser")
    public String updateUser(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        User u = JSON.toJavaObject(params.getJSONObject("user"),User.class);
        System.out.println(u.toString());
        boolean flag = userService.updateById(u);
        if (flag){
            resMap.put("data","更改成功");
            resMap.put("status",200);
        }else {
            resMap.put("data","更改失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }

}

