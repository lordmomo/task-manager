package com.momo.task.manager.utils;


public class ResourceEndpoints {

    private ResourceEndpoints() {
        throw new IllegalStateException();
    }

//      Endpoints

//      Comment controller endpoints
    public static final String MAIN_COMMENT_KEY = "/comment";
    public static final String CREATE_COMMENTS_ENDPOINT = "/projects/{projectKey}/tasks/{taskId}/comments";
    public static final String DELETE_COMMENTS_ENDPOINT = "/projects/{projectKey}/tasks/{taskId}/comments/{commentId}/{username}/delete";
    public static final String UPDATE_COMMENTS_ENDPOINT = "/projects/{projectKey}/tasks/{taskId}/comments/{commentId}/{username}/update";
    public static final String VIEW_ALL_COMMENTS_ENDPOINT = "/projects/{projectKey}/tasks/{taskId}/comments";

//      SuperAdmin controller endpoints
    public static final String MAIN_SUPER_KEY = "/super";

//      Project operations
    public static final String CREATE_PROJECTS_ENDPOINT = "/projects";
    public static final String UPDATE_PROJECTS_ENDPOINT = "/projects/{projectKey}/update";
    public static final String DELETE_PROJECTS_ENDPOINT = "/projects/{projectKey}/remove";
    public static final String ADD_USERS_TO_PROJECT_ENDPOINT = "/project/{projectKey}/add-users/{username}";

//      User operations

    public static final String CREATE_USERS_ENDPOINT = "/users";
    public static final String GET_USERS_DETAILS_ENDPOINT = "/users/{username}";
    public static final String GET_ALL_USERS_ENDPOINT = "/users";
    public static final String GET_ONLY_USERS_ENDPOINT = "/only-users";
    public static final String DELETE_USERS_ENDPOINT = "/users/{username}/remove";
    public static final String UPDATE_USERS_DETAILS_ENDPOINT = "/users/{username}/details";
    public static final String UPDATE_USERS_CREDENTIALS_ENDPOINT = "/users/{username}/credentials";
    public static final String UPDATE_USERS_PROFILE_PICTURE_ENDPOINT = "/users/{username}/profile-picture";


//      Task controller endpoints
    public static final String MAIN_TASK_KEY = "/task";
    public static final String CREATE_TASKS_ENDPOINT = "/tasks";
    public static final String DELETE_TASKS_ENDPOINT = "/tasks/{taskId}";
    public static final String UPDATE_TASKS_ENDPOINT = "/tasks/{taskId}/update";
    public static final String GET_ALL_TASKS_IN_PROJECT_ENDPOINT = "projects/{projectKey}/tasks";


}
