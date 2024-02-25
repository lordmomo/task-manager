package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface CommentService {
    ResponseEntity<String> createComment(Long projectId, CommentDto commentDto) throws IOException;

    ResponseEntity<String> deleteComment(Long projectId, CommentValidation commentValidation);

    ResponseEntity<String> updateComment(Long projectId, Long userId,Long commentId,UpdateCommentDto updateCommentDto) throws IOException;

    ResponseEntity<?> listAllComments(Long projectId,Long taskId);
}
