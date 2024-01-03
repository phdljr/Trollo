package org.nbc.account.trollo.domain.comment.dto.res;

import jakarta.validation.constraints.Size;

public record CommentSaveRes(
    @Size(max = 500)
    String nickname,
    @Size(max = 500)
    String content
) {

}
