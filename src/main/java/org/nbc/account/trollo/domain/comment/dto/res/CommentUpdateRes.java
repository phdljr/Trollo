package org.nbc.account.trollo.domain.comment.dto.res;

import jakarta.validation.constraints.Size;

public record CommentUpdateRes(
    @Size(max = 500)
    String content
) {

}
