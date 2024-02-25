package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.TaskDto;
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
import java.util.Collections;
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
//    public TaskServiceImpl() {
//        this.mapper = new ModelMapper();
//        configureModelMapper();
//    }

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
                .addMapping(TaskDto::getReporterId, Task::setReporterId)
                .addMapping(TaskDto::getAssigneeId, Task::setAssigneeId)
                .addMapping(TaskDto::getStageId, Task::setStageId);
    }

    @Override
    public ResponseEntity<String> createTask(TaskDto taskDto) throws IOException {

        Long checkReporterHasAccessToProject = checkUtils.checkUserProjectAccess(
                taskDto.getReporterId(), taskDto.getProject()
        );
        if (checkReporterHasAccessToProject == 1) {
            Long checkAssigneeHasAccessToProject = checkUtils.checkUserProjectAccess(
                    taskDto.getAssigneeId(), taskDto.getProject()
            );
            if (checkAssigneeHasAccessToProject == 1) {
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setDescription(taskDto.getDescription());
                task.setType(taskDto.getType());
                task.setStatus(checkUtils.getStatusFromId(taskDto.getStatus()));
                task.setStartDate(taskDto.getStartDate());
                task.setEndDate(taskDto.getEndDate());
                task.setProject(checkUtils.getProjectFromId(taskDto.getProject()));
                task.setAssigneeId(checkUtils.getUserFromId(taskDto.getAssigneeId()));
                task.setReporterId(checkUtils.getUserFromId(taskDto.getReporterId()));
                task.setStageId(checkUtils.getStageFromId(taskDto.getStageId()));

                taskRepository.save(task);
                saveTaskFile(task,taskDto.getFile());

                log.info(ResourceInformation.TASK_CREATED_MESSAGE);
                return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_CREATED_MESSAGE);
            } else {
                log.info("Assignee has no access to project");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.ASSIGNEE_HAS_NO_ACCESS_TO_PROJECT_MESSAGE);
            }
        } else {
            log.info("Reporter has no access to project");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.REPORTER_HAS_NO_ACCESS_TO_PROJECT_MESSAGE);
        }

    }

    private void saveTaskFile(Task task,MultipartFile mFile) throws IOException {
        File file = new File();
        file.setTask(task);
        if (mFile != null && !mFile.isEmpty()) {
            file.setFileData(mFile.getBytes());
        } else {
            file.setFileData(null);
        }
        fileRepository.save(file);
        log.info(ResourceInformation.TASK_FILE_ADDED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteTask(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isPresent()) {
            fileRepository.deleteByTaskId(taskId);
            commentRepository.findAllByCommentByTaskId(taskId);
            taskRepository.deleteById(taskId);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_DELETED_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.TASK_NOT_FOUND_MESSAGE);

    }

    @Override
    public ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            Task task = optTask.get();

            if (task.getTaskName() != null && !"".equalsIgnoreCase(task.getTaskName())) {
                task.setTaskName(taskDto.getTaskName());
            }

            if (task.getDescription() != null && !"".equalsIgnoreCase(task.getDescription())) {
                task.setDescription(taskDto.getDescription());
            }
            if (task.getStatus().getStatusId() != null) {
                Optional<TaskStatus> status = taskStatusRepository.findById(taskDto.getStatus());
                if (status.isPresent()) {
                    task.setStatus(status.get());
                } else {
                    log.info("task status not found");
                }
            }
            if (task.getLabel() != null && !"".equalsIgnoreCase(task.getLabel())) {
                task.setLabel(taskDto.getLabel());
            }
            if (task.getStartDate() != null) {
                task.setStartDate(taskDto.getStartDate());
            }
            if (task.getEndDate() != null) {
                task.setEndDate(taskDto.getEndDate());
            }
            if (task.getAssigneeId() != null) {
                Optional<User> user = superAdminRepository.findById(taskDto.getAssigneeId());
                if (user.isPresent()) {
                    task.setAssigneeId(user.get());
                } else {
                    log.info(ResourceInformation.ASSIGNEE_NOT_FOUND_MESSAGE);
                }
            }
            if (task.getReporterId() != null) {
                Optional<User> user = superAdminRepository.findById(taskDto.getReporterId());
                if (user.isPresent()) {
                    task.setReporterId(user.get());
                } else {
                    log.info(ResourceInformation.REPORTER_NOT_FOUND_MESSAGE);
                }
            }
            if (task.getStageId() != null) {
                Optional<Stages> stages = stagesRepository.findById(taskDto.getStageId());
                if (stages.isPresent()) {
                    task.setStageId(stages.get());
                } else {
                    log.info(ResourceInformation.STAGE_NOT_FOUND_MESSAGE);
                }
            }

            task.setUpdatedFlag(true);

            LocalDateTime currentDateTime = LocalDateTime.now();

            task.setUpdatedDate(currentDateTime);
            task.setUpdatedStageDate(currentDateTime);
            task.setUpdatedStatusDate(currentDateTime);

            taskRepository.save(task);
            log.info("save success in service");
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.TASK_UPDATED_MESSAGE);

        } else {
            log.info("save failed in service");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.TASK_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public ResponseEntity<?> getAllTask(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            log.info("project found");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskRepository.findByProdId(projectId));
        } else {
            log.info(ResourceInformation.PROJECT_NOT_FOUND_MESSAGE);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }


}
