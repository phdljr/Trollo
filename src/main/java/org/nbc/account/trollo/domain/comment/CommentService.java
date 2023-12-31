package org.nbc.account.trollo.domain.comment;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.card.repository.CardRepository;
import org.nbc.account.trollo.domain.comment.dto.req.CommentDeleteReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentGetUserReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetUserRes;
import org.nbc.account.trollo.domain.comment.dto.req.CommentSaveReq;
import org.nbc.account.trollo.domain.comment.dto.req.CommentUpdateReq;
import org.nbc.account.trollo.domain.comment.dto.res.CommentSaveRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentUpdateRes;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.nbc.account.trollo.domain.comment.exception.CommentDomainException;
import org.nbc.account.trollo.domain.comment.repository.CommentRepository;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.user.repository.UserRepository;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserBoardRepository userBoardRepository;


    public CommentSaveRes saveComment(CommentSaveReq req) {
        Long board = findBoard(req.cardId());
        User user = userRepository.findUserById(req.userid());
        Card card = cardRepository.findCardById(req.cardId());
        UserBoard userBoard = findUserBoard(req.userid(), board);
        if (userBoard == null) {
            throw new CommentDomainException(ErrorCode.NOT_FOUND_BOARD);
        }
        return CommentServiceMapper.INSTANCE.toCommentSaveRes(
            commentRepository.save(Comment.builder()
                .content(req.content())
                .user(user)
                .card(card)
                .build()));
    }

    public UserBoard findUserBoard(Long userid, Long boardid) {
        return userBoardRepository.findUserBoardByBoardIdAndUserId(userid, boardid);
    }

    public Long findBoard(Long cardid) {
        Card card = cardRepository.findBySection_Board_Id(cardid);
        Long bard = card.getSection().getBoard().getId();
        return bard;
    }

    private User findNickname(String nickname) {
        User userNickname = userRepository.findByNickname(nickname);
        if (userNickname == null) {
            throw new RuntimeException();
        }
        return userNickname;
    }

    @Transactional
    public void deleteComment(CommentDeleteReq req) {
        Comment comment = commentRepository.findByCommentId(req.commentId());
        if (comment == null) {
            throw new CommentDomainException(ErrorCode.NOT_FOUND_COMMENT);
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentUpdateRes updateComment(CommentUpdateReq req) {
        Comment comment = commentRepository.findByCommentIdAndUserNickname(
            req.commentId(),
            req.nickname());
        if (comment == null) {
            throw new CommentDomainException(ErrorCode.NOT_FOUND_COMMENT);
        }
        User nickname = findNickname(req.nickname());
        return CommentServiceMapper.INSTANCE.toCommentUpdateRes(
            commentRepository.save(Comment.builder()
                .commentId(req.commentId())
                .content(req.content())
                .user(nickname)
                .build())
        );
    }

    public List<CommentGetUserRes> findUserComment(CommentGetUserReq req) {
        return CommentServiceMapper.INSTANCE.toCommentGetResUserList(
            commentRepository.findByUserNickname(req.nickname()));
    }

    @Mapper
    public interface CommentServiceMapper {

        CommentService.CommentServiceMapper INSTANCE = Mappers.getMapper(
            CommentService.CommentServiceMapper.class);

        @Mapping(source = "user.nickname", target = "nickname")
        CommentSaveRes toCommentSaveRes(Comment comment);

        CommentUpdateRes toCommentUpdateRes(Comment comment);

        @Mapping(source = "user.nickname", target = "nickname")
        default String toUserNickname(User user) {
            return user.getNickname();
        }

        List<CommentGetUserRes> toCommentGetResUserList(List<Comment> commentEntities);

        @Mapping(source = "user", target = "nickname")
        CommentGetUserRes toCommentGetResUser(Comment comment);

    }

}
