package com.momo.task.manager.utils;

import com.momo.task.manager.exception.CommentNotFoundException;
import com.momo.task.manager.exception.TaskDoesNotBelongToProjectException;
import com.momo.task.manager.exception.UserHasNoAccessToProjectException;
import com.momo.task.manager.exception.UserNotFoundException;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    CommentRepository commentRepository;
    @Autowired
    public CheckUtils(AccessRepository accessRepository,
                      ProjectRepository projectRepository,
                      SuperAdminRepository superAdminRepository,
                      TaskRepository taskRepository,
                      TaskStatusRepository taskStatusRepository,
                      StagesRepository stagesRepository,
                      CommentRepository commentRepository) {

        this.accessRepository = accessRepository;
        this.projectRepository = projectRepository;
        this.superAdminRepository = superAdminRepository;
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.stagesRepository = stagesRepository;
        this.commentRepository = commentRepository;
    }

    public boolean checkUserProjectAccess(Long userId, String projectKey) {
        Long projectId = projectRepository.getProjectIdFromProjectKey(projectKey);
        Access check = accessRepository.validateUserProjectRelation(userId, projectId);
        if(check == null){
            throw new UserHasNoAccessToProjectException(ResourceInformation.USER_HAS_NO_ACCESS_MESSAGE);
        }
        return true;
    }

    public Task checkTaskExists(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.orElse(null);
    }
    public Project getProjectFromKey(String projectKey){
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        return optionalProject.orElse(null);
    }
    public User getUserFromId(Long userId){
        Optional<User> optionalUser = superAdminRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        return optionalUser.get();
    }

    public Long getUserIdFromUsername(String username){
        Optional<User> optionalUser = superAdminRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(ResourceInformation.USER_NOT_FOUND_MESSAGE);
        }
        return optionalUser.get().getUserId();
    }
    public TaskStatus getStatusFromId(Long statusId){
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(statusId);
        return optionalTaskStatus.orElse(null);
    }
    public Stages getStageFromId(Long stageId){
        Optional<Stages> optionalStages = stagesRepository.findById(stageId);
        return optionalStages.orElse(null);
    }

    public void checkIfTaskBelongsToProject(String projectKey,Long taskId){
        Long projectId = projectRepository.getProjectIdFromProjectKey(projectKey);
        Task check = taskRepository.doesTaskIdBelongToProjectId(projectId,taskId);
        if(check==null){
            throw new TaskDoesNotBelongToProjectException("Error in task and project relationship [Duplicate data]");
        }

    }

    public void checkIfCommentExists(Long commentId){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()){
            throw new CommentNotFoundException(ResourceInformation.COMMENT_NOT_FOUND_MESSAGE);
        }
    }

    public Long getUserIdFromCommentId(Long commentId) {
        return commentRepository.getUserIdFromCommentId(commentId);
    }
}
