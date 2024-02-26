package com.momo.task.manager.exception;

public class TaskStatusNotFoundException extends RuntimeException{
    public TaskStatusNotFoundException(String message){
        super(message);
    }
}
