package org.nbc.account.trollo.domain.comment.service;

import java.util.List;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetCardRes;
import org.nbc.account.trollo.domain.comment.dto.req.CommentGetUserReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentSaveReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentUpdateReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetUserRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentSaveRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentUpdateRes;
import org.nbc.account.trollo.domain.user.entity.User;

public interface CommentService {
    CommentSaveRes saveComment(CommentSaveReq req, Long cardId, User user);
    void deleteComment(Long commentId, User user);
    CommentUpdateRes updateComment(CommentUpdateReq req, Long commentId, User user);
    List<CommentGetUserRes> findUserComment(CommentGetUserReq req);
    List<CommentGetCardRes> findCardComment(Long cardId);
}
