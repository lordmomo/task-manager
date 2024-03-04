package com.momo.task.manager.service.impl;

import com.momo.task.manager.exception.DataHasBeenDeletedException;
import com.momo.task.manager.exception.PictureDataException;
import com.momo.task.manager.exception.ProjectNotFoundException;
import com.momo.task.manager.exception.UserNotFoundException;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.request.*;
import com.momo.task.manager.response.UserResponseDto;
import com.momo.task.manager.service.interfaces.SuperAdminService;
import com.momo.task.manager.utils.ImageLoader;
import com.momo.task.manager.utils.ConstantInformation;
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
    CommentDbRepository commentRepository;
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
                                 CommentDbRepository commentRepository,
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
    public ResponseEntity<String> createUser(UserCreateRequestDto userCreateRequestDto) {
        User user = new User();
        this.createUserFromDto(user, userCreateRequestDto);
        this.setRoleForUser(user, userCreateRequestDto.getRoleId());
        this.setProfilePictureForUser(user, userCreateRequestDto.getPictureFile(), new ProfilePicture());
        superAdminRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.USER_CREATED_MESSAGE);
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
                .body(ConstantInformation.USER_DELETED_MESSAGE);
    }

    private void removeAllUserRelatedData(User user) {
        superAdminRepository.deleteByUserId(user.getUserId());
        profilePictureRepository.deleteByPictureId(user.getPicture().getProfilePictureId());
        accessRepository.deleteByUserId(user.getUserId());
    }

    @Override
    public ResponseEntity<String> updateUserDetails(String username, UserDetailsRequestDto userDetailsRequestDto) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserDetails(user, userDetailsRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.USER_DETAILS_UPDATED_MESSAGE);
    }

    private void updateAllUserDetails(User user, UserDetailsRequestDto userDetailsRequestDto) {
        user.setFirstName(userDetailsRequestDto.getFirstName());
        user.setLastName(userDetailsRequestDto.getLastName());
        user.setEmail(userDetailsRequestDto.getEmail());
        this.setFlagForUserUpdate(user);
        superAdminRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUserCredentials(String username, UserCredentialsRequestDto userCredentialsRequestDto) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserCredentials(user, userCredentialsRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.USER_CREDENTIALS_UPDATED_MESSAGE);
    }
    private void updateAllUserCredentials(User user, UserCredentialsRequestDto userCredentialsRequestDto) {
        user.setUsername(userCredentialsRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCredentialsRequestDto.getPassword()));
        this.setFlagForUserUpdate(user);
        superAdminRepository.save(user);
    }

    @Override
    public ResponseEntity<String> updateUserProfilePicture(String username, MultipartFile file) {
        User user = validateIfUserExistsAndIsActive(username);
        this.updateAllUserProfilePicture(user,file);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.USER_PROFILE_PICTURE_UPDATED_MESSAGE);
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
    public ResponseEntity<String> createProject(ProjectCreateRequestDto projectCreateRequestDto) {

        Project project = mapper.map(projectCreateRequestDto, Project.class);
        Optional<User> optUser = superAdminRepository.findById(projectCreateRequestDto.getProjectLead());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        if(!optUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
        }
        User user = optUser.get();
        project.setProjectLead(user);
        this.setFlagForProjectCreation(project);
        projectRepository.save(project);
        this.addUsersToProject(project.getProjectKey(), user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.PROJECT_CREATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> updateProject(String projectKey, UpdateProjectRequestDto updateProjectRequestDto) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ConstantInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        if(!optionalProject.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
        }
        Project project = optionalProject.get();
        User prevUser = project.getProjectLead();
        if(!prevUser.isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
        }
        this.updateProjectLead(prevUser, project, updateProjectRequestDto);
        this.updateGeneralProjectDetails(project, updateProjectRequestDto);
        project.setUpdatedFlg(true);
        project.setUpdatedDate(LocalDateTime.now());
        projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.PROJECT_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteProject(String projectKey) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ConstantInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        if(!optionalProject.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
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
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.PROJECT_DELETED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> addUsersToProject(String projectKey, String username) {
        Optional<User> optionalUser = superAdminRepository.findByUsername(username);
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ConstantInformation.PROJECT_NOT_FOUND_MESSAGE);
        }

        if(!optionalProject.get().isActiveFlg() ||
            !optionalUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
        }
        User user = optionalUser.get();
        Project project = optionalProject.get();
        Access access = createAccess(user, project);
        accessRepository.save(access);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.USER_ADDED_TO_PROJECT_MESSAGE);
    }

    private Access createAccess(User user, Project project) {
        Access access = new Access();
        access.setUser(user);
        access.setProject(project);
        this.setFlagForAccessCreation(access);
        return access;
    }

    private void createUserFromDto(User user, UserCreateRequestDto userCreateRequestDto) {
        user.setFirstName(userCreateRequestDto.getFirstName());
        user.setLastName(userCreateRequestDto.getLastName());
        user.setEmail(userCreateRequestDto.getEmail());
        user.setUsername(userCreateRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
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
                throw new PictureDataException(ConstantInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        } else {
            byte[] defaultPicture = imageLoader.loadImage(ConstantInformation.DEFAULT_IMAGE_PATH);
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
                throw new PictureDataException(ConstantInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        }
        user.setPicture(profilePictureRepository.save(profilePicture));
    }

    private void updateProjectLead(User prevUser, Project project, UpdateProjectRequestDto updateProjectRequestDto) {
        Optional<User> optionalUser = superAdminRepository.findById(updateProjectRequestDto.getProjectLead());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        User user = optionalUser.get();
        if (updateProjectRequestDto.getProjectLead().equals(prevUser.getUserId())) {
            return;
        }
        project.setProjectLead(user);
        accessRepository.deleteByUserId(prevUser.getUserId());
        this.addUsersToProject(project.getProjectKey(), user.getUsername());
    }

    private void updateGeneralProjectDetails(Project project, UpdateProjectRequestDto updateProjectRequestDto) {
        if (!project.getProjectKey().isEmpty() && project.getProjectKey() != null) {
            project.setProjectKey(updateProjectRequestDto.getKey());
        }
        if (!project.getProjectName().isEmpty() && project.getProjectName() != null) {
            project.setProjectName(updateProjectRequestDto.getProjectName());
        }
    }

    private User validateIfUserExistsAndIsActive(String username) {
        Optional<User> optUser = superAdminRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        if(!optUser.get().isActiveFlg()){
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
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

