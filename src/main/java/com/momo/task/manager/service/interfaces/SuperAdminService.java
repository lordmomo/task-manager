package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.UserCredentialsDto;
import com.momo.task.manager.dto.UserDetailsDto;
import com.momo.task.manager.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuperAdminService {

    String createAdmin(String firstName, String lastName, String email,
                       String username, String password, Long role,
                       MultipartFile picture) throws IOException;

    UserDto getUserDetails(Long userId);

    List<UserDto> getAllUsers();

    boolean removeUser(Long userId);

    boolean updateUserDetails(Long userId, UserDetailsDto userDetailsDto);

    boolean updateUserCredentials(Long userId, UserCredentialsDto userCredentialsDto);

    boolean updateUserProfilePicture(Long userId, MultipartFile file) throws IOException;
}
