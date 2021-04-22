package com.maker.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maker.demo.entity.Library;
import com.maker.demo.entity.User;
import com.maker.demo.service.LibraryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
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
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    @PostMapping("/addLibrary")
    public String addLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        Library library = JSON.toJavaObject(params.getJSONObject("library"), Library.class);

            boolean flag = libraryService.save(library);
            if (flag){
                resMap.put("msg","添加成功");
                resMap.put("status",200);
            }else {
                resMap.put("msg","添加失败");
                resMap.put("status",400);
            }


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
    @PostMapping("/updateLibrary")
    public String updateUser(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();
        int id = params.getInteger("id");
        String content = params.getString("content");
        Library library = new Library();
        library.setId(id);
        library.setContent(content);
        System.out.println(library.toString());
        boolean flag = libraryService.updateById(library);
        if (flag){
            resMap.put("data","更改成功");
            resMap.put("msg","更改成功");
            resMap.put("status",200);
        }else {
            resMap.put("data","更改成功");
            resMap.put("msg","更改失败");
            resMap.put("status",400);
        }
        return JSONObject.toJSON(resMap).toString();
    }
    @PostMapping("/removeLibrary")
    public String removeLibrary(@RequestBody JSONObject params) {
        Map<String,Object> resMap = new HashMap<>();

        Library library = JSON.toJavaObject(params.getJSONObject("library"),Library.class);
        System.out.println(library.toString());
        boolean flag = libraryService.removeById(library.getId());
        if (flag){
            resMap.put("msg","删除成功");
            resMap.put("status",200);
        }else {
            resMap.put("msg","删除失败");
            resMap.put("status",400);
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

}

