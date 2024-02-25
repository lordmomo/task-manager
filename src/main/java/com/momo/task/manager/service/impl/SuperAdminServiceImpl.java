package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ImageLoader;
import com.momo.task.manager.utils.ResourceInformation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AccessRepository accessRepository;
    ModelMapper mapper;

    @Autowired
    ImageLoader imageLoader;

    // Constructor for initialization
    @Autowired
    public SuperAdminServiceImpl() {
        this.mapper = new ModelMapper();
        configureModelMapper();
    }

    private void configureModelMapper() {
        mapper.createTypeMap(User.class, UserResponseDto.class)
                .addMapping(src -> src.getPicture().getPictureData(), UserResponseDto::setPictureFile);
    }

    @Override
    public ResponseEntity<String> createAdmin(UserCreateDto userCreateDto) throws IOException {

        User user = new User();
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());

        Optional<Role> optRole = roleRepository.findById(2L);

        optRole.ifPresent(user::setRole);

        ProfilePicture profilePicture = new ProfilePicture();
        if (userCreateDto.getPictureFile() != null) {
            profilePicture.setPictureData(userCreateDto.getPictureFile().getBytes());
        } else {
            byte[] defaultPicture = imageLoader.loadImage(ResourceInformation.defaultImagePath);
            profilePicture.setPictureData(defaultPicture);
        }
        user.setPicture(profilePictureRepository.save(profilePicture));

        superAdminRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceInformation.adminCreatedMessage);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserDetails(Long userId) {
        Optional<User> optUser = superAdminRepository.findById(userId);

        if (optUser.isPresent()) {
            User user = optUser.get();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(mapper.map(user, UserResponseDto.class));
        }
        return null;
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> userList = superAdminRepository.findAll();
        List<UserResponseDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserResponseDto userDto = mapper.map(user, UserResponseDto.class);
            userDtoList.add(userDto);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDtoList);
    }

    @Override
    public ResponseEntity<String> removeUser(Long userId) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            superAdminRepository.delete(user);
            profilePictureRepository.delete(user.getPicture());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResourceInformation.userDeletedMessage);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResourceInformation.userNotFoundMessage);

    }

    @Override
    public ResponseEntity<String> updateUserDetails(Long userId, UserDetailsDto userDetailsDto) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if (optUser.isPresent()) {

            User user = optUser.get();
            user.setFirstName(userDetailsDto.getFirstName());
            user.setLastName(userDetailsDto.getLastName());
            user.setEmail(userDetailsDto.getEmail());

            superAdminRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.userDetailsUpdatedMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResourceInformation.userNotFoundMessage);

    }

    @Override
    public ResponseEntity<String> updateUserCredentials(Long userId, UserCredentialsDto userCredentialsDto) {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setUsername(userCredentialsDto.getUsername());
            user.setPassword(userCredentialsDto.getPassword());
            superAdminRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.userCredentialsUpdatedMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResourceInformation.userNotFoundMessage);

    }

    @Override
    public ResponseEntity<String> updateUserProfilePicture(Long userId, MultipartFile file) throws IOException {
        Optional<User> optUser = superAdminRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            var userPPId = user.getPicture().getProfilePictureId();
            Optional<ProfilePicture> profilePicture = profilePictureRepository.findById(userPPId);
            ProfilePicture picture = profilePicture.orElseGet(ProfilePicture::new);
            picture.setPictureData(file.getBytes());
            user.setPicture(profilePictureRepository.save(picture));
            superAdminRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.userProfilePoctureUpdatedMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResourceInformation.userNotFoundMessage);

    }

    @Override
    public ResponseEntity<String> createUser(UserCreateDto userCreateDto) throws IOException {
        User user = new User();
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());

        Optional<Role> optRole = roleRepository.findById(3L);
        optRole.ifPresent(user::setRole);

        ProfilePicture profilePicture = new ProfilePicture();
        if (userCreateDto.getPictureFile() != null) {
            profilePicture.setPictureData(userCreateDto.getPictureFile().getBytes());
        } else {
            byte[] defaultPicture = imageLoader.loadImage(ResourceInformation.defaultImagePath);
            profilePicture.setPictureData(defaultPicture);
        }
        user.setPicture(profilePictureRepository.save(profilePicture));

        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.userCreatedMessage);
    }

    @Override
    public ResponseEntity<String> createProject(ProjectDto projectDto) {

        Project project = mapper.map(projectDto, Project.class);
        Optional<User> optUser = superAdminRepository.findById(projectDto.getProjectLead());
        if (optUser.isPresent()) {
            User user = optUser.get();
            project.setProjectLead(user);
            projectRepository.save(project);
            addUsersToProject(project.getProjectName(), user.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.projectCreatedMessage);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.userNotFoundMessage);
        }
    }

    @Override
    public ResponseEntity<String> updateProject(Long projectId, UpdateProjectDto updateProjectDto) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            User prevUser = project.getProjectLead();
            if (!project.getKey().isEmpty() && project.getKey() != null) {
                project.setKey(updateProjectDto.getKey());
            }
            if (!project.getProjectName().isEmpty() && project.getProjectName() != null) {
                project.setProjectName(updateProjectDto.getProjectName());
            }
            Optional<User> optionalUser = superAdminRepository.findById(updateProjectDto.getProjectLead());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                project.setProjectLead(user);
                accessRepository.deleteByUserId(prevUser.getUserId());
                addUsersToProject(project.getProjectName(), user.getUserId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.userNotFoundMessage);
            }
            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.projectUpdatedMessage);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectNotFoundMessage);
        }
    }

    @Override
    public ResponseEntity<String> deleteProject(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            projectRepository.delete(project);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.projectDeletedMessage);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectNotFoundMessage);

    }

    @Override
    public ResponseEntity<String> addUsersToProject(String projectName, Long userId) {
        Optional<User> optionalUser = superAdminRepository.findById(userId);
        Optional<Project> optionalProject = projectRepository.findByProjectName(projectName);
        if (optionalUser.isPresent() && optionalProject.isPresent()) {
            User user = optionalUser.get();
            Project project = optionalProject.get();
            Access access = new Access();
            access.setUser(user);
            access.setProject(project);
            accessRepository.save(access);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.userAddedToProjectMessage);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectOrUserNotFoundMessage);
    }

}

