package com.maker.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Maker
 * @since 2021-04-22
 */
public class Library implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer creator;

    private String info;

    private Integer type;

    private String content;

    private String name;

    private String time;

    private Integer mutable;

    public Integer getMutable() {
        return mutable;
    }

    public void setMutable(Integer mutable) {
        this.mutable = mutable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", creator=" + creator +
                ", info='" + info + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", mutable=" + mutable +
                '}';
    }
}
