package com.backend.uujob.service.impl;

import com.backend.uujob.entity.Star;
import com.backend.uujob.mapper.StarMapper;
import com.backend.uujob.service.IStarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StarService extends ServiceImpl<StarMapper, Star> implements IStarService {
    @Resource
    private StarMapper starMapper;
}
