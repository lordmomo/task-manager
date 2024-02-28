package com.momo.task.manager.utils;


import org.springframework.security.core.parameters.P;

public class ResourceInformation {

    private ResourceInformation() {
        throw new IllegalStateException();
    }
    public static final String DEFAULT_IMAGE_PATH = "images/profile-svgrepo-com.svg";
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
    public static final String STATUS_NOT_FOUND_MESSAGE = "Status not found";
    public static final String PICTURE_DATA_EXCEPTION_MESSAGE = "Error setting picture data";
    public static final String USER_CREATED_MESSAGE = "User created successfully";
    public static final String USER_DELETED_MESSAGE = "User deleted successfully";
    public static final String USER_DETAILS_UPDATED_MESSAGE = "User details updated successfully";
    public static final String USER_CREDENTIALS_UPDATED_MESSAGE = "User credentials updated successfully";
    public static final String USER_PROFILE_PICTURE_UPDATED_MESSAGE = "User profile picture updated successfully";
    public static final String USER_ADDED_TO_PROJECT_MESSAGE = "User added to project";
    public static final String PROJECT_CREATED_MESSAGE = "Project created successfully";
    public static final String PROJECT_DELETED_MESSAGE = "Project deleted successfully";
    public static final String PROJECT_UPDATED_MESSAGE = "Project updated successfully";
    public static final String TASK_CREATED_MESSAGE = "Task created successfully";
    public static final String TASK_DELETED_MESSAGE = "Task deleted successfully";
    public static final String TASK_UPDATED_MESSAGE = "Task updated successfully";
    public static final String COMMENT_ADDED_MESSAGE = "Comment added successfully";
    public static final String COMMENT_DELETED_MESSAGE = "Comment deleted successfully";
    public static final String COMMENT_UPDATED_MESSAGE = "Comment updated successfully";
    public static final String TASK_FILE_ADDED_MESSAGE = "Task file added successfully";

    //      Authentication constants
    public static final String TOKEN_ISSUER = "lord_momo";
    public static final String JWT_EXCEPTION_OCCURS_MESSAGE = "Jwt Exception occurs";
    public static final String ILLEGAL_ARGUMENT_OCCURS_MESSAGE ="Illegal Argument error";
    public static final String TOKEN_EXPIRED_MESSAGE ="Token has expired";
    public static final String MAlFORMED_JWT_EXCEPTION_MESSAGE = "Some changes has been done in token !! Invalid Token";
    public static final String ILLEGAL_ACCESS_ERROR_MESSAGE = "Illegal Argument while fetching the username !!";

    public static final String BAD_CREDENTIALS_MESSAGE = "Invalid email or password";


}
