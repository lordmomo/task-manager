package com.momo.task.manager.controller;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ResourceInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(ResourceInformation.MAIN_SUPER_KEY)
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping(ResourceInformation.CREATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> createProject(@RequestBody ProjectDto projectDto) {
        return superAdminService.createProject(projectDto);
    }
    @PutMapping(ResourceInformation.UPDATE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> updateProject(@PathVariable("projectId") Long projectId,@RequestBody UpdateProjectDto updateProjectDto) {
        return superAdminService.updateProject(projectId,updateProjectDto);
    }

    @DeleteMapping(ResourceInformation.DELETE_PROJECTS_ENDPOINT)
    public ResponseEntity<String> deleteProject(@PathVariable("projectId") Long projectId){
        return superAdminService.deleteProject(projectId);
    }

    @PostMapping(ResourceInformation.CREATE_ADMINS_ENDPOINT)
    public ResponseEntity<String> createAdmin(@ModelAttribute UserCreateDto userCreateDto) throws IOException {

        return superAdminService.createAdmin(userCreateDto);
    }

    @PostMapping(ResourceInformation.CREATE_USERS_ENDPOINT)
    public ResponseEntity<String> createUser(@ModelAttribute UserCreateDto userCreateDto) throws IOException {

        return superAdminService.createUser(userCreateDto);
    }
    @GetMapping(ResourceInformation.GET_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<UserResponseDto> showUserDetails(@PathVariable("userId") Long userId){
        return superAdminService.getUserDetails(userId);
    }

    @GetMapping(ResourceInformation.GET_ALL_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showAllUsers(){
        return superAdminService.getAllUsers();
    }


    @GetMapping(ResourceInformation.GET_ONLY_USERS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showOnlyUsers(){
        return superAdminService.getOnlyUsers();
    }


    @GetMapping(ResourceInformation.GET_ONLY_ADMINS_ENDPOINT)
    public ResponseEntity<List<UserResponseDto>> showOnlyAdmins(){
        return superAdminService.getOnlyAdmins();
    }

    @DeleteMapping(ResourceInformation.DELETE_USERS_ENDPOINT)
    public ResponseEntity<String> removeUser(@PathVariable("userId")Long userId){
        return superAdminService.removeUser(userId);
    }

    @PutMapping(ResourceInformation.UPDATE_USERS_DETAILS_ENDPOINT)
    public ResponseEntity<String> updateUserDetails(@PathVariable("userId")Long userId,@RequestBody UserDetailsDto userDetailsDto){
        return superAdminService.updateUserDetails(userId,userDetailsDto);
    }

    @PutMapping(ResourceInformation.UPDATE_USERS_CREDENTIALS_ENDPOINT)
    public ResponseEntity<String> updateUserCredentials(@PathVariable("userId")Long userId, @RequestBody UserCredentialsDto userCredentialsDto){
        return superAdminService.updateUserCredentials(userId,userCredentialsDto);
    }

    @PutMapping(ResourceInformation.UPDATE_USERS_PROFILE_PICTURE_ENDPOINT)
    public ResponseEntity<String> updateUserProfilePicture(@PathVariable("userId")Long userId, @RequestParam MultipartFile newPicture) throws IOException {
       return superAdminService.updateUserProfilePicture(userId,newPicture);
    }

    @PostMapping(ResourceInformation.ADD_USERS_TO_PROJECT_ENDPOINT)
    public ResponseEntity<String> addUsersToProject(@PathVariable("projectName") String projectName, @PathVariable("userId") Long userId){
        return superAdminService.addUsersToProject(projectName,userId);
    }
}
