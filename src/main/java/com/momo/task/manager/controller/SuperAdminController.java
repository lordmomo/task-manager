package com.momo.task.manager.controller;

import com.momo.task.manager.request.*;
import com.momo.task.manager.response.UserResponseDto;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ConstantEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(ConstantEndpoints.MAIN_SUPER_KEY)
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.CREATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> createProjects(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        return superAdminService.createProject(projectCreateRequestDto);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.UPDATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> updateProjects(@PathVariable("projectKey") String projectKey, @RequestBody UpdateProjectRequestDto updateProjectRequestDto) {
        return superAdminService.updateProject(projectKey, updateProjectRequestDto);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.DELETE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> deleteProjects(@PathVariable("projectKey") String projectKey) {
        return superAdminService.deleteProject(projectKey);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.CREATE_USERS_ENDPOINT)
    public ResponseEntity<String> createUsers(@ModelAttribute UserCreateRequestDto userCreateRequestDto) {

        return superAdminService.createUser(userCreateRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ConstantEndpoints.GET_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<UserResponseDto> showUserDetails(@PathVariable("username") String username) {
        return superAdminService.getUserDetails(username);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping(ConstantEndpoints.GET_ALL_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showAllUsers() {
        return superAdminService.getAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping(ConstantEndpoints.GET_ONLY_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showUsers(@RequestParam Long roleId) {
        return superAdminService.getUsers(roleId);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.DELETE_USERS_ENDPOINT)
    public ResponseEntity<String> removeUsers(@PathVariable("username") String username) {
        return superAdminService.removeUser(username);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #username == authentication.name) ")
    @PostMapping(ConstantEndpoints.UPDATE_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<String> updateUserDetails(@PathVariable("username") String username, @RequestBody UserDetailsRequestDto userDetailsRequestDto) {
        return superAdminService.updateUserDetails(username, userDetailsRequestDto);
    }

    @PreAuthorize("#username == authentication.name")
    @PostMapping(ConstantEndpoints.UPDATE_USERS_CREDENTIALS_ENDPOINT)
    //add username instead of userId
    public ResponseEntity<String> updateUserCredentials(@PathVariable("username") String username, @RequestBody UserCredentialsRequestDto userCredentialsRequestDto) {
        return superAdminService.updateUserCredentials(username, userCredentialsRequestDto);
    }

    @PreAuthorize("#username == authentication.name")
    @PostMapping(ConstantEndpoints.UPDATE_USERS_PROFILE_PICTURE_ENDPOINT)
    public ResponseEntity<String> updateUserProfilePictures(@PathVariable("username") String username, @RequestParam MultipartFile newPicture) throws IOException {
        return superAdminService.updateUserProfilePicture(username, newPicture);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(ConstantEndpoints.ADD_USERS_TO_PROJECT_ENDPOINT)
    public ResponseEntity<String> addUsersToProjects(@PathVariable("projectKey") String projectKey, @PathVariable("username") String username) {
        return superAdminService.addUsersToProject(projectKey, username);
    }
}
