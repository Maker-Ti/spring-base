package com.maker.demo.util;

import com.maker.demo.entity.Authority;
import com.maker.demo.entity.User;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class TXTUtils {
    public static String txtFilePath ="/root/u1.base";
    public static String txtPath ="/root/u1.base";

    //public static String txtFilePath = "D:/graduationProject/wordSystem/src/main/java/com/maker/demo/recommend/data/u1.base";
    //public static String txtPath = "D:\\graduationProject\\wordSystem\\src\\main\\java\\com\\maker\\demo\\recommend\\data\\u1.base";
    public static void writeToTxt(List<Authority> list, String path) {
        FileOutputStream outSTr = null;
        BufferedOutputStream Buff = null;
        String path1 = path;
        String tab = "\t";
        String enter = "\r\n";
        StringBuffer write ;
        try {
            outSTr = new FileOutputStream(new File(path));
            Buff = new BufferedOutputStream(outSTr);
            for (int i = 0; i < list.size(); i++) {
                write = new StringBuffer();
                write.append(list.get(i).getPartner());
                write.append(tab);
                write.append(list.get(i).getLibrary());
                write.append(enter);
                Buff.write(write.toString().getBytes("UTF-8"));
            }
            Buff.flush();
            Buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Buff.close();
                outSTr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
