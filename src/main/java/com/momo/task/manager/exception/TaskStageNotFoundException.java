package com.momo.task.manager.exception;

public class TaskStageNotFoundException extends RuntimeException{
    public TaskStageNotFoundException(String message){
        super(message);
    }
}
