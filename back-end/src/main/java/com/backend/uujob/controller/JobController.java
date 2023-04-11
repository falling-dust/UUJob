package com.backend.uujob.controller;

import com.backend.uujob.entity.Application;
import com.backend.uujob.entity.Job;
import com.backend.uujob.result.Result;
import com.backend.uujob.service.IJobService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Resource
    private IJobService jobService;

    @PostMapping("")
    public Result addJob(@RequestBody Job job){
        jobService.save(job);
        return Result.success(job.getId());
    }

    @GetMapping("")
    public Result getJob(@RequestParam int id){
        return Result.success(jobService.getById(id));
    }

    @GetMapping("/basis")
    public Result getJobList(){
        return Result.success(jobService.list());
    }

}