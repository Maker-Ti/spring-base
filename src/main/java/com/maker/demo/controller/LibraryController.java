package com.maker.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maker.demo.entity.*;
import com.maker.demo.recommend.recommender.UserRecommender;
import com.maker.demo.service.*;
import com.maker.demo.util.TimeHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.*;

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
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    @Autowired
    OperateService operateService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    UserService userService;



    @PostMapping("/addLibrary")
    public String addLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int creator = params.getInteger("operator");
        Library library = JSON.toJavaObject(params.getJSONObject("library"), Library.class);
        library.setCreator(creator);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",library.getName());
        queryWrapper.eq("creator",library.getCreator());
        library.setTime(TimeHelper.getTime());
        Library check = libraryService.getOne(queryWrapper);
        if(check!=null){
            resMap.put("msg","添加失败,该名称在您创建的单词库中已存在");
            resMap.put("status",500);
        }else {
            boolean flag = libraryService.save(library);
            if (flag){
                library = libraryService.getOne(queryWrapper);
                resMap.put("msg","添加成功");
                resMap.put("status",200);
                saveOperation("新增词库:"+library.getName(),library.getId(),creator);

            }else {
                resMap.put("msg","添加失败");
                resMap.put("status",500);
            }

        }


        return JSONObject.toJSON(resMap).toString();
    }
    @GetMapping("/libTest")
    public String libTest() {
        Map<String,Object> resMap = new HashMap<>();

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("name","日常单词记忆");
            queryWrapper.eq("creator",1);
            Library library = libraryService.getOne(queryWrapper);
            resMap.put("data",library);
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getSingleLibrary")
    public String getSingleLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = (int) params.get("id");
        Library library = libraryService.getById(id);
        if(library!=null){
            resMap.put("msg","获取成功");
            resMap.put("status",200);
            resMap.put("data",library);
        }else {
            resMap.put("msg","获取失败");
            resMap.put("status",500);
            resMap.put("data",null);
        }

        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getUserRecommend")
    public String getUserRecommend(@RequestBody JSONObject params) throws IOException {
        Map<String,Object> resMap = new HashMap<>();
        int user = params.getInteger("user");
        System.out.println("user rec:"+user);
        List<String> libIds = UserRecommender.getRec(user);
        String query = "";
        for (int i = 0; i < libIds.size(); i++) {
            if(i<libIds.size()-1){
                query = query+libIds.get(i)+",";
            }else {
                query = query+libIds.get(i);
            }
        }
        List<MessageReturn> libs = libraryService.getRecomLib(query);
        resMap.put("msg","获取成功");
        resMap.put("status",200);
        resMap.put("data",libs);
        return JSONObject.toJSON(resMap).toString();
    }

    @PostMapping("/addUserToAuthority")
    public String addUserToAuthority(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = (int) params.get("id");
        int user = (int) params.get("user");
        int operator = params.getInteger("operator");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("library",id);
        queryWrapper.eq("partner",user);
        Authority auth = authorityService.getOne(queryWrapper);
        System.out.println(auth==null);
        if(auth==null){
            auth = new Authority();
            auth.setLibrary(id);
            auth.setPartner(user);
            authorityService.save(auth);
            resMap.put("msg","添加成功");
            resMap.put("status",200);
            resMap.put("data","添加成功");
            saveOperation("添加用户权限"+userService.getById(user).getUsername(),id,operator);

        }else {
            resMap.put("msg","添加失败，该成员已在单词库中");
            resMap.put("status",500);
            resMap.put("data","添加失败，该成员已在单词库中");
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/deleteUserToAuthority")
    public String deleteUserToAuthority(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = (int) params.get("id");
        int lib = (int) params.get("lib");
        int operator = params.getInteger("operator");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("partner",id);
        queryWrapper.eq("library",lib);
        boolean flag = authorityService.remove(queryWrapper);
        if(flag){
            resMap.put("msg","删除成功");
            resMap.put("status",200);
            resMap.put("data","删除成功");
            saveOperation("删除用户权限"+userService.getById(id).getUsername(),lib,operator);
        }else {
            resMap.put("msg","删除失败");
            resMap.put("status",500);
            resMap.put("data","删除失败");
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/searchLib")
    public String searchLib(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        String name = params.getString("info");
        int id = -1;
        Library library = null;
        List<MessageReturn> libs = new ArrayList<>();
        try{
            id = params.getInteger("info");
        }catch (Exception e){

        }
        System.out.println(id);
        if (id!=-1){
            library = libraryService.getById(id);
            if (library!=null){
                User user = userService.getById(library.getCreator());
                MessageReturn messageReturn = new MessageReturn();
                messageReturn.setId(library.getId());
                messageReturn.setInfo(library.getInfo());
                messageReturn.setToken(user.getToken());
                messageReturn.setUsername(user.getUsername());
                messageReturn.setTime(library.getTime());
                messageReturn.setName(library.getName());
                libs.add(messageReturn);
                resMap.put("msg","搜索ID为："+library.getId()+"的词库");
                resMap.put("status",200);
                resMap.put("data",libs);
            }else {
                resMap.put("msg","搜索不到该id");
                resMap.put("status",500);
                resMap.put("data","搜索不到该id");
            }
        }else {
                libs = libraryService.searchInfo(name);
                if(libs.size()>0){
                    resMap.put("msg","搜索名称包含："+name+"的词库");
                    resMap.put("status",200);
                    resMap.put("data",libs);
                }else {
                    resMap.put("msg","搜索不到该信息的词库");
                    resMap.put("status",500);
                    resMap.put("data","搜索不到该信息的词库");
                }

        }



        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getLibraryAuthority")
    public String getLibraryAuthority(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = (int) params.get("id");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("library",id);
        List<Authority> authList = authorityService.list(queryWrapper);
        List<User> userList = new ArrayList<>();
        String str = "";
        if(authList.size()>0){
            for (int i = 0; i < authList.size(); i++) {
                if(i<authList.size()-1){
                    str = str+authList.get(i).getPartner()+",";
                }else {
                    str = str+authList.get(i).getPartner();
                }
            }
        }
        if(str.length()>0){
            userList = userService.getFollowingUser(str);
        }

            resMap.put("msg","获取成功");
            resMap.put("status",200);
            resMap.put("data",userList);
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getRecentOpreation")
    public String getRecentOpreation(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = (int) params.get("id");
        Library library = libraryService.getById(id);
        if(library!=null){
            resMap.put("msg","获取成功");
            resMap.put("status",200);
            resMap.put("data",library);
        }else {
            resMap.put("msg","获取失败");
            resMap.put("status",500);
            resMap.put("data",null);
        }

        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/updateLibraryInfo")
    public String updateLibraryInfo(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        Library lib = JSON.toJavaObject(params.getJSONObject("library"), Library.class);
        System.out.println("update lib:"+lib.toString());
        int operator = params.getInteger("operator");
        String operation = params.getString("operation");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",lib.getName());
        queryWrapper.eq("creator",lib.getCreator());
        Library check = libraryService.getOne(queryWrapper);
        if(check!=null){
            resMap.put("data","更改失败");
            resMap.put("msg","更改失败，该名称已被使用");
            resMap.put("status",200);
        }else {
            boolean flag = libraryService.updateById(lib);
            if (flag){
                resMap.put("data","更改成功");
                resMap.put("msg","更改成功");
                resMap.put("status",200);
                saveOperation(operation,lib.getId(),operator);
            }else {
                resMap.put("data","更改成功");
                resMap.put("msg","更改失败");
                resMap.put("status",400);
            }
        }


        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/updateLibrary")
    public String updateUser(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = params.getInteger("id");
        int operator = params.getInteger("operator");
        String content = params.getString("content");
        String operation = params.getString("operation");
        Library library = new Library();
        library.setId(id);
        library.setContent(content);
        Library check = libraryService.getById(id);
        if(check.getCreator().equals(operator)){

        }else {
            if(check.getMutable() == 0){
                resMap.put("data","抱歉，该词库创建者设置了不可修改");
                resMap.put("msg","抱歉，该词库创建者设置了不可修改");
                resMap.put("status",500);
                return JSONObject.toJSON(resMap).toString();
            }
        }
        System.out.println(library.toString());
        boolean flag = libraryService.updateById(library);
        if (flag){
            resMap.put("data","更改成功");
            resMap.put("msg","更改成功");
            resMap.put("status",200);
            saveOperation(operation,id,operator);
        }else {
            resMap.put("data","更改成功");
            resMap.put("msg","更改失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    //添加操作记录
    public void saveOperation(String operation,int id,int operator){
        Operate operate = new Operate();
        operate.setTime(TimeHelper.getTime());
        operate.setContent(operation);
        operate.setObject(id);
        operate.setOpreator(operator);
        operateService.save(operate);
    }

    //删除词库
    @PostMapping("/removeLibrary")
    public String removeLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        Library library = JSON.toJavaObject(params.getJSONObject("library"),Library.class);
        System.out.println(library.toString());
        boolean flag = libraryService.removeById(library.getId());
        if (flag){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("library",library.getId());
            authorityService.remove(queryWrapper);
            resMap.put("msg","删除成功");
            resMap.put("status",200);
            saveOperation("删除词库:"+library.getName(),library.getId(),params.getInteger("operator"));
        }else {
            resMap.put("msg","删除失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    //删除订阅
    @PostMapping("/removeSubscribe")
    public String removeSubscribe(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        Library library = JSON.toJavaObject(params.getJSONObject("library"),Library.class);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("library",library.getId());
        queryWrapper.eq("partner",params.getInteger("operator"));
        boolean flag = authorityService.remove(queryWrapper);
        if (flag){
            resMap.put("msg","取消订阅成功");
            resMap.put("status",200);
            saveOperation("取消订阅词库:"+library.getName(),library.getId(),params.getInteger("operator"));
        }else {
            resMap.put("msg","取消订阅失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    //添加订阅
    @PostMapping("/addSubscribe")
    public String addSubscribe(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int lib = params.getInteger("lib");
        int partner = params.getInteger("partner");
        System.out.println(partner);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("library",lib);
        queryWrapper.eq("partner",partner);
        boolean flag = authorityService.remove(queryWrapper);
        if (flag){
            resMap.put("msg","您已经订阅了该词库了");
            resMap.put("data","您已经订阅了该词库了");
            resMap.put("status",200);
        }else {
            Authority authority = new Authority();
            authority.setPartner(partner);
            authority.setLibrary(lib);
            boolean add = authorityService.save(authority);
            if(add){
                resMap.put("msg","订阅词库成功");
                resMap.put("data","订阅词库成功");
                Library library = libraryService.getById(lib);
                saveOperation("订阅词库:"+library.getName(),library.getId(),partner);

                resMap.put("status",200);
            }else {
                resMap.put("msg","订阅词库失败");
                resMap.put("data","订阅词库失败");
                resMap.put("status",500);
            }

        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/libraryList")
    public String libraryList(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        int creator = (int) params.get("id");
        List<Library> flag = libraryService.getLibraryList(creator);
        System.out.println("libraryList:"+flag.size());
        if (flag!=null){
            resMap.put("msg","查询成功");
            resMap.put("data",flag);
            resMap.put("status",200);
        }else {
            resMap.put("msg","查询失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/joinedLibrary")
    public String joinedLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        int partner = (int) params.get("id");
        List<Library> flag = libraryService.getJoinedLibrary(partner);
        System.out.println("libraryList:"+flag.size());
        if (flag!=null){
            resMap.put("msg","查询成功");
           resMap.put("data",flag);
            resMap.put("status",200);
        }else {
            resMap.put("msg","查询失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/getRecentMsg")
    public String getRecentMsg(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        int partner = (int) params.get("id");
        List<MessageReturn> data = operateService.getRecentMsg(partner);
        System.out.println("libraryList:"+data.size());
        List<MessageReturn> subData = new ArrayList<>();
        if(data.size()>15){
            subData = data.subList(data.size()-15,data.size());
        }else {
            subData = data;
        }
        Collections.reverse(subData);
        if (data!=null){
            resMap.put("msg","查询成功");
            resMap.put("data",subData);
            resMap.put("status",200);
        }else {
            resMap.put("msg","查询失败");
            resMap.put("status",500);
        }
        return JSONObject.toJSON(resMap).toString();
    }

}

