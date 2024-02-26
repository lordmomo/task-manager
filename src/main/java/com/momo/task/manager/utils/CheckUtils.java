package com.momo.task.manager.utils;

import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckUtils {
    AccessRepository accessRepository;
    ProjectRepository projectRepository;
    SuperAdminRepository superAdminRepository;
    TaskRepository taskRepository;
    TaskStatusRepository taskStatusRepository;
    StagesRepository stagesRepository;
    @Autowired
    public CheckUtils(AccessRepository accessRepository,
                      ProjectRepository projectRepository,
                      SuperAdminRepository superAdminRepository,
                      TaskRepository taskRepository,
                      TaskStatusRepository taskStatusRepository,
                      StagesRepository stagesRepository) {

        this.accessRepository = accessRepository;
        this.projectRepository = projectRepository;
        this.superAdminRepository = superAdminRepository;
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.stagesRepository = stagesRepository;
    }

    public Long checkUserProjectAccess(Long userId, Long projectId) {
        return accessRepository.validateUserProjectRelation(userId, projectId);
    }
    public Long checkTaskIdBelongsToProjectId(Long projectId,Long taskId){
        return taskRepository.validateCheckTaskIdBelongsToProjectId(projectId,taskId);
    }
    public Task checkTaskExists(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.orElse(null);
    }
    public Project getProjectFromId(Long projectId){
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        return optionalProject.orElse(null);
    }
    public User getUserFromId(Long userId){
        Optional<User> optionalUser = superAdminRepository.findById(userId);
        return optionalUser.orElse(null);
    }
    public TaskStatus getStatusFromId(Long statusId){
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(statusId);
        return optionalTaskStatus.orElse(null);
    }
    public Stages getStageFromId(Long stageId){
        Optional<Stages> optionalStages = stagesRepository.findById(stageId);
        return optionalStages.orElse(null);
    }

    public ResponseEntity<String> checkIfTaskBelongsToProject(Long projectId, Long taskId){
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if(optionalTask.isEmpty() || optionalProject.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task or Project not found");
        }

        Long checkNum = taskRepository.findTaskIdBelongsToProjectId(projectId,taskId);
        if(checkNum == 1 ){
            return null;
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error in task and project relationship [Duplicate data]");
        }
    }

}
