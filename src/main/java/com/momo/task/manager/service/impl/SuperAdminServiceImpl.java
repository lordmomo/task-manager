package com.momo.task.manager.service.impl;

import com.momo.task.manager.model.User;
import com.momo.task.manager.dto.UserDto;
import com.momo.task.manager.repository.SuperAdminRepository;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    SuperAdminRepository superAdminRepository;

    ModelMapper mapper;

    @Override
    public String createAdmin(UserDto userDto) {
        User user = mapper.map(userDto,User.class);
        superAdminRepository.save(user);
        return "user added";
    }
}

