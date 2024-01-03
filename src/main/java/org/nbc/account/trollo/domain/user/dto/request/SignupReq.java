package org.nbc.account.trollo.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupReq(@Email String email, String nickname,
                        @Size(min = 4, max = 15, message = "pw should be longer than 4 and shorter than 15")
                        @Pattern(regexp = "^[a-zA-Z_0-9]*$", message = "only alphabets and numbers are allowed for pw")
                        String password, String passwordCheck) {

}
