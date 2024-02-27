package com.momo.task.manager.exception;

public class TaskDoesNotBelongToProjectException extends RuntimeException{
    public TaskDoesNotBelongToProjectException(String message){
        super(message);
    }
}
