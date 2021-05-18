package com.maker.demo.dataCtrl;

import com.github.javafaker.Faker;
import com.maker.demo.entity.User;

import java.util.List;
import java.util.stream.Stream;

public class ModelFactory {
    public static Faker FAKER = new Faker();


    /**
     * 随机生成一定数量学生
     *
     * @param number 数量
     * @return 学生
     */
    public static User listStudentList(final int number) {
        User u = new User(
                FAKER.name().firstName(), ""+FAKER.number().numberBetween(100000,999999),""+FAKER.phoneNumber().phoneNumber(),FAKER.number().numberBetween(100000000,999999999)+"@qq.com");
        return u;
    }


    /**
     * main函数
     */
    public static void main(String[] args) throws Exception {

    }

}
