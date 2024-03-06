package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.request.UpdateCommentRequestDto;
import com.momo.task.manager.exception.DataHasBeenDeletedException;
import com.momo.task.manager.exception.PictureDataException;
import com.momo.task.manager.model.Comment;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.model.User;
import com.momo.task.manager.repository.CommentDbRepository;
import com.momo.task.manager.response.CustomResponse;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.RefreshCache;
import com.momo.task.manager.utils.ConstantInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@EnableCaching
@CacheConfig(cacheNames = "COMMENT")
public class CommentServiceIml implements CommentService {
    CheckUtils checkUtils;
    CommentDbRepository commentRepository;
    RefreshCache refreshCache;

    @Autowired
    public CommentServiceIml(CheckUtils checkUtils,
                             CommentDbRepository commentRepository,
                             RefreshCache refreshCache) {
        this.checkUtils = checkUtils;
        this.commentRepository = commentRepository;
        this.refreshCache = refreshCache;
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
        return ResponseEntity.status(HttpStatus.OK).body(ConstantInformation.COMMENT_ADDED_MESSAGE);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#taskId", value = "COMMENT")
    public CustomResponse<Object> deleteComment(String projectKey, Long taskId, Long commentId, String username) {

        try {
            checkIfDetailsAreAlreadyDeleted(projectKey, taskId, commentId, username);
            checkIfUserTryingToModifyIsSameAsCreator(commentId,username,projectKey);
            this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);
            this.checkUtils.checkIfCommentExists(commentId);
            commentRepository.deleteByCommentId(commentId);
            this.refreshCache.refresh("COMMENT");
            List<CommentDto> responseCommentList = getCommentList(projectKey, taskId);

            return CustomResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ConstantInformation.COMMENT_DELETED_MESSAGE)
                    .data(responseCommentList)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    @Transactional
    @CachePut(key = "#taskId", value = "COMMENT")
    public CustomResponse<Object> updateComment(String projectKey, Long taskId, Long commentId,
                                                String username, UpdateCommentRequestDto updateCommentRequestDto) {
        try {
            checkIfDetailsAreAlreadyDeleted(projectKey, taskId, commentId, username);
            checkIfUserTryingToModifyIsSameAsCreator(commentId,username,projectKey);
            this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);
            this.updateCommentFromDto(commentId, updateCommentRequestDto);
//          Use flush concept
//          flush removes all the data from cache and then retrieves new data from db.
            this.refreshCache.refresh("COMMENT");

            List<CommentDto> responseCommentList = getCommentList(projectKey, taskId);

//          Returns the updated comment you want to store in the cache
            return CustomResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ConstantInformation.COMMENT_UPDATED_MESSAGE)
                    .data(responseCommentList)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Cacheable(key = "#taskId", value = "COMMENT")
    public CustomResponse<Object> listAllComments(String projectKey, Long taskId) {

        List<CommentDto> responseCommentList = getCommentList(projectKey, taskId);

        log.info("inside db");
        return CustomResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success OK")
                .data(responseCommentList)
                .build();

    }

    private void checkIfUserTryingToModifyIsSameAsCreator(Long commentId, String username,String projectKey) {
        try {
            Long userIdOfCreator = checkUtils.getUserIdFromCommentId(commentId);
            Long userIdOfModifier = checkUtils.getUserIdFromUsername(username);
            if (!userIdOfModifier.equals(userIdOfCreator)) {
                throw new AccessDeniedException(ConstantInformation.ACCESS_DENIED_MESSAGE);
            }
            this.performCommonValidations(projectKey, userIdOfModifier);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    private void checkIfDetailsAreAlreadyDeleted(String projectKey, Long taskId, Long commentId, String username) {
        if (!this.checkUtils.checkIfProjectIdDeleted(projectKey) ||
                !this.checkUtils.checkIfTaskIdDeleted(taskId) ||
                !this.checkUtils.checkIfCommentIdDeleted(commentId) ||
                !this.checkUtils.checkIfUserDeletedByUsername(username)
        ) {
            throw new DataHasBeenDeletedException(ConstantInformation.DATA_HAS_DELETED_MESSAGE);
        }
    }

    private List<CommentDto> getCommentList(String projectKey, Long taskId) {
        this.checkUtils.checkIfTaskBelongsToProject(projectKey, taskId);

        List<Comment> commentList = commentRepository.findAllByCommentByTaskId(taskId);
        List<CommentDto> responseCommentList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentDto commentDto = CommentDto
                    .builder()
                    .userId(comment.getUserId().getUserId())
                    .username(comment.getUserId().fullName())
                    .fileData(comment.getFileData())
                    .message(comment.getMessage())
                    .build();
            responseCommentList.add(commentDto);
        }
        return responseCommentList;
    }

    private void updateCommentFromDto(Long commentId, UpdateCommentRequestDto updateCommentRequestDto) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isPresent()) {
            Comment comment = optComment.get();
            comment.setMessage(updateCommentRequestDto.getMessage());
            comment.setUpdatedFlg(true);
            comment.setUpdatedDate(LocalDateTime.now());
            MultipartFile file = updateCommentRequestDto.getDocumentFile();
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
                throw new PictureDataException(ConstantInformation.PICTURE_DATA_EXCEPTION_MESSAGE);
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