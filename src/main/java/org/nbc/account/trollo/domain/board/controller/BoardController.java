package org.nbc.account.trollo.domain.board.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.combine.BoardCombine;
import org.nbc.account.trollo.domain.board.dto.BoardListResponseDto;
import org.nbc.account.trollo.domain.board.dto.BoardReadResponseDto;
import org.nbc.account.trollo.domain.board.dto.BoardRequestDto;
import org.nbc.account.trollo.domain.board.service.BoardService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardCombine boardCombine;

    @GetMapping //메인페이지(현재 사용자 보드 전체조회) 유저정보를 사용할수 없어서 안됨.
    public ApiResponse<List<BoardListResponseDto>> mainBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardListResponseDto> responseDto = boardService.mainBoard(userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "메인 페이지", responseDto);
    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardReadResponseDto> getBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardReadResponseDto responseDto = boardCombine.getCardAndSectionInBoard(boardId,
            userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "보드 단일 조회", responseDto);
    }

    @PostMapping//보드 생성
    public ApiResponse<Void> createBoard(
        @RequestBody BoardRequestDto boardRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.createBoard(boardRequestDto, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "보드 생성");
    }

    @PutMapping("/{boardId}")//보드 수정
    public ApiResponse<Void> updateBoard(
        @PathVariable Long boardId,
        @RequestBody BoardRequestDto boardRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.updateBoard(boardId, boardRequestDto, userDetails.getUser());

        return new ApiResponse<>(HttpStatus.OK.value(), "보드 수정");
    }

    @DeleteMapping("/{boardId}")//보드 삭제
    public ApiResponse<Void> deleteBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "보드 삭제");

    }


}

