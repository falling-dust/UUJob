package com.backend.uujob.controller.dto;

import lombok.Data;

import java.util.Date;

@Data
public class JobDTO {
    private Integer id;
    private Integer publisher_id;
    private String title;
    private String position;
    private String salary;
    private Date date;
    private String description;

}
