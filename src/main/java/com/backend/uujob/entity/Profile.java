package com.backend.uujob.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "profile")
public class Profile implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private short age;
    private String name;
    private String sex;
    private String email;
    private Integer seeker_id;
    private String edu;
    private String description;


    public Profile(Integer seeker_id) {
        this.seeker_id = seeker_id;
    }
    public Profile() {
        ;
    }

}
