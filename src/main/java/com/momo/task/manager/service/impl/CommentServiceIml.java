package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.model.Comment;
import com.momo.task.manager.model.Project;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.model.User;
import com.momo.task.manager.repository.CommentRepository;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ResourceInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceIml implements CommentService {
    CheckUtils checkUtils;
    CommentRepository commentRepository;
    @Autowired
    public CommentServiceIml(CheckUtils checkUtils, CommentRepository commentRepository) {
        this.checkUtils = checkUtils;
        this.commentRepository = commentRepository;
    }

    @Override
    public ResponseEntity<String> createComment(Long projectId,Long taskId, CommentDto commentDto) throws IOException {

        Long userId = commentDto.getUserId();
        ResponseEntity<String> validateUserProject = checkProjectAndUserIfNull(projectId,userId);
        if(validateUserProject != null){
            return validateUserProject;
        }

        ResponseEntity<String> validateUserProjectAccess = checkProjectAndUserAccess(projectId,userId);
        if(validateUserProjectAccess !=null){
            return validateUserProjectAccess;
        }

        Comment comment = new Comment();
        Task task = checkUtils.checkTaskExists(taskId);
        User user = checkUtils.getUserFromId(userId);
        comment.setTaskId(task);
        comment.setUserId(user);
        comment.setMessage(commentDto.getMessage());
        comment.setMessagePostDate(new Date());

        MultipartFile file = commentDto.getDocumentFile();

        if (file != null && !file.isEmpty()) {
            comment.setFileData(file.getBytes());
        } else {
            comment.setFileData(null);
        }
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_ADDED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteComment(Long projectId, Long taskId, Long commentId,CommentValidation commentValidation) {

        Long userId = commentValidation.getUserId();
        ResponseEntity<String> validateUserProject = checkProjectAndUserIfNull(projectId,userId);
        if(validateUserProject != null){
            return validateUserProject;
        }

        ResponseEntity<String> validateUserProjectAccess = checkProjectAndUserAccess(projectId,userId);
        if(validateUserProjectAccess !=null){
            return validateUserProjectAccess;
        }

        ResponseEntity<String> validateTaskIsOfProject =checkUtils.checkIfTaskBelongsToProject(projectId,taskId);
        if(validateTaskIsOfProject != null){
            return validateTaskIsOfProject;
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_DELETED_MESSAGE);

    }

    @Override
    public ResponseEntity<String> updateComment(Long projectId,Long taskId, Long commentId, UpdateCommentDto updateCommentDto) throws IOException {

        Long userId = updateCommentDto.getUserId();
        ResponseEntity<String> validateUserProject = checkProjectAndUserIfNull(projectId,userId);
        if(validateUserProject != null){
            return validateUserProject;
        }

        ResponseEntity<String> validateUserProjectAccess = checkProjectAndUserAccess(projectId,userId);
        if(validateUserProjectAccess !=null){
            return validateUserProjectAccess;
        }

        ResponseEntity<String> validateTaskIsOfProject =checkUtils.checkIfTaskBelongsToProject(projectId,taskId);
        if(validateTaskIsOfProject != null){
            return validateTaskIsOfProject;
        }

        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isPresent()) {
            Comment comment = optComment.get();
            comment.setMessage(updateCommentDto.getMessage());
            MultipartFile file = updateCommentDto.getDocumentFile();

            if (file != null && !file.isEmpty()) {
                comment.setFileData(file.getBytes());

            } else {
                comment.setFileData(null);
            }
            commentRepository.save(comment);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_UPDATED_MESSAGE);
        }
        log.info(ResourceInformation.COMMENT_NOT_FOUND_MESSAGE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.COMMENT_NOT_FOUND_MESSAGE);
    }

    @Override
    public ResponseEntity<Object> listAllComments(Long projectId, Long taskId) {
        Long checkNum = checkUtils.checkTaskIdBelongsToProjectId(projectId,taskId);
        if(checkNum != 1) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResourceInformation.TASK_DOES_NOT_BELONG_TO_PROJECT_MESSAGE);
        }
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(commentRepository.findAllByCommentByTaskId(taskId));
    }

    private ResponseEntity<String> checkProjectAndUserAccess(Long projectId, Long userId) {
        User user = checkUtils.getUserFromId(userId);
        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info(ResourceInformation.USER_HAS_NO_ACCESS_MESSAGE);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.USER_HAS_NO_ACCESS_MESSAGE);
        }
        return null;
    }

    private ResponseEntity<String> checkProjectAndUserIfNull(Long projectId, Long userId) {
        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(userId);
        if (project == null || user == null) {
            log.info(ResourceInformation.PROJECT_OR_USER_NOT_FOUND_MESSAGE);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.PROJECT_OR_USER_NOT_FOUND_MESSAGE);
        }
        return null;
    }

}
