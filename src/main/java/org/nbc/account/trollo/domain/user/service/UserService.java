package org.nbc.account.trollo.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import org.nbc.account.trollo.domain.user.dto.request.LoginReq;
import org.nbc.account.trollo.domain.user.dto.request.PasswordUpdateReq;
import org.nbc.account.trollo.domain.user.dto.request.SignupReq;
import org.nbc.account.trollo.domain.user.dto.request.UserInfoUpdateReq;
import org.nbc.account.trollo.domain.user.dto.response.MyPageRes;
import org.nbc.account.trollo.domain.user.entity.User;

public interface UserService {

    void signup(SignupReq signupReq);

    void login(LoginReq loginReq, HttpServletResponse response);

    MyPageRes mypage(User user);

    void updateInfo(UserInfoUpdateReq updateReq ,User user);

    void updatePassword(PasswordUpdateReq updateReq, User user);

    void deleteAccount(User user);

    void logout(HttpServletResponse response);

}
