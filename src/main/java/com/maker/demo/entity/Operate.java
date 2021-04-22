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
 * @since 2021-04-19
 */
public class Operate implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String time;

    private String content;

    private Integer object;

    private Integer opreator;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getObject() {
        return object;
    }

    public void setObject(Integer object) {
        this.object = object;
    }

    public Integer getOpreator() {
        return opreator;
    }

    public void setOpreator(Integer opreator) {
        this.opreator = opreator;
    }

    @Override
    public String toString() {
        return "Operate{" +
        "id=" + id +
        ", time=" + time +
        ", content=" + content +
        ", object=" + object +
        ", opreator=" + opreator +
        "}";
    }
}
