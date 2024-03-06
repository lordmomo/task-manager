package com.momo.task.manager.utils;

import com.momo.task.manager.exception.*;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CheckUtils {

    AccessRepository accessRepository;
    ProjectRepository projectRepository;
    SuperAdminRepository superAdminRepository;
    TaskRepository taskRepository;
    TaskStatusRepository taskStatusRepository;
    CommentDbRepository commentRepository;
    LabelRepository labelRepository;
    TaskLabelRepository taskLabelRepository;

    @Autowired
    public CheckUtils(AccessRepository accessRepository,
                      ProjectRepository projectRepository,
                      SuperAdminRepository superAdminRepository,
                      TaskRepository taskRepository,
                      TaskStatusRepository taskStatusRepository,
                      CommentDbRepository commentRepository,
                      LabelRepository labelRepository,
                      TaskLabelRepository taskLabelRepository) {

        this.accessRepository = accessRepository;
        this.projectRepository = projectRepository;
        this.superAdminRepository = superAdminRepository;
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.commentRepository = commentRepository;
        this.labelRepository = labelRepository;
        this.taskLabelRepository = taskLabelRepository;
    }

    public boolean checkUserProjectAccess(Long userId, String projectKey) {
        Long projectId = projectRepository.getProjectIdFromProjectKey(projectKey);
        Access check = accessRepository.validateUserProjectRelation(userId, projectId);
        if (check == null) {
            throw new UserHasNoAccessToProjectException(ConstantInformation.USER_HAS_NO_ACCESS_MESSAGE);
        }
        return true;
    }


    public void checkProjectAndUserIfNull(String projectKey, Long userId) {
        Project project = getProjectFromKey(projectKey);
        User user = getUserFromId(userId);
        if (project == null || user == null) {
            throw new UserNotFoundException(ConstantInformation.PROJECT_OR_USER_NOT_FOUND_MESSAGE);
        }
    }

    public Task checkTaskExists(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.orElse(null);
    }

    public Project getProjectFromKey(String projectKey) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        return optionalProject.orElse(null);
    }

    public User getUserFromId(Long userId) {
        Optional<User> optionalUser = superAdminRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        return optionalUser.get();
    }

    public Long getUserIdFromUsername(String username) {
        Optional<User> optionalUser = superAdminRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        return optionalUser.get().getUserId();
    }

    public TaskStatus getStatusFromId(Long statusId) {
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(statusId);
        return optionalTaskStatus.orElse(null);
    }

    public void checkIfTaskBelongsToProject(String projectKey, Long taskId) {
        Long projectId = projectRepository.getProjectIdFromProjectKey(projectKey);
        Task check = taskRepository.doesTaskIdBelongToProjectId(projectId, taskId);
        if (check == null) {
            throw new TaskDoesNotBelongToProjectException(ConstantInformation.TASK_DOES_NOT_BELONG_TO_PROJECT_MESSAGE);
        }

    }

    public void checkIfCommentExists(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CommentNotFoundException(ConstantInformation.COMMENT_NOT_FOUND_MESSAGE);
        }
    }

    public Long getUserIdFromCommentId(Long commentId) {
        return commentRepository.getUserIdFromCommentId(commentId);
    }

    public boolean checkIfProjectIdDeleted(String projectKey) {
        Optional<Project> optionalProject = projectRepository.findByProjectKey(projectKey);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException(ConstantInformation.PROJECT_NOT_FOUND_MESSAGE);
        }
        return optionalProject.get().isActiveFlg();
    }

    public boolean checkIfTaskIdDeleted(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException(ConstantInformation.TASK_NOT_FOUND_MESSAGE);
        }
        return optionalTask.get().isActiveFlg();
    }

    public boolean checkIfCommentIdDeleted(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CommentNotFoundException(ConstantInformation.COMMENT_NOT_FOUND_MESSAGE);
        }
        return optionalComment.get().isActiveFlg();
    }

    public boolean checkIfUserDeletedByUsername(String username) {
        Optional<User> optionalUser = superAdminRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(ConstantInformation.USER_NOT_FOUND_MESSAGE);
        }
        return optionalUser.get().isActiveFlg();
    }

    public Label getLabelFromName(String labelName) {

        Optional<Label> existingLabel = labelRepository.findByLabelName(labelName);

        if (existingLabel.isPresent()) {
            return existingLabel.get();
        } else {
            Label newLabel = Label.builder()
                    .labelName(labelName)
                    .build();
            labelRepository.save(newLabel);
            return newLabel;
        }
    }

    public void saveToTaskLabel(Task task, Label label) {
        TaskLabel taskLabel = TaskLabel.builder()
                .label(label)
                .task(task)
                .build();
        taskLabelRepository.save(taskLabel);
    }

    public List<Task> getAllTaskFromLabel(String projectKey, String labelName) {
        Long labelId = labelRepository.getLabelIdFromName(labelName);
        List<Long> taskIds = taskLabelRepository.getTaskIdFromLabel(labelId);
        List<Task> approvedTasks = new ArrayList<>();
        for (Long task : taskIds) {
            checkIfTaskBelongsToProject(projectKey, task);
            Optional<Task> taskOptional = taskRepository.findById(task);
            taskOptional.ifPresent(approvedTasks::add);

        }
        return approvedTasks;
    }

    public void checkIfLabelExistsInTaskLabel(Long taskId, String label) {

        Optional<Label> optionalLabel = labelRepository.findByLabelName(label);
        if (optionalLabel.isEmpty()) {
            Label newLabel = Label.builder()
                    .labelName(label)
                    .build();
            labelRepository.save(newLabel);
            optionalLabel = Optional.of(newLabel);
        }

        Label tempLabel = optionalLabel.get();

        TaskLabel taskLabel = taskLabelRepository.findByTaskIdAndLabelId(taskId, tempLabel.getLabelId());
        if (taskLabel == null) {
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                saveToTaskLabel(task, tempLabel);
            }
        }

    }

    public void removeLabelsFromTaskLabel(Long taskId, List<String> labels) {
        List<Long> currentLabels = taskLabelRepository.getAllLabelIdFromTaskId(taskId);

        List<Label> currentLabelNames = new ArrayList<>();

        for (Long labelId : currentLabels) {
            Optional<Label> optLabel = labelRepository.findById(labelId);
            optLabel.ifPresent(currentLabelNames::add);
        }

        for (Label label : currentLabelNames) {
            String labelName = label.getLabelName();
            if (!labels.contains(labelName)) {
                taskLabelRepository.deleteByTaskIdAndLabelName(taskId, label.getLabelId());
            }
        }
    }

    public Long getTaskIdFromTaskName(String taskName) {
        return taskRepository.getTaskIdFromTaskName(taskName);
    }

    public List<Long> getUserIdByProjectKey(Long projectId) {
        return accessRepository.getUserIdByProjectKey(projectId);
    }

    public Optional<User> checkIfUserIsAdmin(Long userId) {
        return superAdminRepository.findById(userId);
    }
}
