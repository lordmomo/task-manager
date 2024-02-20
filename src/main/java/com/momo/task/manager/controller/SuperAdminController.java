package com.momo.task.manager.controller;

import com.momo.task.manager.dto.UserDto;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/super")
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping("/create-admin")
    public String createAdmin(@RequestBody UserDto userDto){
        return superAdminService.createAdmin(userDto);
    }
}
