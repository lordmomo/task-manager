package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.exception.*;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ResourceInformation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    TaskRepository taskRepository;
    CommentRepository commentRepository;
    FileRepository fileRepository;
    TaskStatusRepository taskStatusRepository;
    SuperAdminRepository superAdminRepository;
    ProjectRepository projectRepository;
    StagesRepository stagesRepository;
    CheckUtils checkUtils;
    ModelMapper mapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           CommentRepository commentRepository,
                           FileRepository fileRepository,
                           TaskStatusRepository taskStatusRepository,
                           SuperAdminRepository superAdminRepository,
                           ProjectRepository projectRepository,
                           StagesRepository stagesRepository,
                           CheckUtils checkUtils,
                           ModelMapper mapper) {

        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.fileRepository = fileRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.superAdminRepository = superAdminRepository;
        this.projectRepository = projectRepository;
        this.stagesRepository = stagesRepository;
        this.checkUtils = checkUtils;
        this.mapper = mapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        // Use strict matching strategy to avoid conflicts
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Explicitly define mappings to avoid conflicts
        mapper.createTypeMap(TaskDto.class, Task.class)
                .addMapping(TaskDto::getLabel, Task::setLabel)
                .addMapping(TaskDto::getReporterId, Task::setReporterId)
                .addMapping(TaskDto::getAssigneeId, Task::setAssigneeId)
                .addMapping(TaskDto::getStageId, Task::setStageId);
    }

    @Override
    public ResponseEntity<String> createTask(TaskDto taskDto) {
        if (hasUserAccessToProject(taskDto.getAssigneeId(), taskDto.getProjectKey()) &&
                hasUserAccessToProject(taskDto.getReporterId(), taskDto.getProjectKey())
        ) {
            Task task = createTaskFromDto(taskDto);
            taskRepository.save(task);
            saveTaskFile(task, taskDto.getFile());
            log.info(ResourceInformation.TASK_CREATED_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_CREATED_MESSAGE);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> deleteTask(Long taskId) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isEmpty()) {
                throw new TaskNotFoundException(ResourceInformation.TASK_NOT_FOUND_MESSAGE);
            }
            fileRepository.deleteByTaskId(taskId);
            commentRepository.deleteByTaskId(taskId);
            taskRepository.deleteByTaskId(taskId);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_DELETED_MESSAGE);
        } catch (TaskNotFoundException e) {
            throw new TaskNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            throw new TaskNotFoundException(ResourceInformation.TASK_NOT_FOUND_MESSAGE);
        }
        //try catch if project in taskDto is null
        Task task = optTask.get();
        updateTaskFile(task, taskDto.getFile());
        updateTaskFields(task, taskDto);
        task.setUpdatedFlg(true);
        updateTaskDateFields(task);
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_UPDATED_MESSAGE);
    }

    @Override
    public ResponseEntity<List<Task>> getAllTask(String projectKey) {
        Optional<Project> project = projectRepository.findByProjectKey(projectKey);
        if (project.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        List<Task> taskList = taskRepository.findByProdId(project.get().getProjectId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskList);
    }


    private void updateTaskFile(Task task, MultipartFile mFile) {


        Optional<File> optFile = Optional.ofNullable(fileRepository.findFileByTaskId(task.getTaskId()));
        if(optFile.isPresent()){
            File file = optFile.get();
            try {
                if (!Arrays.equals(file.getFileData(), mFile.getBytes())) {
                    if (!mFile.isEmpty()) {
                        file.setFileData(mFile.getBytes());
                    } else {
                        file.setFileData(null);
                    }
                    setFlagForFileUpdate(file);
                }
                return;
            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        }
        File file = new File();
        file.setTask(task);
        setFlagForFileCreation(file);
        if (mFile != null && !mFile.isEmpty()) {
            try {
                file.setFileData(mFile.getBytes());
            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        }
        fileRepository.save(file);
        log.info(ResourceInformation.TASK_FILE_ADDED_MESSAGE);
    }


    private void updateTaskDateFields(Task task) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        task.setUpdatedDate(currentDateTime);
        task.setUpdatedStageDate(currentDateTime);
        task.setUpdatedStatusDate(currentDateTime);
    }

    private void updateTaskFields(Task task, TaskDto taskDto) {

        updateGeneralTaskInformation(task, taskDto);
        updateTaskStatus(task, taskDto);
        updateTaskStage(task, taskDto);
        updateAssociatedReporterUsers(taskDto.getReporterId(), task);
        updateAssociatedAssigneeUsers(taskDto.getAssigneeId(), task);
    }

    private void updateAssociatedReporterUsers(Long userId, Task task) {
        if (userId != null) {
            Optional<User> user = superAdminRepository.findById(userId);
            if (user.isEmpty()) {
                throw new UserNotFoundException(ResourceInformation.REPORTER_NOT_FOUND_MESSAGE);
            }
            task.setReporterId(user.get());
        }
    }

    private void updateAssociatedAssigneeUsers(Long userId, Task task) {
        if (userId != null) {
            Optional<User> user = superAdminRepository.findById(userId);
            if (user.isEmpty()) {
                throw new UserNotFoundException(ResourceInformation.ASSIGNEE_NOT_FOUND_MESSAGE);
            }
            task.setAssigneeId(user.get());
        }
    }

    private void updateTaskStage(Task task, TaskDto taskDto) {
        if (task.getStageId() != null && taskDto.getStageId()!=null) {
            Optional<Stages> stages = stagesRepository.findById(taskDto.getStageId());
            if (stages.isEmpty()) {
                throw new TaskStageNotFoundException(ResourceInformation.STAGE_NOT_FOUND_MESSAGE);
            }
            task.setStageId(stages.get());
        }
    }

    private void updateTaskStatus(Task task, TaskDto taskDto) {
        if (task.getStatus().getStatusId() != null && taskDto.getStatus()!=null) {
            Optional<TaskStatus> status = taskStatusRepository.findById(taskDto.getStatus());
            if (status.isEmpty()) {
                throw new TaskStatusNotFoundException(ResourceInformation.STATUS_NOT_FOUND_MESSAGE);
            }
            task.setStatus(status.get());
        }
    }

    private void updateGeneralTaskInformation(Task task, TaskDto taskDto) {
        if (task.getTaskName() != null && !"".equalsIgnoreCase(task.getTaskName()) && taskDto.getTaskName()!=null) {
            task.setTaskName(taskDto.getTaskName());
        }

        if (task.getDescription() != null && !"".equalsIgnoreCase(task.getDescription()) && taskDto.getDescription()!=null
        ) {
            task.setDescription(taskDto.getDescription());
        }
        if (task.getLabel() != null && !"".equalsIgnoreCase(task.getLabel()) && taskDto.getLabel()!=null) {
            task.setLabel(taskDto.getLabel());
        }
        if (task.getStartDate() != null && taskDto.getStartDate()!=null) {
            task.setStartDate(taskDto.getStartDate());
        }
        if (task.getEndDate() != null && taskDto.getEndDate()!=null) {
            task.setEndDate(taskDto.getEndDate());
        }
    }

    private Task createTaskFromDto(TaskDto taskDto) {
        Task task = new Task();
        task.setActiveFlg(true);
        task.setTaskName(taskDto.getTaskName());
        task.setDescription(taskDto.getDescription());
        task.setType(taskDto.getType());
        task.setLabel(taskDto.getLabel());
        task.setStatus(checkUtils.getStatusFromId(taskDto.getStatus()));
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());
        task.setProject(checkUtils.getProjectFromKey(taskDto.getProjectKey()));
        task.setAssigneeId(checkUtils.getUserFromId(taskDto.getAssigneeId()));
        task.setReporterId(checkUtils.getUserFromId(taskDto.getReporterId()));
        task.setStageId(checkUtils.getStageFromId(taskDto.getStageId()));
        return task;
    }

    private boolean hasUserAccessToProject(Long userId, String projectKey) {
        return checkUtils.checkUserProjectAccess(userId, projectKey);
    }

    private void saveTaskFile(Task task, MultipartFile mFile) {
        if(mFile == null){
            return;
        }
        File file = new File();
        file.setTask(task);
        setFlagForFileCreation(file);
            try {
                file.setFileData(mFile.getBytes());
            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        fileRepository.save(file);
        log.info(ResourceInformation.TASK_FILE_ADDED_MESSAGE);
    }

    private void setFlagForFileCreation(File file) {
        file.setActiveFlg(true);
        file.setUpdatedFlg(false);
        file.setStartDate(LocalDateTime.now());
        file.setEndDate(LocalDateTime.of(9999,12,31,23,59,59));
    }

    private void setFlagForFileUpdate(File file) {
        file.setActiveFlg(true);
        file.setUpdatedFlg(true);
        file.setUpdatedDate(LocalDateTime.now());
    }

}
