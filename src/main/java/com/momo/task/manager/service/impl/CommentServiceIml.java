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

    @Autowired
    CheckUtils checkUtils;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public ResponseEntity<String> createComment(Long projectId, CommentDto commentDto) throws IOException {


        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(Long.valueOf(commentDto.getUserId()));

        if (project == null || user == null) {
            log.info(ResourceInformation.projectOrUserNotFoundMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectOrUserNotFoundMessage);
        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info(ResourceInformation.userHasNoAccessMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.userHasNoAccessMessage);
        }

        Comment comment = new Comment();
        Task task = checkUtils.checkTaskExists(Long.valueOf(commentDto.getTaskId()));
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
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.commentAddedMessage);
    }

    @Override
    public ResponseEntity<String> deleteComment(Long projectId, CommentValidation commentValidation) {

        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(commentValidation.getUserId());

        if (project == null || user == null) {
            log.info(ResourceInformation.projectOrUserNotFoundMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectOrUserNotFoundMessage);

        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info(ResourceInformation.userHasNoAccessMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.userHasNoAccessMessage);
        }

        // try to use custom method
        commentRepository.deleteById(commentValidation.getCommentId());
        return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.commentDeletedMessage);

    }

    @Override
    public ResponseEntity<String> updateComment(Long projectId, Long userId, Long commentId, UpdateCommentDto updateCommentDto) throws IOException {

        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(userId);

        if (project == null || user == null) {
            log.info(ResourceInformation.projectOrUserNotFoundMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.projectOrUserNotFoundMessage);
        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info(ResourceInformation.userHasNoAccessMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.userHasNoAccessMessage);
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
            return ResponseEntity.status(HttpStatus.OK).body(ResourceInformation.commentUpdatedMessage);
        }
        log.info(ResourceInformation.commentNotFoundMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResourceInformation.commentNotFoundMessage);
    }

    @Override
    public ResponseEntity<?> listAllComments(Long projectId,Long taskId) {
        Long checkNum = checkUtils.checkTaskIdBelongsToProjectId(projectId,taskId);
        if(checkNum != 1) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResourceInformation.taskDoesNotBelongToProjectMessage);
        }
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(commentRepository.findAllByCommentByTaskId(taskId));
    }


}
