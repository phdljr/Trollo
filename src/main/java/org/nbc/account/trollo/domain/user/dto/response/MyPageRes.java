package org.nbc.account.trollo.domain.user.dto.response;

import java.util.List;

public record MyPageRes(String email, String nickname, List<BoardRes> myBoards) {

}
