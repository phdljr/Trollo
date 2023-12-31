package org.nbc.account.trollo.domain.comment.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nbc.account.trollo.domain.card.dto.response.CardAllReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.comment.CommentService;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetUserRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentSaveRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentUpdateRes;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.nbc.account.trollo.domain.user.entity.User;

@Mapper
public interface CommentServiceMapper {

    CommentServiceMapper INSTANCE = Mappers.getMapper(
        CommentServiceMapper.class);

    @Mapping(source = "user.nickname", target = "nickname")
    CommentSaveRes toCommentSaveRes(Comment comment);

    CommentUpdateRes toCommentUpdateRes(Comment comment);

    @Mapping(source = "user.nickname", target = "nickname")
    default String toUserNickname(User user) {
        return user.getNickname();
    }

    List<CommentGetUserRes> toCommentGetResUserList(List<Comment> commentEntities);

    CommentGetUserRes toCommentGetResUser(Comment comment);

}
