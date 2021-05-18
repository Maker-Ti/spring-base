package com.maker.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maker.demo.entity.User;
import com.maker.demo.service.UserService;
import com.maker.demo.util.TimeHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
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

    //String imageFileDir = "D:\\img\\";
    String imageFileDir ="/root/img/";
    //通过produces 告知浏览器我要返回的媒体类型
    @GetMapping(value = "/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ApiOperation("获取图片-返回BufferedImage")
    public BufferedImage getImage2(String name) throws IOException {
        System.out.println("getImage2:"+imageFileDir+name);
        return ImageIO.read(new FileInputStream(new File(imageFileDir+name)));
    }

    @GetMapping(value = "/getImage",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImages(String name) throws Exception {
        //File file = new File("/root/img/"+name);
        System.out.println("getImage:"+imageFileDir+name);
        File file = new File(imageFileDir+name);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

    @GetMapping("/login")
    public String login(String username,String password){
        Map<String,Object> resMap = new HashMap<>();
        String res = "";
        User user = null;
        System.out.println(username+","+password);
        int id = -1;
        try{
            id = Integer.valueOf(username);
        }catch (Exception e){

        }
        if(id!=-1){
            User check = userService.getById(id);
            if(check!=null){
                if(check.getPassword().equals(password)){
                    user = new User();
                }
            }

        }else {
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user = userService.userLogin(user);
        }


        if (user!=null){
            resMap.put("data","登录成功");
            resMap.put("status",200);
        }else {
            resMap.put("data","登录失败，请检查用户名或ID与密码是否对应");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/userHeaderUpload")
    public String userHeaderUpload(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        String url = params.getString("url");

        int index = url.indexOf("base64,")+7;
        url = url.substring(index);
        System.out.println("getImage:"+url);
        int id = params.getInteger("id");
        String instance = TimeHelper.getTimeInstance()+".png";
        String fileName = imageFileDir+ instance;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(url);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(fileName);
            out.write(bytes);
            out.flush();
            out.close();
            //====

        } catch (Exception e) {
            resMap.put("data","上传图片出错！");
            resMap.put("status",500);
            return JSONObject.toJSON(resMap).toString();
        }
        User user = new User();
        user.setId(id);
        user.setToken(instance);
        boolean flag = userService.updateById(user);
        if(flag){
            resMap.put("data","上传成功！");
            resMap.put("status",200);
        }else {
            resMap.put("data","用户绑定头像出错！");
            resMap.put("status",500);
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
            resMap.put("status",500);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/searchingUser")
    public String searchingUser(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        String info = params.getString("info");
        User u = userService.getUserByUserName(info);

        if(u!=null){
            resMap.put("data",u);
            resMap.put("msg","查询名称为:"+info+"的用户");
            resMap.put("status",200);
        }else {
            try{
                u = userService.getById(Integer.valueOf(info));
            }catch (Exception e){

            }
             if (u!=null){
                 resMap.put("data",u);
                 resMap.put("msg","查询id为:"+info+"的用户");
                 resMap.put("status",200);
             }else {
                 resMap.put("data",u);
                 resMap.put("msg","查询不到该用户");
                 resMap.put("status",400);
             }
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getFollowingList")
    public String getFollowingList(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        String following = params.getString("following");
        if(following!=null&&following.length()>0){
            following = following.substring(0,following.length()-1);

             String[] users = following.split(";");
             String query = "";
            for (int i = 0; i <users.length ; i++) {
                if(i<users.length-1){
                    query = query+users[i]+",";
                }else {
                    query = query+users[i];
                }
            }
            System.out.println(query);
            List<User> result = userService.getFollowingUser(query);
            resMap.put("data",result);
            resMap.put("msg","查询成功");
            resMap.put("status",200);
        }else {
            resMap.put("msg","传输的人数为0");
            resMap.put("data","传输的人数为0");
            resMap.put("status",200);
        }
        return JSONObject.toJSON(resMap).toString();
    }

}

