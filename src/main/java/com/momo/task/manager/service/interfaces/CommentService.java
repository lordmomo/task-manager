package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

public interface CommentService {
    ResponseEntity<String> createComment(String projectKey,Long taskId, CommentDto commentDto) throws IOException;

    ResponseEntity<String> deleteComment(String projectKey, Long taskId, Long commentId,CommentValidation commentValidation);

    ResponseEntity<String> updateComment(String projectKey,Long taskId, Long commentId,UpdateCommentDto updateCommentDto) throws IOException;

    ResponseEntity<Object> listAllComments(String projectKey, Long taskId);
}
