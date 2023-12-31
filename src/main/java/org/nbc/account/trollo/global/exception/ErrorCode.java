package org.nbc.account.trollo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    BAD_LOGIN(HttpStatus.BAD_REQUEST, "이메일 또는 패스워드를 확인해주세요."),
    BAD_FORM(HttpStatus.BAD_REQUEST, "입력 형식이 맞지 않습니다."),
    INVALID_PASSWORD_CHECK(HttpStatus.BAD_REQUEST, "password check가 password와 일치하지 않습니다."),
    SELF_CANNOT_BE_INVITED(HttpStatus.BAD_REQUEST,"자기 자신은 보드에 초대될 수 없습니다."),
    ALREADY_EXIST_INVITATION(HttpStatus.BAD_REQUEST,"이미 초대된 사용자입니다."),
    ONLY_PARTICIPANTS_CAN_INVITE(HttpStatus.BAD_REQUEST,"보드 구성원만 다른 사람을 초대할 수 있습니다."),

    // 403
    FORBIDDEN_ACCESS_CARD(HttpStatus.FORBIDDEN, "해당 카드에 접근할 수 없습니다."),
    FORBIDDEN_ACCESS_BOARD(HttpStatus.FORBIDDEN, "해당 보드에 접근할 수 없습니다."),

    // 404
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾지 못하였습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "보드를 찾지 못하였습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾지 못하였습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일을 찾지 못하였습니다."),
    NOT_FOUND_CHECKLIST(HttpStatus.NOT_FOUND, "체크리스트를 찾지 못하였습니다."),
    NOT_FOUND_USER_BOARD(HttpStatus.NOT_FOUND, "해당 보드에 속하지 않은 유저입니다."),
    NOT_FOUND_SECTION(HttpStatus.NOT_FOUND, "색션을 찾지 못하였습니다."),
    NOT_FOUND_SECTION_IN_BOARD(HttpStatus.NOT_FOUND, "해당 보드에 속하지 않은 색션입니다."),
    NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "카드를 찾지 못하였습니다."),
    NOT_FOUND_INVITATION(HttpStatus.NOT_FOUND, "초대가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}