package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


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
    public ResponseEntity<String> deleteComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody CommentValidation commentValidation) {
        return commentService.deleteComment(projectKey, taskId, commentId, commentValidation);
    }
    @PreAuthorize("#username == authentication.name")
    @PutMapping(ResourceEndpoints.UPDATE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> updateComment(@PathVariable("projectKey") String projectKey,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @ModelAttribute UpdateCommentDto updateCommentDto) throws IOException {
        return commentService.updateComment(projectKey, taskId, commentId, updateCommentDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ResourceEndpoints.VIEW_ALL_COMMENTS_ENDPOINT)
    public ResponseEntity<Object> listOfAllComments(@PathVariable("projectKey") String projectKey, @PathVariable("taskId") Long taskId) {
        return commentService.listAllComments(projectKey, taskId);
    }

}
