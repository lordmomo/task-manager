package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.model.Comment;
import com.momo.task.manager.response.CommentResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    ResponseEntity<String> createComment(String projectKey, Long taskId, CommentDto commentDto) throws IOException;

    CommentResponse<Object> deleteComment(String projectKey, Long taskId, Long commentId, String username, CommentValidation commentValidation);

    CommentResponse<Object> updateComment(String projectKey, Long taskId, Long commentId, String username, UpdateCommentDto updateCommentDto);

    CommentResponse<Object> listAllComments(String projectKey, Long taskId);
}
