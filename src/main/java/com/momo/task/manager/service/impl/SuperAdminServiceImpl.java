package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.exception.PictureDataException;
import com.momo.task.manager.exception.ProjectNotFoundException;
import com.momo.task.manager.exception.UserNotFoundException;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ImageLoader;
import com.momo.task.manager.utils.ResourceInformation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    SuperAdminRepository superAdminRepository;
    RoleRepository roleRepository;
    ProfilePictureRepository profilePictureRepository;
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    AccessRepository accessRepository;
    CommentRepository commentRepository;
    FileRepository fileRepository;
    ModelMapper mapper;
    ImageLoader imageLoader;
    PasswordEncoder passwordEncoder;

    @Autowired
    public SuperAdminServiceImpl(SuperAdminRepository superAdminRepository,
                                 RoleRepository roleRepository,
                                 ProfilePictureRepository profilePictureRepository,
                                 ProjectRepository projectRepository,
                                 TaskRepository taskRepository,
                                 AccessRepository accessRepository,
                                 CommentRepository commentRepository,
                                 FileRepository fileRepository,
                                 ModelMapper mapper,
                                 ImageLoader imageLoader,
                                 PasswordEncoder passwordEncoder) {

        this.superAdminRepository = superAdminRepository;
        this.roleRepository = roleRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.accessRepository = accessRepository;
        this.fileRepository=fileRepository;
        this.mapper = mapper;
        this.imageLoader = imageLoader;
        this.passwordEncoder = passwordEncoder;
        configureModelMapper();
    }

    private void configureModelMapper() {
        mapper.createTypeMap(User.class, UserResponseDto.class)
                .addMapping(src -> src.getPicture().getPictureData(), UserResponseDto::setPictureFile);
    }

    @Override
    public ResponseEntity<String> createUser(UserCreateDto userCreateDto) {
        User user = new User();
        createUserFromDto(user, userCreateDto);
        setRoleForUser(user, userCreateDto.getRoleId());
        setProfilePictureForUser(user, userCreateDto.getPictureFile(), new ProfilePicture());
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_CREATED_MESSAGE);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserDetails(String username) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);

        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.map(user, UserResponseDto.class));
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
    public ResponseEntity<List<UserResponseDto>> getUsers(Long role) {
        List<User> userList = superAdminRepository.findUsersByRole(role);
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
    public ResponseEntity<String> removeUser(String username) {

        //user delete , delete their access, comment
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        superAdminRepository.deleteByUserId(user.getUserId());
        profilePictureRepository.deleteByPictureId(user.getPicture().getProfilePictureId());
        accessRepository.deleteByUserId(user.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceInformation.USER_DELETED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateUserDetails(String username, UserDetailsDto userDetailsDto) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        user.setFirstName(userDetailsDto.getFirstName());
        user.setLastName(userDetailsDto.getLastName());
        user.setEmail(userDetailsDto.getEmail());
        setFlagForUserUpdate(user);
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_DETAILS_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateUserCredentials(String username, UserCredentialsDto userCredentialsDto) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        user.setUsername(userCredentialsDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));
        setFlagForUserUpdate(user);
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_CREDENTIALS_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateUserProfilePicture(String username, MultipartFile file) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        var userPPId = user.getPicture().getProfilePictureId();
        Optional<ProfilePicture> profilePicture = profilePictureRepository.findById(userPPId);
        ProfilePicture picture = profilePicture.orElseGet(ProfilePicture::new);
        updateProfilePictureForUser(user, file, picture);
        setFlagForUserUpdate(user);
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_PROFILE_PICTURE_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> createProject(ProjectDto projectDto) {

        Project project = mapper.map(projectDto, Project.class);
        Optional<User> optUser = superAdminRepository.findById(projectDto.getProjectLead());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optUser.get();
        project.setProjectLead(user);
        setFlagForProjectCreation(project);
        projectRepository.save(project);
        addUsersToProject(project.getProjectKey(), user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.PROJECT_CREATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateProject(String projectKey, UpdateProjectDto updateProjectDto) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        Project project = optionalProject.get();
        User prevUser = project.getProjectLead();
        updateProjectLead(prevUser, project, updateProjectDto);
        updateGeneralProjectDetails(project, updateProjectDto);
        project.setUpdatedFlg(true);
        project.setUpdatedDate(LocalDateTime.now());
        projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.PROJECT_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteProject(String projectKey) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        Long projectId = optionalProject.get().getProjectId();
        //check if active flag is zero or not before deletion in every delete operation
        projectRepository.deleteProjectById(projectId);
        List<Long> taskIdsInProject = taskRepository.getAllTaskIdFromProjectId(projectId);
        taskRepository.deleteByProjectId(projectId);
        for (Long taskId : taskIdsInProject) {
            fileRepository.deleteByTaskId(taskId);
            commentRepository.deleteByTaskId(taskId);
        }
        accessRepository.deleteProjectById(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.PROJECT_DELETED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> addUsersToProject(String projectKey, String username) {
        Optional<User> optionalUser = superAdminRepository.findByUsername(username);
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        User user = optionalUser.get();
        Project project = optionalProject.get();
        Access access = new Access();
        access.setUser(user);
        access.setProject(project);
        setFlagForAccessCreation(access);
        accessRepository.save(access);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_ADDED_TO_PROJECT_MESSAGE);
    }

    private void createUserFromDto(User user, UserCreateDto userCreateDto) {
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        setFlagForUserCreation(user);
    }

    private void setRoleForUser(User user, Long roleId) {
        Optional<Role> optRole = roleRepository.findById(roleId);
        optRole.ifPresent(user::setRole);
    }

    private void setProfilePictureForUser(User user, MultipartFile pictureFile, ProfilePicture profilePicture) {
        if (pictureFile != null) {
            try {
                profilePicture.setPictureData(pictureFile.getBytes());

            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        } else {
            byte[] defaultPicture = imageLoader.loadImage(ResourceInformation.DEFAULT_IMAGE_PATH);
            profilePicture.setPictureData(defaultPicture);
        }
        setFlagForProfilePictureCreation(profilePicture);
        user.setPicture(profilePictureRepository.save(profilePicture));
    }

    private void updateProfilePictureForUser(User user, MultipartFile pictureFile, ProfilePicture profilePicture) {
        if (pictureFile != null) {
            try {
                profilePicture.setPictureData(pictureFile.getBytes());
                setFlagForProfilePictureUpdate(profilePicture);

            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        }

        user.setPicture(profilePictureRepository.save(profilePicture));
    }

    private void updateProjectLead(User prevUser, Project project, UpdateProjectDto updateProjectDto) {
        Optional<User> optionalUser = superAdminRepository.findById(updateProjectDto.getProjectLead());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optionalUser.get();
        if (updateProjectDto.getProjectLead().equals(prevUser.getUserId())) {
            return;
        }
        project.setProjectLead(user);
        accessRepository.deleteByUserId(prevUser.getUserId());
        addUsersToProject(project.getProjectKey(), user.getUsername());
    }

    private void updateGeneralProjectDetails(Project project, UpdateProjectDto updateProjectDto) {
        if (!project.getProjectKey().isEmpty() && project.getProjectKey() != null) {
            project.setProjectKey(updateProjectDto.getKey());
        }
        if (!project.getProjectName().isEmpty() && project.getProjectName() != null) {
            project.setProjectName(updateProjectDto.getProjectName());
        }
    }

    private void setFlagForUserCreation(User user) {
        user.setActiveFlg(true);
        user.setUpdatedFlg(false);
        user.setStartDate(LocalDateTime.now());
        user.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    private void setFlagForProfilePictureCreation(ProfilePicture profilePicture) {
        profilePicture.setActiveFlg(true);
        profilePicture.setUpdatedFlg(false);
        profilePicture.setStartDate(LocalDateTime.now());
        profilePicture.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    private void setFlagForProjectCreation(Project project) {
        project.setActiveFlg(true);
        project.setUpdatedFlg(false);
        project.setStartDate(LocalDateTime.now());
        project.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    private void setFlagForAccessCreation(Access access) {
        access.setActiveFlg(true);
        access.setUpdatedFlg(false);
        access.setStartDate(LocalDateTime.now());
        access.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }


    private void setFlagForUserUpdate(User user) {
        user.setUpdatedFlg(true);
        user.setUpdatedDate(LocalDateTime.now());
    }

    private void setFlagForProfilePictureUpdate(ProfilePicture picture) {
        picture.setUpdatedFlg(true);
        picture.setUpdatedDate(LocalDateTime.now());
    }

}

