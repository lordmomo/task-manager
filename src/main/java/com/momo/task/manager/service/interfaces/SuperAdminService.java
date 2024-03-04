package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.request.*;
import com.momo.task.manager.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuperAdminService {

    ResponseEntity<String> createUser(UserCreateRequestDto userCreateRequestDto);

    ResponseEntity<UserResponseDto> getUserDetails(String username);

    ResponseEntity<List<UserResponseDto>> getAllUsers();

    ResponseEntity<String> removeUser(String username);

    ResponseEntity<String> updateUserDetails(String username, UserDetailsRequestDto userDetailsRequestDto);

    ResponseEntity<String> updateUserCredentials(String username, UserCredentialsRequestDto userCredentialsRequestDto);

    ResponseEntity<String> updateUserProfilePicture(String username, MultipartFile file) throws IOException;

    ResponseEntity<String> createProject(ProjectCreateRequestDto projectCreateRequestDto);

    ResponseEntity<String> updateProject(String projectKey, UpdateProjectRequestDto updateProjectRequestDto);

    ResponseEntity<String> deleteProject(String projectKey);

    ResponseEntity<String> addUsersToProject(String projectKey, String username);

    ResponseEntity<List<UserResponseDto>> getUsers(Long role);

}
