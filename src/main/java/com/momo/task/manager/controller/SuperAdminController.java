package com.momo.task.manager.controller;

import com.momo.task.manager.dto.UserCredentialsDto;
import com.momo.task.manager.dto.UserDetailsDto;
import com.momo.task.manager.dto.UserDto;
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

    //@ModelAttribute User user
    @PostMapping("/create-admin")
    public String createAdmin(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String email,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam Long role,
                              @RequestParam MultipartFile picture
                              ) throws IOException {

        return superAdminService.createAdmin(firstName,lastName,email,username,password,role,picture);
    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String email,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam Long role,
                              @RequestParam MultipartFile picture
    ) throws IOException {

        return superAdminService.createUser(firstName,lastName,email,username,password,role,picture);
    }
    @GetMapping("/get-user-details/{userId}")
    public ResponseEntity<?> showUserDetails(@PathVariable("userId") Long userId){
        UserDto userDto = superAdminService.getUserDetails(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> showAllUsers(){
        List<UserDto> userDtoList = superAdminService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }
    @DeleteMapping("/remove-user/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable("userId")Long userId){
        boolean check = superAdminService.removeUser(userId);
        if(!check){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User not found");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User removed!!!");
    }

    @PutMapping("/update-user-details/{userId}")
    public ResponseEntity<String> updateUserDetails(@PathVariable("userId")Long userId,@RequestBody UserDetailsDto userDetailsDto){
        boolean check = superAdminService.updateUserDetails(userId,userDetailsDto);
        if(!check){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User details updated!!!");
    }

    @PutMapping("/update-user-credentials/{userId}")
    public ResponseEntity<String> updateUserCredentials(@PathVariable("userId")Long userId, @RequestBody UserCredentialsDto userCredentialsDto){
        boolean check = superAdminService.updateUserCredentials(userId,userCredentialsDto);
        if(!check){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User credentials updated!!!");
    }

    @PutMapping("/update-user-profile-picture/{userId}")
    public ResponseEntity<String> updateUserProfilePicture(@PathVariable("userId")Long userId, @RequestParam MultipartFile newPicture) throws IOException {
        boolean check = superAdminService.updateUserProfilePicture(userId,newPicture);
        if(!check){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User profile picture updated!!!");
    }
}
