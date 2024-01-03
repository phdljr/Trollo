package org.nbc.account.trollo.domain.checklist.controller;

import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.checklist.dto.request.CheckListRequestDto;
import org.nbc.account.trollo.domain.checklist.service.CheckListService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cards")
public class CheckListController {

    private final CheckListService checkListService;

    @PostMapping("/{cardId}/checklists")
    public ApiResponse<Void> createList(
        @PathVariable Long cardId,
        @RequestBody CheckListRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkListService.createList(cardId, requestDto, userDetails);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "체크리스트 생성");
    }

    @PutMapping("/{cardId}/checklists/{checkListId}")
    public ApiResponse<Void> updateCheckList(
        @PathVariable Long cardId,
        @PathVariable Long checkListId,
        @RequestBody CheckListRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkListService.updateCheckList(cardId, checkListId, requestDto, userDetails);
        return new ApiResponse<>(HttpStatus.OK.value(), "체크리스트 수정");
    }

    @PutMapping("/{cardId}/checklists/{checkListId}/check")
    public ApiResponse<Void> checkUncheck(
        @PathVariable Long cardId,
        @PathVariable Long checkListId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkListService.checkUncheck(cardId,checkListId,userDetails);
        return new ApiResponse<>(HttpStatus.OK.value(), "확인/취소 변경 완료");
    }

    @DeleteMapping("/{cardId}/checklists/{checkListId}")
    public ApiResponse<Void> deleteCheckList(
        @PathVariable Long cardId,
        @PathVariable Long checkListId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkListService.deleteCheckList(cardId, checkListId, userDetails);
        return new ApiResponse<>(HttpStatus.OK.value(), "체크리스트 삭제");
    }

}
