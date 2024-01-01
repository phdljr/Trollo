package org.nbc.account.trollo.domain.comment;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.card.repository.CardRepository;
import org.nbc.account.trollo.domain.comment.dto.req.CommentGetUserReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetUserRes;
import org.nbc.account.trollo.domain.comment.dto.req.CommentSaveReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentUpdateReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentSaveRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentUpdateRes;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.nbc.account.trollo.domain.comment.exception.CommentDomainException;
import org.nbc.account.trollo.domain.comment.mapper.CommentServiceMapper;
import org.nbc.account.trollo.domain.comment.repository.CommentRepository;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final UserBoardRepository userBoardRepository;

    public CommentSaveRes saveComment(CommentSaveReq req, Long cardId, User user) {
        Card card = cardRepository.findCardById(cardId);
        Long boardId = findBoardId(cardId);
        Long userId = user.getId();
        UserBoard userBoard = findUserBoard(boardId, userId);
        if (userBoard == null) {
            throw new CommentDomainException(ErrorCode.NOT_FOUND_USER_BOARD);
        }
        return CommentServiceMapper.INSTANCE.toCommentSaveRes(
            commentRepository.save(Comment.builder()
                .content(req.content())
                .user(user)
                .card(card)
                .build()));
    }

    private UserBoard findUserBoard(Long userid, Long boardid) {
        return userBoardRepository.findByUserIdAndBoardId(userid, boardid);
    }

    private Long findBoardId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(
            () -> new CommentDomainException(ErrorCode.NOT_FOUND_CARD)
        );
        Long boardId = card.getSection().getBoard().getId();
        return boardId;
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findCommentById(commentId);
        UserBoard userBoard = userBoardRepository.findByUser_Id(user.getId());
        if (validateUserAndComment(comment, userBoard.getBoard().getId())) {
            commentRepository.delete(comment);
        }
    }

    @Transactional
    public CommentUpdateRes updateComment(CommentUpdateReq req, Long commentId, User user) {
        Comment comment = commentRepository.findCommentByIdAndUserId(
            commentId,
            user.getId());
        UserBoard userBoard = userBoardRepository.findByUser_Id(user.getId());
        if (validateUserAndComment(comment, userBoard.getBoard().getId())) {
            return CommentServiceMapper.INSTANCE.toCommentUpdateRes(
                commentRepository.save(Comment.builder()
                    .id(commentId)
                    .content(req.content())
                    .user(user)
                    .build())
            );
        }
        return null;
    }

    public boolean validateUserAndComment(Comment comment, Long boardId) {
        if (comment == null || boardId == null) {
            throw new CommentDomainException((ErrorCode.BAD_COMMENT_AND_BOARD_ID));
        } else {
            return true;
        }
    }

    public List<CommentGetUserRes> findUserComment(CommentGetUserReq req) {
        return CommentServiceMapper.INSTANCE.toCommentGetResUserList(
            commentRepository.findByUserNickname(req.nickname()));
    }
}
