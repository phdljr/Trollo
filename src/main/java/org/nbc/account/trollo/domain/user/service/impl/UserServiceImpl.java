package org.nbc.account.trollo.domain.user.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.user.dto.request.LoginReq;
import org.nbc.account.trollo.domain.user.dto.request.SignupReq;
import org.nbc.account.trollo.domain.user.dto.response.MypageRes;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.user.exception.UserDomainException;
import org.nbc.account.trollo.domain.user.repository.UserRepository;
import org.nbc.account.trollo.domain.user.service.UserService;
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
  public MypageRes mypage(User user) {
        return new MypageRes(user.getEmail(),user.getNickname());
  }

}
