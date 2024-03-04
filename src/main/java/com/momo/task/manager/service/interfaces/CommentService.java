package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.request.UpdateCommentRequestDto;
import com.momo.task.manager.response.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface CommentService {
    ResponseEntity<String> createComment(String projectKey, Long taskId, CommentDto commentDto) throws IOException;

    CustomResponse<Object> deleteComment(String projectKey, Long taskId, Long commentId, String username);

    CustomResponse<Object> updateComment(String projectKey, Long taskId, Long commentId, String username, UpdateCommentRequestDto updateCommentRequestDto);

    CustomResponse<Object> listAllComments(String projectKey, Long taskId);
}
