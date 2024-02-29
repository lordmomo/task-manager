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
    StatusLogRepository statusLogRepository;
    LabelRepository labelRepository;
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
                           StatusLogRepository statusLogRepository,
                           LabelRepository labelRepository,
                           CheckUtils checkUtils,
                           ModelMapper mapper) {

        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.fileRepository = fileRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.superAdminRepository = superAdminRepository;
        this.projectRepository = projectRepository;
        this.stagesRepository = stagesRepository;
        this.statusLogRepository = statusLogRepository;
        this.labelRepository = labelRepository;
        this.checkUtils = checkUtils;
        this.mapper = mapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        // Use strict matching strategy to avoid conflicts
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Explicitly define mappings to avoid conflicts
        mapper.createTypeMap(TaskDto.class, Task.class)
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
            this.saveTaskLabel(task, taskDto.getLabel());
            this.saveStatusLog(task, task.getStatus(), true);
            this.saveTaskFile(task, taskDto.getFile());
            log.info(ResourceInformation.TASK_CREATED_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_CREATED_MESSAGE);
        } else {
            return null;
        }
    }

    private void saveTaskLabel(Task task, List<String> labelNames) {
        if (labelNames != null && !labelNames.isEmpty()) {
            for (String name : labelNames) {
                Label label = checkUtils.getLabelFromName(name);
                checkUtils.saveToTaskLabel(task, label);
            }
        }
    }

    @Override
    public ResponseEntity<String> deleteTask(Long taskId) {


        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException(ResourceInformation.TASK_NOT_FOUND_MESSAGE);
        }
        if (!optionalTask.get().isActiveFlg()) {
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        this.removeAllTaskRelatedData(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_DELETED_MESSAGE);

    }

    @Override
    public ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            throw new TaskNotFoundException(ResourceInformation.TASK_NOT_FOUND_MESSAGE);
        }
        if (!optTask.get().isActiveFlg()) {
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
        Task task = optTask.get();
        this.updateTaskFile(task, taskDto.getFile());
        this.updateTaskFields(task, taskDto);
        task.setUpdatedFlg(true);
        this.updateTaskDateFields(task);
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

    @Override
    public ResponseEntity<List<Task>> getAllTaskOfLabel(String projectKey, String labelName) {
        Optional<Project> project = projectRepository.findByProjectKey(projectKey);
        if (project.isEmpty()) {
            throw new ProjectNotFoundException(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        Optional<Label> label = labelRepository.findByLabelName(labelName);
        if (label.isEmpty()){
            throw new LabelNotFoundException(ResourceInformation.LABEL_NOT_FOUND);
        }

        List<Task> taskList = checkUtils.getAllTaskFromLabel(projectKey,labelName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskList);

    }


    private void updateTaskFile(Task task, MultipartFile mFile) {
        Optional<File> optFile = Optional.ofNullable(fileRepository.findFileByTaskId(task.getTaskId()));
        if (optFile.isPresent()) {
            File file = optFile.get();
            if (mFile != null) {
                updateExistingFile(file, mFile);
            }
        } else {
            this.createNewFile(task, mFile);
        }
    }

    private void createNewFile(Task task, MultipartFile mFile) {
        File file = new File();
        file.setTask(task);
        this.setFlagForFileCreation(file);
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

    private void updateExistingFile(File file, MultipartFile mFile) {
        try {
            if (!Arrays.equals(file.getFileData(), mFile.getBytes())) {
                if (!mFile.isEmpty()) {
                    file.setFileData(mFile.getBytes());
                } else {
                    file.setFileData(null);
                }
                this.setFlagForFileUpdate(file);
            }
        } catch (IOException e) {
            throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
        }
    }


    private void updateTaskDateFields(Task task) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        task.setUpdatedDate(currentDateTime);
        task.setUpdatedStageDate(currentDateTime);
        task.setUpdatedStatusDate(currentDateTime);
    }

    private void updateTaskFields(Task task, TaskDto taskDto) {

        this.updateGeneralTaskInformation(task, taskDto);
        this.updateTaskStatus(task, taskDto);
        this.updateTaskStage(task, taskDto);
        this.updateAssociatedReporterUsers(taskDto.getReporterId(), task);
        this.updateAssociatedAssigneeUsers(taskDto.getAssigneeId(), task);
    }

    private void updateAssociatedReporterUsers(Long userId, Task task) {
        if (userId != null) {
            Optional<User> user = superAdminRepository.findById(userId);
            if (user.isEmpty()) {
                throw new UserNotFoundException(ResourceInformation.REPORTER_NOT_FOUND_MESSAGE);
            }
            if (user.get().isActiveFlg()) {
                throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
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
            if (user.get().isActiveFlg()) {
                throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
            }
            task.setAssigneeId(user.get());
        }
    }

    private void updateTaskStage(Task task, TaskDto taskDto) {
        if (task.getStageId() != null && taskDto.getStageId() != null) {
            Optional<Stages> stages = stagesRepository.findById(taskDto.getStageId());
            if (stages.isEmpty()) {
                throw new TaskStageNotFoundException(ResourceInformation.STAGE_NOT_FOUND_MESSAGE);
            }
            task.setStageId(stages.get());
        }
    }

    private void updateTaskStatus(Task task, TaskDto taskDto) {
        if (task.getStatus().getStatusId() != null
                && taskDto.getStatus() != null
                && !taskDto.getStatus().equals(task.getStatus().getStatusId())
        ) {
            Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(taskDto.getStatus());
            if (optionalTaskStatus.isEmpty()) {
                throw new TaskStatusNotFoundException(ResourceInformation.STATUS_NOT_FOUND_MESSAGE);
            }
            TaskStatus status = optionalTaskStatus.get();
            saveStatusLog(task, status, false);
            task.setStatus(status);
        }
    }

    private void saveStatusLog(Task task, TaskStatus status, boolean create) {
        Project project = task.getProject();
        User user = task.getAssigneeId();
        TaskStatus previousTaskStatus;
        if (create) {
            previousTaskStatus = null;
        } else {
            previousTaskStatus = task.getStatus();
        }
        StatusLog statusLog = StatusLog.builder()
                .projectId(project)
                .taskId(task)
                .userId(user)
                .transitionDate(LocalDateTime.now())
                .previousStatus(previousTaskStatus)
                .currentStatus(status)
                .activeFlag(true)
                .build();
        statusLogRepository.save(statusLog);
    }


    private void updateGeneralTaskInformation(Task task, TaskDto taskDto) {
        if (task.getTaskName() != null && !"".equalsIgnoreCase(task.getTaskName()) && taskDto.getTaskName() != null) {
            task.setTaskName(taskDto.getTaskName());
        }

        if (task.getDescription() != null && !"".equalsIgnoreCase(task.getDescription()) && taskDto.getDescription() != null
        ) {
            task.setDescription(taskDto.getDescription());
        }
//        if (taskDto.getLabel() != null) {
//            updateTaskLabels(task, taskDto.getLabel());
//        }
        if (task.getStartDate() != null && taskDto.getStartDate() != null) {
            task.setStartDate(taskDto.getStartDate());
        }
        if (task.getEndDate() != null && taskDto.getEndDate() != null) {
            task.setEndDate(taskDto.getEndDate());
        }
    }

//    private void updateTaskLabels(Task task, List<String> labelNames) {
//        if (labelNames != null && !labelNames.isEmpty()) {
//            Set<Label> labels = labelNames.stream()
//                    .map(labelName -> checkUtils.getLabelFromName(labelName))
//                    .collect(Collectors.toSet());
//            task.setLabels(labels);
//        } else {
//            task.setLabels(Collections.emptySet());
//        }
//    }

    private Task createTaskFromDto(TaskDto taskDto) {
        Task task = new Task();
        task.setActiveFlg(true);
        task.setTaskName(taskDto.getTaskName());
        task.setDescription(taskDto.getDescription());
        task.setType(taskDto.getType());
//        task.setLabel(taskDto.getLabel());
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
        if (mFile == null) {
            return;
        }
        File file = new File();
        file.setTask(task);
        this.setFlagForFileCreation(file);
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
        file.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    private void setFlagForFileUpdate(File file) {
        file.setActiveFlg(true);
        file.setUpdatedFlg(true);
        file.setUpdatedDate(LocalDateTime.now());
    }

    private void removeAllTaskRelatedData(Long taskId) {
        fileRepository.deleteByTaskId(taskId);
        commentRepository.deleteByTaskId(taskId);
        statusLogRepository.deleteByTaskId(taskId);
        taskRepository.deleteByTaskId(taskId);
    }

}
