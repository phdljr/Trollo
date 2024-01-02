package org.nbc.account.trollo.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserInfoUpdateReq(
    @NotBlank(message = "nickname can't be null")
    String nickname,
    String password
) {

}
