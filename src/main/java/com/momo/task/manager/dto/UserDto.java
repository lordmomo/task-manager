package com.momo.task.manager.dto;

import com.momo.task.manager.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Role role;
    private MultipartFile pictureFile;

}
