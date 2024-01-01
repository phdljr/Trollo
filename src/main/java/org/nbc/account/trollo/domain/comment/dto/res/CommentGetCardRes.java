package org.nbc.account.trollo.domain.comment.dto.res;

public record CommentGetCardRes(
    Long cardId,
    String nickname,
    String content
) {

}
