package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuperAdminService {

    ResponseEntity<String> createUser(UserCreateDto userCreateDto);

    ResponseEntity<UserResponseDto> getUserDetails(String username);

    ResponseEntity<List<UserResponseDto>> getAllUsers();

    ResponseEntity<String> removeUser(String username);

    ResponseEntity<String> updateUserDetails(String username, UserDetailsDto userDetailsDto);

    ResponseEntity<String> updateUserCredentials(String username, UserCredentialsDto userCredentialsDto);

    ResponseEntity<String> updateUserProfilePicture(String username, MultipartFile file) throws IOException;

    ResponseEntity<String> createProject(ProjectDto projectDto);

    ResponseEntity<String> updateProject(String projectKey, UpdateProjectDto updateProjectDto);

    ResponseEntity<String> deleteProject(String projectKey);

    ResponseEntity<String> addUsersToProject(String projectKey, String username);

    ResponseEntity<List<UserResponseDto>> getUsers(Long role);

}
