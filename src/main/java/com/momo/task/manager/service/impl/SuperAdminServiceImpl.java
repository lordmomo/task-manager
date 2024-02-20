package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.UserCredentialsDto;
import com.momo.task.manager.dto.UserDetailsDto;
import com.momo.task.manager.dto.UserDto;
import com.momo.task.manager.model.ProfilePicture;
import com.momo.task.manager.model.Role;
import com.momo.task.manager.model.User;

import com.momo.task.manager.repository.ProfilePictureRepository;
import com.momo.task.manager.repository.RoleRepository;
import com.momo.task.manager.repository.SuperAdminRepository;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProfilePictureRepository profilePictureRepository;

    ModelMapper mapper;

    // Constructor for initialization
    @Autowired
    public SuperAdminServiceImpl() {
        this.mapper = new ModelMapper();
        configureModelMapper();
    }

    private void configureModelMapper(){
        mapper.createTypeMap(User.class, UserDto.class)
                .addMapping(src -> src.getPicture().getPictureData(),UserDto::setPictureFile);
    }

    @Override
    public String createAdmin(String firstName, String lastName, String email,
                              String username, String password, Long roleId,
                              MultipartFile picture) throws IOException {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        Role optRole = roleRepository.findById(roleId)
                .orElseThrow( () -> {
                    return new RuntimeException("Role Not found");
                });

        user.setRole(optRole);

        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setPictureData(picture.getBytes());
        user.setPicture(profilePictureRepository.save(profilePicture));

        superAdminRepository.save(user);
        return "Success";
    }

    @Override
    public UserDto getUserDetails(Long userId) {
        Optional<User> optUser = superAdminRepository.findById(userId);

        if(optUser.isPresent()){
            User user = optUser.get();
//            return user;
            return mapper.map(user, UserDto.class);
        }
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = superAdminRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user : userList){
            UserDto userDto = mapper.map(user, UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public boolean removeUser(Long userId) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if(optUser.isPresent()){
            User user = optUser.get();
            superAdminRepository.delete(user);
            profilePictureRepository.delete(user.getPicture());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserDetails(Long userId, UserDetailsDto userDetailsDto) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if(optUser.isPresent()){

            User user = optUser.get();
            user.setFirstName(userDetailsDto.getFirstName());
            user.setLastName(userDetailsDto.getLastName());
            user.setEmail(userDetailsDto.getEmail());

            superAdminRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserCredentials(Long userId, UserCredentialsDto userCredentialsDto) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if(optUser.isPresent()){
            User user = optUser.get();
            user.setUsername(userCredentialsDto.getUsername());
            user.setPassword(userCredentialsDto.getPassword());
            superAdminRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserProfilePicture(Long userId, MultipartFile file) throws IOException {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if(optUser.isPresent()){
            User user = optUser.get();
            var userPPId = user.getPicture().getProfilePictureId();
            Optional<ProfilePicture> profilePicture = profilePictureRepository.findById(userPPId);
            if(profilePicture.isPresent()){
                ProfilePicture picture = profilePicture.get();
                picture.setPictureData(file.getBytes());
                user.setPicture(profilePictureRepository.save(picture));
                superAdminRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

}

