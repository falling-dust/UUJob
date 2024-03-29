package com.backend.uujob.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Star {
  @TableId(value = "id",type = IdType.AUTO)
  private Integer id;
  private Integer userId;
  private Integer starType;
  private Integer targetId;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public Integer getStarType() {
    return starType;
  }

  public void setStarType(Integer starType) {
    this.starType = starType;
  }


  public Integer getTargetId() {
    return targetId;
  }

  public void setTargetId(Integer targetId) {
    this.targetId = targetId;
  }

}
