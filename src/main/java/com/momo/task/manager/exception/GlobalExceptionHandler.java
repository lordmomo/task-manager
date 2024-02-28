package com.momo.task.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String>  userNotFoundException(UserNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(PictureDataException.class)
    public ResponseEntity<String>  pictureDataException(PictureDataException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(TaskStatusNotFoundException.class)
    public ResponseEntity<String> taskStatusNotFoundException(TaskStatusNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    @ExceptionHandler(TaskStageNotFoundException.class)
    public ResponseEntity<String> taskStageNotFoundException(TaskStageNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(UserHasNoAccessToProjectException.class)
    public ResponseEntity<String> userHasNoAccessToProjectException(UserHasNoAccessToProjectException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
    @ExceptionHandler(TaskDoesNotBelongToProjectException.class)
    public ResponseEntity<String> taskDoesNotBelongToProjectException(TaskDoesNotBelongToProjectException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> projectNotFoundException(ProjectNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> taskNotFoundException(TaskNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> invalidTokenException(InvalidTokenException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<String> userNameNotFoundException(UserNameNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


}

