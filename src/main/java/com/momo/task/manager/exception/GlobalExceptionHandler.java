package com.momo.task.manager.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String  userNotFoundException(UserNotFoundException e){
        return e.getMessage();
    }
    @ExceptionHandler(PictureDataException.class)
    public String pictureDataException(PictureDataException e){
        return e.getMessage();
    }
    @ExceptionHandler(TaskStatusNotFoundException.class)
    public String taskStatusNotFoundException(TaskStatusNotFoundException e){
        return e.getMessage();
    }
    @ExceptionHandler(TaskStageNotFoundException.class)
    public String taskStageNotFoundException(TaskStageNotFoundException e){
        return e.getMessage();
    }
}
