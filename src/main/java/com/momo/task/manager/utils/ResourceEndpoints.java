package com.momo.task.manager.utils;


public class ResourceEndpoints {

    private ResourceEndpoints() {
        throw new IllegalStateException();
    }


    public static final String MAIN_COMMENT_KEY = "/comment";

//    Endpoints

    //comment controller endpoints
    public static final String CREATE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments";
    public static final String DELETE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments/{commentId}";
    public static final String UPDATE_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments/{commentId}";
    public static final String VIEW_ALL_COMMENTS_ENDPOINT = "/projects/{projectId}/tasks/{taskId}/comments";

    //superAdmin controller endpoints
    public static final String MAIN_SUPER_KEY = "/super";
    public static final String CREATE_PROJECTS_ENDPOINT = "/projects";
    public static final String UPDATE_PROJECTS_ENDPOINT = "/projects/{projectId}/update";
    public static final String DELETE_PROJECTS_ENDPOINT = "/projects/{projectId}/remove";
    public static final String CREATE_USERS_ENDPOINT = "/users";
    public static final String GET_USERS_DETAILS_ENDPOINT = "/users/{userId}";
    public static final String GET_ALL_USERS_ENDPOINT = "/users";
    public static final String GET_ONLY_USERS_ENDPOINT = "/only-users";
    public static final String DELETE_USERS_ENDPOINT = "/users/{userId}/remove";
    public static final String UPDATE_USERS_DETAILS_ENDPOINT = "/users/{userId}/details";
    public static final String UPDATE_USERS_CREDENTIALS_ENDPOINT = "/users/{userId}/credentials";
    public static final String UPDATE_USERS_PROFILE_PICTURE_ENDPOINT = "/users/{userId}/profile-picture";
    public static final String ADD_USERS_TO_PROJECT_ENDPOINT = "/project/{projectName}/add-users/{userId}";


    //TASK controller endpoints
    public static final String MAIN_TASK_KEY = "/task";
    public static final String CREATE_TASKS_ENDPOINT = "/tasks";
    public static final String DELETE_TASKS_ENDPOINT = "/tasks/{taskId}";
    public static final String UPDATE_TASKS_ENDPOINT = "/tasks/{taskId}/update";
    public static final String GET_ALL_TASKS_IN_PROJECT_ENDPOINT = "projects/{projectId}/tasks";


}
