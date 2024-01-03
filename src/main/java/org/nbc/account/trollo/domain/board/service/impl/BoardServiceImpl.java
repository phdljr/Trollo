package org.nbc.account.trollo.domain.board.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.dto.BoardListResponseDto;
import org.nbc.account.trollo.domain.board.dto.BoardRequestDto;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.exception.NotFoundBoardException;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.board.service.BoardService;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;
import org.nbc.account.trollo.domain.userboard.exception.ForbiddenAccessBoardException;
import org.nbc.account.trollo.domain.userboard.exception.NotFoundUserBoardException;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.CustomException;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final UserBoardRepository userBoardRepository;


    @Override
    public void createBoard(BoardRequestDto boardRequestDto, User user) {

        Board board = Board.builder()
            .name(boardRequestDto.name())
            .color(boardRequestDto.color())
            .build();
        boardRepository.save(board);

        UserBoard creator = UserBoard.builder()
            .user(user)
            .board(board)
            .role(UserBoardRole.CREATOR)
            .build();
        userBoardRepository.save(creator);

    }

    @Override
    @Transactional
    public void updateBoard(Long boardId, BoardRequestDto boardRequestDto, User user) {

        Board board = checkUserInBoard(boardId, user.getId());

        board.update(boardRequestDto.name(), boardRequestDto.color());

    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId, User user) {

        Board board = checkUserInBoard(boardId, user.getId());

        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, user.getId())
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));

        if (!userBoard.getRole().equals(UserBoardRole.CREATOR)) {
            throw new NotFoundBoardException(ErrorCode.FORBIDDEN_ACCESS_BOARDCHANGE);
        }
        userBoardRepository.delete(userBoard);
        boardRepository.delete(board);

    }

    @Override
    public List<BoardListResponseDto> mainBoard(User user) {

        List<UserBoard> userboards = userBoardRepository.findAllByUser(user).orElse(null);

        List<BoardListResponseDto> boards = null;

        if(userboards!=null){
             boards = userboards
                .stream()
                .map(
                    (UserBoard userboard)
                -> new BoardListResponseDto(
                    userboard.getBoard().getId(),
                        userboard.getBoard().getName(),
                        userboard.getBoard().getColor())).toList();
        }

        return boards;
    }

    private Board checkUserInBoard(Long boardId, Long userId) {

        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new NotFoundUserBoardException(ErrorCode.NOT_FOUND_USER_BOARD));
        if (userBoard.getRole() == UserBoardRole.WAITING) {
            throw new ForbiddenAccessBoardException(ErrorCode.FORBIDDEN_ACCESS_BOARD);
        }
        return board;
    }

}

