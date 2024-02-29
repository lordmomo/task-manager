package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.*;
import com.momo.task.manager.exception.DataHasBeenDeletedException;
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
        this.fileRepository = fileRepository;
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
        this.createUserFromDto(user, userCreateDto);
        this.setRoleForUser(user, userCreateDto.getRoleId());
        this.setProfilePictureForUser(user, userCreateDto.getPictureFile(), new ProfilePicture());
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_CREATED_MESSAGE);
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserDetails(String username) {
        User user = validateIfUserExistsAndIsActive(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.map(user, UserResponseDto.class));
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> userList = superAdminRepository.findAllActiveUsers();
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
        User user = validateIfUserExistsAndIsActive(username);
        removeAllUserRelatedData(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResourceInformation.USER_DELETED_MESSAGE);
    }

    private void removeAllUserRelatedData(User user) {
        superAdminRepository.deleteByUserId(user.getUserId());
        profilePictureRepository.deleteByPictureId(user.getPicture().getProfilePictureId());
        accessRepository.deleteByUserId(user.getUserId());
    }

    @Override
    public ResponseEntity<String> updateUserDetails(String username, UserDetailsDto userDetailsDto) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserDetails(user,userDetailsDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_DETAILS_UPDATED_MESSAGE);
    }

    private void updateAllUserDetails(User user,UserDetailsDto userDetailsDto) {
        user.setFirstName(userDetailsDto.getFirstName());
        user.setLastName(userDetailsDto.getLastName());
        user.setEmail(userDetailsDto.getEmail());
        this.setFlagForUserUpdate(user);
        superAdminRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUserCredentials(String username, UserCredentialsDto userCredentialsDto) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserCredentials(user,userCredentialsDto);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_CREDENTIALS_UPDATED_MESSAGE);
    }
    private void updateAllUserCredentials(User user, UserCredentialsDto userCredentialsDto) {
        user.setUsername(userCredentialsDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));
        this.setFlagForUserUpdate(user);
        superAdminRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUserProfilePicture(String username, MultipartFile file) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserProfilePicture(user,file);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_PROFILE_PICTURE_UPDATED_MESSAGE);
    }

    private void updateAllUserProfilePicture(User user, MultipartFile file) {
        var userPPId = user.getPicture().getProfilePictureId();
        Optional<ProfilePicture> profilePicture = profilePictureRepository.findById(userPPId);
        ProfilePicture picture = profilePicture.orElseGet(ProfilePicture::new);
        this.updateProfilePictureForUser(user, file, picture);
        this.setFlagForUserUpdate(user);
        superAdminRepository.save(user);
    }

    @Override
    public ResponseEntity<String> createProject(ProjectDto projectDto) {

        Project project = mapper.map(projectDto, Project.class);
        Optional<User> optUser = superAdminRepository.findById(projectDto.getProjectLead());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        if(!optUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        User user = optUser.get();
        project.setProjectLead(user);
        this.setFlagForProjectCreation(project);
        projectRepository.save(project);
        this.addUsersToProject(project.getProjectKey(), user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.PROJECT_CREATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateProject(String projectKey, UpdateProjectDto updateProjectDto) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        if(!optionalProject.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        Project project = optionalProject.get();
        User prevUser = project.getProjectLead();
        if(!prevUser.isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        this.updateProjectLead(prevUser, project, updateProjectDto);
        this.updateGeneralProjectDetails(project, updateProjectDto);
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
        if(!optionalProject.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
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

        if(!optionalProject.get().isActiveFlg() ||
            !optionalUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        User user = optionalUser.get();
        Project project = optionalProject.get();
        Access access = createAccess(user, project);
        accessRepository.save(access);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.USER_ADDED_TO_PROJECT_MESSAGE);
    }

    private Access createAccess(User user, Project project) {
        Access access = new Access();
        access.setUser(user);
        access.setProject(project);
        this.setFlagForAccessCreation(access);
        return access;
    }

    private void createUserFromDto(User user, UserCreateDto userCreateDto) {
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        this.setFlagForUserCreation(user);
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
        this.setFlagForProfilePictureCreation(profilePicture);
        user.setPicture(profilePictureRepository.save(profilePicture));
    }

    private void updateProfilePictureForUser(User user, MultipartFile pictureFile, ProfilePicture profilePicture) {
        if (pictureFile != null) {
            try {
                profilePicture.setPictureData(pictureFile.getBytes());
                this.setFlagForProfilePictureUpdate(profilePicture);

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
        this.addUsersToProject(project.getProjectKey(), user.getUsername());
    }

    private void updateGeneralProjectDetails(Project project, UpdateProjectDto updateProjectDto) {
        if (!project.getProjectKey().isEmpty() && project.getProjectKey() != null) {
            project.setProjectKey(updateProjectDto.getKey());
        }
        if (!project.getProjectName().isEmpty() && project.getProjectName() != null) {
            project.setProjectName(updateProjectDto.getProjectName());
        }
    }

    private User validateIfUserExistsAndIsActive(String username) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        if(!optUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        return optUser.get();
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

