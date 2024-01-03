package org.nbc.account.trollo.domain.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
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
}
