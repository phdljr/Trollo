package org.nbc.account.trollo.domain.card.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nbc.account.trollo.domain.card.dto.response.CardReadResponseDto;
import org.nbc.account.trollo.domain.card.dto.response.CardReadDetailResponseDto;
import org.nbc.account.trollo.domain.card.entity.Card;
import org.nbc.account.trollo.domain.comment.dto.res.CommentReadResponseDto;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.nbc.account.trollo.domain.worker.dto.response.WorkerResponseDto;
import org.nbc.account.trollo.domain.worker.entity.Worker;

@Mapper
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.nickname", target = "nickname")
    WorkerResponseDto toWorkerResponseDto(Worker worker);

    @Mapping(source = "user.nickname", target = "nickname")
    CommentReadResponseDto toCommentResponseDto(Comment comment);

    CardReadDetailResponseDto toCardReadResponseDto(Card card, Float rate);

    CardReadResponseDto toCardAllReadResponseDto(Card card);

    List<CardReadResponseDto> toCardAllReadResponseDtoList(List<Card> card);
}
