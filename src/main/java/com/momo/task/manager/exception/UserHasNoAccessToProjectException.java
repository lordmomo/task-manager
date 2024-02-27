package com.momo.task.manager.exception;

public class UserHasNoAccessToProjectException extends RuntimeException{
    public UserHasNoAccessToProjectException(String message){
        super(message);
    }
}
