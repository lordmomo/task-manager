package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuperAdminService {

    ResponseEntity<String> createAdmin(UserCreateDto userCreateDto) throws IOException;

    ResponseEntity<String> createUser(UserCreateDto userCreateDto) throws IOException;

    ResponseEntity<UserResponseDto> getUserDetails(Long userId);

    ResponseEntity<List<UserResponseDto>> getAllUsers();

    ResponseEntity<String> removeUser(Long userId);

    ResponseEntity<String> updateUserDetails(Long userId, UserDetailsDto userDetailsDto);

    ResponseEntity<String> updateUserCredentials(Long userId, UserCredentialsDto userCredentialsDto);

    ResponseEntity<String> updateUserProfilePicture(Long userId, MultipartFile file) throws IOException;

    ResponseEntity<String> createProject(ProjectDto projectDto);

    ResponseEntity<String> updateProject(Long projectId, UpdateProjectDto updateProjectDto);

    ResponseEntity<String> deleteProject(Long projectId);

    ResponseEntity<String> addUsersToProject(String projectName, Long userId);
}
