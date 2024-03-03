package com.momo.task.manager.utils;

public class ResourceInformation {

    public static final String DATA_HAS_DELETED_MESSAGE ="Data has been deleted" ;
    public static final String LABEL_NOT_FOUND = "Label not found" ;

    public static final String ACCESS_DENIED_MESSAGE = "Access denied";

    private ResourceInformation() {
        throw new IllegalStateException();
    }
    public static final String DEFAULT_IMAGE_PATH = "images/profile-svgrepo-com.svg";
    public static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public static final String USERNAME_NOT_FOUND_MESSAGE = "Username not found";

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
    public static final String TOKEN_INVALID_MESSAGE ="Invalid token";

    public static final String MALFORMED_JWT_EXCEPTION_MESSAGE = "Some changes has been done in token !! Invalid Token";
    public static final String ILLEGAL_ACCESS_ERROR_MESSAGE = "Illegal Argument while fetching the username !!";

    public static final String BAD_CREDENTIALS_MESSAGE = "Invalid email or password";

    //      Bean validations

    public static final String UPDATE_DATE_BEAN_VALIDATION_MESSAGE = "Update date must be in the past or present";
    public static final String START_DATE_BEAN_VALIDATION_MESSAGE = "Start date must be in the past or present";
    public static final String END_DATE_BEAN_VALIDATION_MESSAGE = "End date must be in the future or present";
    public static final String MESSAGE_POSTED_DATE_BEAN_VALIDATION_MESSAGE = "Message posted date must be in the past or present ";


    public static final String ROLE_NOT_BLANK_VALIDATION_MESSAGE = "Role name is required";
    public static final String STATUS_NOT_BLANK_VALIDATION_MESSAGE = "Status name is required";

    public static final String PASSWORD_LENGTH_MESSAGE = "Password length must be at least 8 characters";
    public static final String PASSWORD_REQUIRED_MESSAGE = "Password is required";

    public static final String USERNAME_LENGTH_MESSAGE = "Username length must be between 6 and 16 characters";
    public static final String USERNAME_REQUIRED_MESSAGE = "Username is required";

    public static final String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid email format";
    public static final String EMAIL_REQUIRED_MESSAGE = "Email is required";

    public static final String FIRST_NAME_LENGTH_MESSAGE = "First name must be between 3 and 16 characters";
    public static final String FIRST_NAME_REQUIRED_MESSAGE = "First name is required";

    public static final String LAST_NAME_LENGTH_MESSAGE = "Last name must be between 3 and 16 characters";
    public static final String LAST_NAME_REQUIRED_MESSAGE = "Last name is required";


    public static final String PROJECT_NAME_REQUIRED_MESSAGE = "Project name is required";
    public static final String PROJECT_KEY_REQUIRED_MESSAGE = "Project key is required";
    public static final String PROJECT_TEMPLATE_REQUIRED_MESSAGE = "Project template is required";
    public static final String PROJECT_TYPE_REQUIRED_MESSAGE = "Project type is required";

    public static final String TASK_NAME_REQUIRED_MESSAGE = "Task name is required";

    public static final String TASK_DESCRIPTION_REQUIRED_MESSAGE = "Task description is required";

    public static final String TASK_TYPE_REQUIRED_MESSAGE = "Task type is required";

    public static final String TASK_LABEL_REQUIRED_MESSAGE = "Task label is required";

    public static final String TASK_START_DATE_REQUIRED_MESSAGE = "Start date is required";

    public static final String TASK_END_DATE_REQUIRED_MESSAGE = "End date is required";






}
