package org.nbc.account.trollo.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateReq(

    @NotNull(message = "new password can't be null")
    @Size(min = 4, max = 15, message = "pw should be longer than 4 and shorter than 15")
    @Pattern(regexp = "^[a-zA-Z_0-9]*$", message = "only alphabets and numbers are allowed for pw")
    String newPassword,

    @NotNull(message = "new password check can't be null")
    String newPasswordCheck,

    @NotNull(message = "password can't be null")
    String password
) {

}
