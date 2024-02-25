package com.momo.task.manager.controller;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/super")
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping("/create-projects")
    public ResponseEntity<String> createProject(@RequestBody ProjectDto projectDto) {
        return superAdminService.createProject(projectDto);
    }
    @PutMapping("/update-project/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable("projectId") Long projectId,@RequestBody UpdateProjectDto updateProjectDto) {
        return superAdminService.updateProject(projectId,updateProjectDto);
    }

    @DeleteMapping("/delete-project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectId") Long projectId){
        return superAdminService.deleteProject(projectId);
    }

    //@ModelAttribute User user
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@ModelAttribute UserCreateDto userCreateDto) throws IOException {

        return superAdminService.createAdmin(userCreateDto);
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@ModelAttribute UserCreateDto userCreateDto) throws IOException {

        return superAdminService.createUser(userCreateDto);
    }
    @GetMapping("/get-user-details/{userId}")
    public ResponseEntity<UserResponseDto> showUserDetails(@PathVariable("userId") Long userId){
        return superAdminService.getUserDetails(userId);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDto>> showAllUsers(){
        return superAdminService.getAllUsers();
    }


    @GetMapping("/get-only-users")
    public ResponseEntity<List<UserResponseDto>> showOnlyUsers(){
        return superAdminService.getOnlyUsers();
    }


    @GetMapping("/get-only-admins")
    public ResponseEntity<List<UserResponseDto>> showOnlyAdmins(){
        return superAdminService.getOnlyAdmins();
    }

    @DeleteMapping("/remove-user/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable("userId")Long userId){
        return superAdminService.removeUser(userId);
    }

    @PutMapping("/update-user-details/{userId}")
    public ResponseEntity<String> updateUserDetails(@PathVariable("userId")Long userId,@RequestBody UserDetailsDto userDetailsDto){
        return superAdminService.updateUserDetails(userId,userDetailsDto);
    }

    @PutMapping("/update-user-credentials/{userId}")
    public ResponseEntity<String> updateUserCredentials(@PathVariable("userId")Long userId, @RequestBody UserCredentialsDto userCredentialsDto){
        return superAdminService.updateUserCredentials(userId,userCredentialsDto);
    }

    @PutMapping("/update-user-profile-picture/{userId}")
    public ResponseEntity<String> updateUserProfilePicture(@PathVariable("userId")Long userId, @RequestParam MultipartFile newPicture) throws IOException {
       return superAdminService.updateUserProfilePicture(userId,newPicture);
    }

    @PostMapping("/{projectName}/add-users/{userId}")
    public ResponseEntity<String> addUsersToProject(@PathVariable("projectName") String projectName, @PathVariable("userId") Long userId){
        return superAdminService.addUsersToProject(projectName,userId);
    }
}
