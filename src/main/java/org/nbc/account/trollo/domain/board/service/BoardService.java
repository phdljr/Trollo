package org.nbc.account.trollo.domain.board.service;

import java.util.List;
import org.nbc.account.trollo.domain.board.dto.BoardListResponseDto;
import org.nbc.account.trollo.domain.board.dto.BoardRequestDto;
import org.nbc.account.trollo.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {

    void createBoard(final BoardRequestDto boardRequestDto, User user);

    void updateBoard(Long boardId, final BoardRequestDto boardRequestDto, User user);

    void deleteBoard(Long boardId, User user);

    List<BoardListResponseDto> mainBoard(User user);
}
