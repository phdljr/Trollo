package org.nbc.account.trollo.domain.comment.dto.req;

import jakarta.validation.constraints.Size;

public record CommentUpdateReq(
    Long commentId,
    @Size(max = 500)
    String nickname,
    @Size(max = 500)
    String content
) {

}
