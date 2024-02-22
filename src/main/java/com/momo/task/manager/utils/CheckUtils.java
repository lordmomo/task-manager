package com.momo.task.manager.utils;

import com.momo.task.manager.model.Project;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.model.User;
import com.momo.task.manager.repository.AccessRepository;
import com.momo.task.manager.repository.ProjectRepository;
import com.momo.task.manager.repository.SuperAdminRepository;
import com.momo.task.manager.repository.TaskRepository;
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

    public Long checkUserProjectAccess(Long userId, Long projectId) {
        return accessRepository.validateUserProjectRelation(userId, projectId);
    }

    public Project checkProjectExists(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        return optionalProject.orElse(null);
    }

    public Task checkTaskExists(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.orElse(null);
    }

    public User checkUserExists(Long userId) {
        Optional<User> optionalUser = superAdminRepository.findById(userId);
        return optionalUser.orElse(null);
    }


}
