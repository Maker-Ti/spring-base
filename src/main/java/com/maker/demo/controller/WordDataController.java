package com.maker.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
import com.maker.demo.dataCtrl.ModelFactory;
import com.maker.demo.entity.Authority;
import com.maker.demo.entity.Library;
import com.maker.demo.entity.User;
import com.maker.demo.entity.WordData;
import com.maker.demo.service.AuthorityService;
import com.maker.demo.service.LibraryService;
import com.maker.demo.service.UserService;
import com.maker.demo.service.WordDataService;
import com.maker.demo.util.TimeHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Maker
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/wordData")
public class WordDataController {
    @Autowired
    LibraryService libraryService;

    @Autowired
    WordDataService wordDataService;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/createUser")
    public void  createUser(){
        for (int i=0;i<20;i++){
            User u = ModelFactory.listStudentList(2);
            System.out.println(u.toString());
            userService.save(u);
        }

    }

    @GetMapping("/addUserSubData")
    public void  addUserSubData(){
        List<User> users = userService.list();
        List<Library> libraries = libraryService.list();
        for (User u:users
             ) {
            int max = ModelFactory.FAKER.number().numberBetween(10,20);
            for(int i=0;i<=max;i++){
                int index = (int) (Math.random()*libraries.size());
                Authority authority = new Authority();
                authority.setLibrary(libraries.get(index).getId());
                authority.setPartner(u.getId());
                authorityService.save(authority);
            }
        }

    }

    @GetMapping("/testRestTemplate")
    public String testRestTemplate(){
        String url = "http://rw.ylapi.cn/reciteword/wordlist.u?uid=11650&appkey=ea04f841fdc959f69f6ad0d3141eb854&class_id=45090&course=1";
        JSONObject postData = new JSONObject();
        postData.put("descp", "request for post");
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json.toJSONString();
    }
    public JSONArray getPostWord(int id,int pages){
        String url = "http://rw.ylapi.cn/reciteword/wordlist.u?uid=11650&appkey=ea04f841fdc959f69f6ad0d3141eb854&class_id="+id+"&course="+pages;
        JSONObject postData = new JSONObject();
        postData.put("descp", "request for post");
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        JSONArray jsonArray = null;
        if(json.getString("code").equals("1000")){
            jsonArray = json.getJSONArray("datas");
        }
        return jsonArray;
    }
    @GetMapping("/insertSingle")
    public void insertSingle(){
        Library library = new Library();
        library.setId(123321);
        library.setName("child_title");
        library.setCreator(1);
        library.setTime(TimeHelper.getTimeInstance());
        library.setType(1);
        library.setInfo("title+child_title");
        libraryService.save(library);
    }
    @GetMapping("/startCreateLib")
    public void startCreateLib(){
        String fileName = "D:\\graduationProject\\wordSystem\\src\\main\\java\\com\\maker\\demo\\dataCtrl\\lib.json";
        JSONObject json = fileToJson(fileName);
        System.out.println(json.toJSONString());
        JSONArray jsonArray = new JSONArray(json.getJSONArray("datas"));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String title = item.getString("title");
            JSONArray child = item.getJSONArray("child_list");
            for (int j = 0; j < child.size(); j++) {
                JSONObject child_item = child.getJSONObject(j);
                String child_title = child_item.getString("title");
                int id = Integer.valueOf(child_item.getString("class_id"));

                System.out.println(id+title+":"+child_title);
            }
        }
    }
    @GetMapping("/startCreateLibContent")
    public void startCreateLibContent(){
        String fileName = "D:\\graduationProject\\wordSystem\\src\\main\\java\\com\\maker\\demo\\dataCtrl\\lib.json";
        JSONObject json = fileToJson(fileName);
        System.out.println(json.toJSONString());
        JSONArray jsonArray = new JSONArray(json.getJSONArray("datas"));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String title = item.getString("title");
            JSONArray child = item.getJSONArray("child_list");
            for (int j = 0; j < child.size(); j++) {
                JSONObject child_item = child.getJSONObject(j);
                String child_title = child_item.getString("title");

                int id = Integer.valueOf(child_item.getString("class_id"));

                int course_num = Integer.valueOf(child_item.getString("course_num"));
                if(course_num>10){
                    course_num = 10;
                }
                System.out.println(title+":"+child_title+","+id+","+course_num);
                JSONArray contentJson = getPostWord(id,course_num);
                JSONArray resultData = new JSONArray();
                System.out.println(contentJson+":"+contentJson.toJSONString());
                if(contentJson!=null){
                    //{"spell":"null","mean":"n.荡妇,娼妓","name":"chippie"}
                    for (int k = 0; k < contentJson.size(); k++) {
                        JSONObject single = contentJson.getJSONObject(k);
                        JSONObject word = new JSONObject();
                        word.put("name",single.getString("name"));
                        word.put("mean",single.getString("desc"));
                        word.put("spell",single.getString("symbol"));
                        resultData.add(word);
                    }
                    Library library = new Library();
                    library.setId(id);
                    library.setName(title+":"+child_title);
                    library.setCreator(1);
                    library.setTime(TimeHelper.getTime());
                    library.setType(1);
                    library.setInfo(title+":"+child_title);
                    library.setContent(resultData.toJSONString());
                    libraryService.save(library);
                }
            }
        }
    }
    public  JSONObject fileToJson(String fileName) {
        JSONObject json = null;
        try {
            File f = new File(fileName);
            InputStream is = new FileInputStream(f);
            json = JSONObject.parseObject(IOUtils.toString(is, "utf-8"));
        } catch (Exception e) {
            System.out.println(fileName + "文件读取异常" + e);
        }
        return json;
    }

    //数据库生成接口
    @GetMapping("/startTransform")
    public void startTransform(){
        int i;
        Library library = new Library();
        for(i=1;i<7;i++){
            library = new Library();
            library.setId(i);
            int j;
            JSONArray jsonArray = new JSONArray();
            for(j=0;j<2000;j++){
                JSONObject jsonObject = new JSONObject();
                WordData wordData = wordDataService.getById(i+(j*8));
                jsonObject.put("name",wordData.getWord());
                jsonObject.put("spell","null");
                jsonObject.put("mean",wordData.getMean());
                jsonArray.add(jsonObject);
            }
            library.setContent(JSONArray.toJSONString(jsonArray));
            libraryService.updateById(library);
        }

    }
}

