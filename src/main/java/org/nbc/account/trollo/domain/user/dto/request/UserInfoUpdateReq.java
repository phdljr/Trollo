package org.nbc.account.trollo.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoUpdateReq(
    @NotBlank(message = "nickname can't be null")
    String nickname,

    @NotNull(message = "password can't be null")
    String password
) {

}
