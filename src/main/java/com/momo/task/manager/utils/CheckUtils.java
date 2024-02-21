package com.momo.task.manager.utils;

import com.momo.task.manager.repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckUtils {

    @Autowired
    AccessRepository accessRepository;

    public Long checkUserProjectAccess(Long userId,Long projectId){
        return accessRepository.validateUserProjectRelation(userId,projectId);
    }

}
