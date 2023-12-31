package org.nbc.account.trollo.domain.comment.dto.req;

import jakarta.validation.constraints.Size;

public record CommentSaveReq(
    @Size(max = 500)
    String content,
    Long userid,
    Long cardId

) {

}
