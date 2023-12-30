package org.nbc.account.trollo.domain.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.comment.dto.req.CommentDeleteReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentGetReqUser;
import org.nbc.account.trollo.domain.comment.dto.req.CommentSaveReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentUpdateReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetResUser;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/cards/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<Void> saveComment(@RequestBody CommentSaveReq req) {
        commentService.saveComment(req);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "save_comment");
    }

    @DeleteMapping
    public ApiResponse<Void> deleteComment(@RequestBody CommentDeleteReq req) {
        commentService.deleteComment(req);
        return new ApiResponse<>(HttpStatus.OK.value(), "delete_comment");
    }

    @PatchMapping
    public ApiResponse<Void> updateComment(@RequestBody CommentUpdateReq req) {
        commentService.updateComment(req);
        return new ApiResponse<>(HttpStatus.OK.value(), "update_comment");
    }

    @GetMapping
    public ApiResponse<List<CommentGetResUser>> getComments(@RequestBody CommentGetReqUser req) {
        return new ApiResponse<>(commentService.findUserComment(req));
    }

}
