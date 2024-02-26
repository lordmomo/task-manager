package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

public interface CommentService {
    ResponseEntity<String> createComment(Long projectId,Long taskId, CommentDto commentDto) throws IOException;

    ResponseEntity<String> deleteComment(Long projectId, Long taskId, Long commentId,CommentValidation commentValidation);

    ResponseEntity<String> updateComment(Long projectId,Long taskId, Long commentId,UpdateCommentDto updateCommentDto) throws IOException;

    ResponseEntity<Object> listAllComments(Long projectId, Long taskId);
}
