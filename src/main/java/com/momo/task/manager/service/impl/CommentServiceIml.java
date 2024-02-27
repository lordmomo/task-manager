package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.exception.PictureDataException;
import com.momo.task.manager.exception.TaskDoesNotBelongToProjectException;
import com.momo.task.manager.exception.UserHasNoAccessToProjectException;
import com.momo.task.manager.exception.UserNotFoundException;
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
import java.util.List;
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
    public ResponseEntity<String> createComment(Long projectId, Long taskId, CommentDto commentDto) {
        Long userId = commentDto.getUserId();
        this.performCommonValidations(projectId, userId);
        Comment comment = new Comment();
        createCommentFromDto(comment, userId, taskId, commentDto);
        MultipartFile file = commentDto.getDocumentFile();
        saveFileInComment(comment, file);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_ADDED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteComment(Long projectId, Long taskId, Long commentId, CommentValidation commentValidation) {

        try {
            Long userId = commentValidation.getUserId();
            this.performCommonValidations(projectId, userId);
            this.checkUtils.checkIfTaskBelongsToProject(projectId, taskId);
            commentRepository.deleteById(commentId);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_DELETED_MESSAGE);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (TaskDoesNotBelongToProjectException e) {
            throw new TaskDoesNotBelongToProjectException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateComment(Long projectId, Long taskId, Long commentId, UpdateCommentDto updateCommentDto) {
        try {
            Long userId = updateCommentDto.getUserId();
            this.performCommonValidations(projectId, userId);
            this.checkUtils.checkIfTaskBelongsToProject(projectId, taskId);
            this.updateCommentFromDto(commentId, updateCommentDto);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_UPDATED_MESSAGE);
        } catch (TaskDoesNotBelongToProjectException e) {
            throw new TaskDoesNotBelongToProjectException(e.getMessage());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> listAllComments(Long projectId, Long taskId) {

        try {
            this.checkUtils.checkIfTaskBelongsToProject(projectId, taskId);
            List<Comment> commentList = commentRepository.findAllByCommentByTaskId(taskId);
            //make commentList Dto..also for task
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(commentList);
        } catch (TaskDoesNotBelongToProjectException e) {
            throw new TaskDoesNotBelongToProjectException(e.getMessage());
        }
    }

    private void updateCommentFromDto(Long commentId, UpdateCommentDto updateCommentDto) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isPresent()) {
            Comment comment = optComment.get();
            comment.setMessage(updateCommentDto.getMessage());
            MultipartFile file = updateCommentDto.getDocumentFile();
            //check is file is null , delete then null too
            saveFileInComment(comment, file);
            commentRepository.save(comment);
        }
    }

    private void createCommentFromDto(Comment comment, Long userId, Long taskId, CommentDto commentDto) {
        try {
            Task task = checkUtils.checkTaskExists(taskId);
            User user = checkUtils.getUserFromId(userId);
            comment.setTaskId(task);
            comment.setUserId(user);
            comment.setMessage(commentDto.getMessage());
            comment.setMessagePostDate(new Date());
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }

    }

    private void saveFileInComment(Comment comment, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                comment.setFileData(file.getBytes());
            } catch (IOException e) {
                throw new PictureDataException(ResourceInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
            }
        } else {
            comment.setFileData(null);
        }
    }

    private void performCommonValidations(Long projectId, Long userId) {
        try {
            this.checkProjectAndUserIfNull(projectId, userId);
            this.checkProjectAndUserAccess(projectId, userId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (UserHasNoAccessToProjectException e) {
            throw new UserHasNoAccessToProjectException(e.getMessage());
        }
    }

    private void checkProjectAndUserAccess(Long projectId, Long userId) {
        try {
            User user = checkUtils.getUserFromId(userId);
            checkUtils.checkUserProjectAccess(user.getUserId(), projectId);
        }
        catch (UserHasNoAccessToProjectException e){
            throw new UserHasNoAccessToProjectException(e.getMessage());
        }
        catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }
    }

    private void checkProjectAndUserIfNull(Long projectId, Long userId) {
        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(userId);
        if (project == null || user == null) {
            throw new UserNotFoundException(ResourceInformation.PROJECT_OR_USER_NOT_FOUND_MESSAGE);
        }
    }
}
