package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.exception.DataHasBeenDeletedException;
import com.momo.task.manager.exception.PictureDataException;
import com.momo.task.manager.model.Comment;
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
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
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
    public ResponseEntity<String> createComment(String projectKey, Long taskId, CommentDto commentDto) {
        Long userId = commentDto.getUserId();
        this.performCommonValidations(projectKey, userId);
        Comment comment = new Comment();
        this.createCommentFromDto(comment, userId, taskId, commentDto);
        MultipartFile file = commentDto.getDocumentFile();
        this.saveFileInComment(comment, file);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_ADDED_MESSAGE);
    }

    @Override
    public ResponseEntity<String> deleteComment(String projectKey, Long taskId, Long commentId, String username, CommentValidation commentValidation) {

        try {
            checkIfDetailsAreAlreadyDeleted(projectKey,taskId,commentId,username);

            Long userIdOfUserWhoCommented = checkUtils.getUserIdFromCommentId(commentId);
            Long userIdOfUserWhoWantsToDeleteComment = checkUtils.getUserIdFromUsername(username);
            if (!userIdOfUserWhoWantsToDeleteComment.equals(userIdOfUserWhoCommented)) {
                throw new AccessDeniedException("access denied");
            }
            this.performCommonValidations(projectKey, userIdOfUserWhoWantsToDeleteComment);
            this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);
            this.checkUtils.checkIfCommentExists(commentId);
            commentRepository.deleteByCommentId(commentId);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_DELETED_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void checkIfDetailsAreAlreadyDeleted(String projectKey, Long taskId, Long commentId, String username) {
        if(!this.checkUtils.checkIfProjectIdDeleted(projectKey) ||
                !this.checkUtils.checkIfTaskIdDeleted(taskId) ||
                !this.checkUtils.checkIfCommentIdDeleted(commentId) ||
                !this.checkUtils.checkIfUserDeletedByUsername(username)
        ){
            throw new DataHasBeenDeletedException(ResourceInformation.DATA_HAS_DELETED_MESSAGE);
        }
    }

    @Override
    public ResponseEntity<String> updateComment(String projectKey, Long taskId, Long commentId,
                                                String username, UpdateCommentDto updateCommentDto) {
        try {
            checkIfDetailsAreAlreadyDeleted(projectKey,taskId,commentId,username);

            Long userIdOfUserWhoCommented = checkUtils.getUserIdFromCommentId(commentId);
            Long userIdOfUserWhoWantsToUpdateComment = checkUtils.getUserIdFromUsername(username);
            if (!userIdOfUserWhoWantsToUpdateComment.equals(userIdOfUserWhoCommented)) {
                throw new AccessDeniedException("access denied");
            }
            this.performCommonValidations(projectKey, userIdOfUserWhoWantsToUpdateComment);
            this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);
            this.updateCommentFromDto(commentId, updateCommentDto);
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.COMMENT_UPDATED_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> listAllComments(String projectKey, Long taskId) {

        this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);
        List<Comment> commentList = commentRepository.findAllByCommentByTaskId(taskId);
        //make commentList Dto..also for task
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentList);

    }

    private void updateCommentFromDto(Long commentId, UpdateCommentDto updateCommentDto) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isPresent()) {
            Comment comment = optComment.get();
            comment.setMessage(updateCommentDto.getMessage());
            comment.setUpdatedFlg(true);
            comment.setUpdatedDate(LocalDateTime.now());
            MultipartFile file = updateCommentDto.getDocumentFile();
            //check is file is null , delete then null too
            this.saveFileInComment(comment, file);
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
            comment.setMessagePostDate(LocalDateTime.now());
            this.setFlagForCommentCreation(comment);
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

    private void performCommonValidations(String projectKey, Long userId) {

        this.checkUtils.checkProjectAndUserIfNull(projectKey, userId);
        this.checkUtils.checkUserProjectAccess(userId, projectKey);

    }


    private void setFlagForCommentCreation(Comment comment) {
        comment.setActiveFlg(true);
        comment.setUpdatedFlg(false);
        comment.setStartDate(LocalDateTime.now());
        comment.setEndDate(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }
}
