package com.momo.task.manager.utils;

import org.springframework.web.bind.annotation.*;

public class ResourceInformation {

    private ResourceInformation(){
        throw new IllegalStateException();
    }

    public static final String DEFAULT_IMAGE_PATH = "images/profile-svgrepo-com.svg" ;

    public static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    public static final String PROJECT_OR_USER_NOT_FOUND_MESSAGE = " Project or User not found";
    public static final String USER_HAS_NO_ACCESS_MESSAGE = "User has no access to the project";
    public static final String COMMENT_NOT_FOUND_MESSAGE = "Comment not found";
    public static final String TASK_DOES_NOT_BELONG_TO_PROJECT_MESSAGE = "Task doesn't belong to the project";
    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found";
    public static final String ASSIGNEE_NOT_FOUND_MESSAGE = "Assignee not found";
    public static final String REPORTER_NOT_FOUND_MESSAGE = "Reporter not found";
    public static final String STAGE_NOT_FOUND_MESSAGE = "Stage not found";

    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found";
    public static final String ASSIGNEE_HAS_NO_ACCESS_TO_PROJECT_MESSAGE = "Assignee has no access to project";
    public static final String REPORTER_HAS_NO_ACCESS_TO_PROJECT_MESSAGE = "Reporter has no access to project";



    public static final String ADMIN_CREATED_MESSAGE = "Admin created successfully";
    public static final String USER_CREATED_MESSAGE = "User created successfully";
    public static final String USER_DELETED_MESSAGE = "User created successfully";
    public static final String USER_DETAILS_UPDATED_MESSAGE = "User details updated successfully";
    public static final String USER_CREDENTIALS_UPDATED_MESSAGE = "User credentials updated successfully";
    public static final String USER_PROFILE_PICTURE_UPDATED_MESSAGE = "User profile picture updated successfully";
    public static final String USER_ADDED_TO_PROJECT_MESSAGE = "User added to project";
    public static final String PROJECT_CREATED_MESSAGE ="Project created successfully";
    public static final String PROJECT_DELETED_MESSAGE ="Project deleted successfully";
    public static final String PROJECT_UPDATED_MESSAGE ="Project updated successfully";
    public static final String TASK_CREATED_MESSAGE = "Task created successfully";
    public static final String TASK_DELETED_MESSAGE = "Task deleted successfully";
    public static final String TASK_UPDATED_MESSAGE = "Task updated successfully";
    public static final String COMMENT_ADDED_MESSAGE = "Comment added successfully";
    public static final String COMMENT_DELETED_MESSAGE = "Comment deleted successfully";
    public static final String COMMENT_UPDATED_MESSAGE = "Comment updated successfully";
    public static final String TASK_FILE_ADDED_MESSAGE = "Task file added successfully";

//    Endpoints

    //comment controller endpoints

    public static final String MAIN_COMMENT_KEY = "/comment";

    public static final String CREATE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments";
    public static final String DELETE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments/{commentId}";
    public static final String UPDATE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments/{commentId}";
    public static final String VIEW_ALL_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments";

    //superAdmin controller endpoints
    public static final String MAIN_SUPER_KEY = "/super";
    public static final String CREATE_PROJECTS_ENDPOINT = "/projects";
    public static final String UPDATE_PROJECTS_ENDPOINT = "/projects/{projectId}";
    public static final String DELETE_PROJECTS_ENDPOINT = "/projects/{projectId}";

    public static final String CREATE_ADMINS_ENDPOINT = "/admins";
    public static final String CREATE_USERS_ENDPOINT = "/users";
    public static final String GET_USERS_DETAILS_ENDPOINT = "/users/{userId}";
    public static final String GET_ALL_USERS_ENDPOINT = "/users";
    public static final String GET_ONLY_USERS_ENDPOINT = "/only-users";
    public static final String GET_ONLY_ADMINS_ENDPOINT = "/only-admins";
    public static final String DELETE_USERS_ENDPOINT = "/users/{userId}";
    public static final String UPDATE_USERS_DETAILS_ENDPOINT = "/users/{userId}";
    public static final String UPDATE_USERS_CREDENTIALS_ENDPOINT = "/users/{userId}/credentials";
    public static final String UPDATE_USERS_PROFILE_PICTURE_ENDPOINT = "/users/{userId}/profile-picture";
    public static final String ADD_USERS_TO_PROJECT_ENDPOINT = "/project/{projectId}/add-users/{userId}";

    //TASK controller endpoints

    public static final String MAIN_TASK_KEY = "/task";
    public static final String CREATE_TASKS_ENDPOINT = "/tasks";
    public static final String DELETE_TASKS_ENDPOINT = "/tasks/{taskId}";
    public static final String UPDATE_TASKS_ENDPOINT = "/tasks/{taskId}";
    public static final String GET_ALL_TASKS_IN_PROJECT_ENDPOINT = "projects/{projectId}/tasks";








// comment endpoints
//    @PostMapping("/projects/{projectId}/comment")
//    @DeleteMapping("/projects/{projectId}/comment/delete")
//    @PutMapping("/projects/{projectId}/comment/{commentId}/update/{userId}")
//    @GetMapping("/projects/{projectId}/{taskId}/list-all-comments")

//    suoerAdmin endpoints
//    @RequestMapping("/super")
//    @PostMapping("/create-projects")
//    @PutMapping("/update-project/{projectId}")
//    @DeleteMapping("/delete-project/{projectId}")
//    @PostMapping("/create-admin")
//    @PostMapping("/create-user")
//    @GetMapping("/get-user-details/{userId}")
//    @GetMapping("/get-all-users")
//    @GetMapping("/get-only-users")
//    @GetMapping("/get-only-admins")
//    @DeleteMapping("/remove-user/{userId}")
//    @PutMapping("/update-user-details/{userId}")
//    @PutMapping("/update-user-credentials/{userId}")
//    @PutMapping("/update-user-profile-picture/{userId}")
//    @PostMapping("/{projectName}/add-users/{userId}")

// task endpoints
//    @RequestMapping("/task")
//    @PostMapping("/create-tasks")
//    @DeleteMapping("/delete-task/{taskId}")
//    @PutMapping("/update-task/{taskId}")
//    @GetMapping("/projects/{projectId}/all-tasks")

}
