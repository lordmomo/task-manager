package com.momo.task.manager.dto;


import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserCreateDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Long roleId;
    @Nullable
    private MultipartFile pictureFile;
}
