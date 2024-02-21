package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.model.Project;
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

    String createUser(String firstName, String lastName, String email,
                      String username, String password, Long role,
                      MultipartFile picture) throws IOException;

    void createProject(ProjectDto projectDto);

    void updateProject(Long projectId, UpdateProjectDto updateProjectDto);

    void deleteProject(Long projectId);

    boolean addUsersToProject(String projectName, Long userId);
}
