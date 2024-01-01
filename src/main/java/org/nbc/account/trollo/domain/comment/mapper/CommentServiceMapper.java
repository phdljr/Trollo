package org.nbc.account.trollo.domain.comment.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetCardRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentGetUserRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentSaveRes;
import org.nbc.account.trollo.domain.comment.dto.res.CommentUpdateRes;
import org.nbc.account.trollo.domain.comment.entity.Comment;

@Mapper
public interface CommentServiceMapper {

    CommentServiceMapper INSTANCE = Mappers.getMapper(
        CommentServiceMapper.class);

    @Mapping(source = "user.nickname", target = "nickname")
    CommentSaveRes toCommentSaveRes(Comment comment);

    CommentUpdateRes toCommentUpdateRes(Comment comment);
    @Mapping(source = "user.nickname",target = "nickname")
    List<CommentGetUserRes> toCommentGetResUserList(List<Comment> commentEntities);

    CommentGetUserRes toCommentGetResUser(Comment comment);
    @Mapping(source = "user.nickname",target = "nickname")
    @Mapping(source = "card.id",target = "cardId")
    CommentGetCardRes toCommentGetCardRes(Comment comment);

    List<CommentGetCardRes> toCommentGetCardList(List<Comment> comments);

}
