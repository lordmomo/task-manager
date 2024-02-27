package com.momo.task.manager.controller;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(ResourceEndpoints.MAIN_SUPER_KEY)
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping(ResourceEndpoints.CREATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> createProjects(@RequestBody ProjectDto projectDto) {
        return superAdminService.createProject(projectDto);
    }

    @PostMapping(ResourceEndpoints.UPDATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> updateProjects(@PathVariable("projectId") Long projectId, @RequestBody UpdateProjectDto updateProjectDto) {
        return superAdminService.updateProject(projectId, updateProjectDto);
    }

    @PostMapping(ResourceEndpoints.DELETE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> deleteProjects(@PathVariable("projectId") Long projectId) {
        return superAdminService.deleteProject(projectId);
    }

    @PostMapping(ResourceEndpoints.CREATE_USERS_ENDPOINT)
    public ResponseEntity<String> createUsers(@ModelAttribute UserCreateDto userCreateDto) {

        return superAdminService.createUser(userCreateDto);
    }

    @GetMapping(ResourceEndpoints.GET_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<UserResponseDto> showUserDetails(@PathVariable("userId") Long userId) {
        return superAdminService.getUserDetails(userId);
    }

    @GetMapping(ResourceEndpoints.GET_ALL_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showAllUsers() {
        return superAdminService.getAllUsers();
    }


    @GetMapping(ResourceEndpoints.GET_ONLY_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showUsers(@RequestParam Long roleId) {
        return superAdminService.getUsers(roleId);
    }


    @PostMapping(ResourceEndpoints.DELETE_USERS_ENDPOINT)
    public ResponseEntity<String> removeUsers(@PathVariable("userId") Long userId) {
        return superAdminService.removeUser(userId);
    }

    @PostMapping(ResourceEndpoints.UPDATE_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<String> updateUserDetails(@PathVariable("userId") Long userId, @RequestBody UserDetailsDto userDetailsDto) {
        return superAdminService.updateUserDetails(userId, userDetailsDto);
    }

    @PostMapping(ResourceEndpoints.UPDATE_USERS_CREDENTIALS_ENDPOINT)
    public ResponseEntity<String> updateUserCredentials(@PathVariable("userId") Long userId, @RequestBody UserCredentialsDto userCredentialsDto) {
        return superAdminService.updateUserCredentials(userId, userCredentialsDto);
    }

    @PostMapping(ResourceEndpoints.UPDATE_USERS_PROFILE_PICTURE_ENDPOINT)
    public ResponseEntity<String> updateUserProfilePictures(@PathVariable("userId") Long userId, @RequestParam MultipartFile newPicture) throws IOException {
        return superAdminService.updateUserProfilePicture(userId, newPicture);
    }

    @PostMapping(ResourceEndpoints.ADD_USERS_TO_PROJECT_ENDPOINT)
    public ResponseEntity<String> addUsersToProjects(@PathVariable("projectName") String projectName, @PathVariable("userId") Long userId) {
        return superAdminService.addUsersToProject(projectName, userId);
    }
}
