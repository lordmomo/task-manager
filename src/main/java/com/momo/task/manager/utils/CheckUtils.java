package com.momo.task.manager.utils;

import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckUtils {

    @Autowired
    AccessRepository accessRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    SuperAdminRepository superAdminRepository;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    StagesRepository stagesRepository;

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

}
