package org.nbc.account.trollo.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.comment.dto.req.CommentSaveReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentUpdateReq;
import org.nbc.account.trollo.domain.comment.service.CommentService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public ApiResponse<Void> saveComment(
        @RequestBody CommentSaveReq req,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.saveComment(req, cardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "save_comment");
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "delete_comment");
    }

    @PutMapping("/comments/{commentId}")
    public ApiResponse<Void> updateComment(
        @RequestBody CommentUpdateReq req,
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(req, commentId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "update_comment");
    }
}
