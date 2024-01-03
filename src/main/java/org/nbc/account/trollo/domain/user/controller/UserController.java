package org.nbc.account.trollo.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.user.dto.request.LoginReq;
import org.nbc.account.trollo.domain.user.dto.request.PasswordUpdateReq;
import org.nbc.account.trollo.domain.user.dto.request.SignupReq;
import org.nbc.account.trollo.domain.user.dto.request.UserInfoUpdateReq;
import org.nbc.account.trollo.domain.user.dto.response.MyPageRes;
import org.nbc.account.trollo.domain.user.service.UserService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // sign up
    @PostMapping("/signup")
    public ApiResponse<Void> signup(
        @Valid @RequestBody SignupReq signupReq) {

        userService.signup(signupReq);
        // if succeeded
        return new ApiResponse<>(HttpStatus.CREATED.value(), "회원가입 성공");
    }

    // login
    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody LoginReq loginReq,
        HttpServletResponse response) {

        userService.login(loginReq, response);
        return new ApiResponse<>(HttpStatus.OK.value(), "로그인 성공");
    }

    // my page
    @GetMapping("/me")
    public ApiResponse<MyPageRes> mypage(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ApiResponse<>(HttpStatus.OK.value(), "마이페이지 조회 성공",
            userService.mypage(userDetails.getUser()));
    }

    //update personal informations
    @PutMapping("/edit")
    public ApiResponse<Void> updateInfo(
        @Valid @RequestBody UserInfoUpdateReq updateReq,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.updateInfo(updateReq, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "개인정보 업데이트 성공");
    }

    //update password
    @PutMapping("/pw")
    public ApiResponse<Void> updatePassword(
        @Valid @RequestBody PasswordUpdateReq updateReq,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.updatePassword(updateReq, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "비밀번호 업데이트 성공");
    }

    //delete my account
    @DeleteMapping("/withdraw")
    public ApiResponse<Void> deleteAccount(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.deleteAccount(userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "회원 탈퇴 성공");
    }

    //log out
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        HttpServletResponse response) {

        userService.logout(response);
        return new ApiResponse<>(HttpStatus.OK.value(), "로그아웃 성공");
    }

}
