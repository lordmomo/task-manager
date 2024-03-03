package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.model.Comment;
import com.momo.task.manager.response.CommentResponse;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(ResourceEndpoints.MAIN_COMMENT_KEY)
public class CommentController {

    @Autowired
    CommentService commentService;

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @PostMapping(ResourceEndpoints.CREATE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> createComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @ModelAttribute CommentDto commentDto) throws IOException {
        return commentService.createComment(projectKey, taskId, commentDto);
    }


    @PreAuthorize("#username == authentication.name")
    @PostMapping(ResourceEndpoints.DELETE_COMMENTS_ENDPOINT)
    public CommentResponse<Object> deleteComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @PathVariable("username") String username,
                                                @RequestBody CommentValidation commentValidation) {
        return commentService.deleteComment(projectKey, taskId, commentId, username, commentValidation);
    }

    @PreAuthorize("#username == authentication.name")
    @PostMapping(ResourceEndpoints.UPDATE_COMMENTS_ENDPOINT)
    public CommentResponse<Object> updateComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @PathVariable("username") String username,
                                                @ModelAttribute UpdateCommentDto updateCommentDto) {
        return commentService.updateComment(projectKey, taskId, commentId, username, updateCommentDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ResourceEndpoints.VIEW_ALL_COMMENTS_ENDPOINT)
    public CommentResponse<Object> listOfAllComments(@PathVariable("projectKey") String projectKey, @PathVariable("taskId") Long taskId) {
        return commentService.listAllComments(projectKey, taskId);
    }

}
