package com.backend.uujob.service;

import com.backend.uujob.entity.Application;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IApplicationService extends IService<Application> {
    List<Application> getByJobIdAndStatus(int jobId, int status);

    Application getByProfileId(int profileId);

    Boolean checkApplicationExist(Integer profileId,Integer jobId);
}
