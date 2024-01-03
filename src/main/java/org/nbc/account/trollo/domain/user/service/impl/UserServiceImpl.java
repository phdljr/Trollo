package org.nbc.account.trollo.domain.user.service.impl;

import static org.nbc.account.trollo.global.jwt.JwtUtil.AUTHORIZATION_HEADER;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.user.dto.request.LoginReq;
import org.nbc.account.trollo.domain.user.dto.request.PasswordUpdateReq;
import org.nbc.account.trollo.domain.user.dto.request.SignupReq;
import org.nbc.account.trollo.domain.user.dto.request.UserInfoUpdateReq;
import org.nbc.account.trollo.domain.user.dto.response.BoardRes;
import org.nbc.account.trollo.domain.user.dto.response.MyPageRes;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.user.exception.UserDomainException;
import org.nbc.account.trollo.domain.user.repository.UserRepository;
import org.nbc.account.trollo.domain.user.service.UserService;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.nbc.account.trollo.global.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserBoardRepository userBoardRepository;

    public void signup(SignupReq signupReq) {

        String email = signupReq.email();
        String nickname = signupReq.nickname();
        String password = passwordEncoder.encode(signupReq.password());
        String passwordCheck = signupReq.passwordCheck();

        // check username duplication
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserDomainException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        // check password
        if (!passwordEncoder.matches(passwordCheck, password)) {
            throw new UserDomainException(ErrorCode.INVALID_PASSWORD_CHECK);
        }

        //register user
        User user = User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();

        userRepository.save(user);
    }

    public void login(LoginReq loginReq, HttpServletResponse response) {
        String email = loginReq.email();
        String password = loginReq.password();
        // find email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserDomainException(ErrorCode.BAD_LOGIN));
        // check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserDomainException(ErrorCode.BAD_LOGIN);
        }

        jwtUtil.addJwtToCookie(jwtUtil.createToken(loginReq.email()), response);
    }

    @Override
    public MyPageRes mypage(User user) {

        List<UserBoard> userboards = userBoardRepository.findAllByUser(user).orElse(null);

        List<BoardRes> boards = null;

        if (userboards != null) {
            boards = userboards
                .stream()
                .map(
                    (UserBoard userboard)
                        -> new BoardRes(
                        userboard.getBoard().getId(),
                        userboard.getBoard().getName(),
                        userboard.getBoard().getColor())).toList();
        }

        return new MyPageRes(user.getEmail(), user.getNickname(), boards);

    }

    @Override
    public void updateInfo(UserInfoUpdateReq updateReq, User user) {

        String nickname = updateReq.nickname();
        String password = updateReq.password();

        //if password is equal to the saved password, proceed
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserDomainException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(PasswordUpdateReq updateReq, User user) {

        String password = updateReq.password();
        String newPassword = passwordEncoder.encode(updateReq.newPassword());
        String newPasswordCheck = updateReq.newPasswordCheck();

        // check if new password matches with new password check
        if (!passwordEncoder.matches(newPasswordCheck, newPassword)) {
            throw new UserDomainException(ErrorCode.INVALID_PASSWORD_CHECK);
        }

        //if password is equal to the saved password, proceed
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserDomainException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        user.updatePassword(newPassword);
        userRepository.save(user);

    }

    @Override
    public void deleteAccount(User user) {
        userRepository.delete(user);
    }

    @Override
    public void logout(HttpServletResponse response) {

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

    }

}
