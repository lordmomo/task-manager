package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.request.UpdateCommentRequestDto;
import com.momo.task.manager.response.CustomResponse;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.ConstantEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping(ConstantEndpoints.MAIN_COMMENT_KEY)
public class CommentController {

    @Autowired
    CommentService commentService;

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping(ConstantEndpoints.CREATE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> createComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @ModelAttribute CommentDto commentDto) throws IOException {
        return commentService.createComment(projectKey, taskId, commentDto);
    }


    @PreAuthorize("#username == authentication.name")
    @PostMapping(ConstantEndpoints.DELETE_COMMENTS_ENDPOINT)
    public CustomResponse<Object> deleteComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @PathVariable("username") String username
                                                ) {
        return commentService.deleteComment(projectKey, taskId, commentId, username);
    }

    @PreAuthorize("#username == authentication.name")
    @PostMapping(ConstantEndpoints.UPDATE_COMMENTS_ENDPOINT)
    public CustomResponse<Object> updateComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @PathVariable("username") String username,
                                                @ModelAttribute UpdateCommentRequestDto updateCommentRequestDto) {
        return commentService.updateComment(projectKey, taskId, commentId, username, updateCommentRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ConstantEndpoints.VIEW_ALL_COMMENTS_ENDPOINT)
    public CustomResponse<Object> listOfAllComments(@PathVariable("projectKey") String projectKey, @PathVariable("taskId") Long taskId) {
        return commentService.listAllComments(projectKey, taskId);
    }

}
