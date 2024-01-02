package org.nbc.account.trollo.domain.invitation.controller;

import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.invitation.dto.response.InvitationsRes;
import org.nbc.account.trollo.domain.invitation.dto.response.UserBoardRes;
import org.nbc.account.trollo.domain.invitation.service.InvitationService;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class InvitationController {

    private final InvitationService invitationService;

    // invite user to a board
    @PostMapping("/boards/{boardId}/invite/{userId}")
    public ApiResponse<Void> invite(
        @PathVariable(name = "boardId") Long boardId,
        @PathVariable(name = "userId") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        invitationService.createInvitation(boardId, userId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "successfully invited user");
    }

    // view invitations I got
    @GetMapping("/invitations")
    public ApiResponse<InvitationsRes> getInvitations(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ApiResponse<>(HttpStatus.OK.value(), "내가 받은/보낸 초대 조회",
            invitationService.getInvitations(userDetails.getUser()));
    }

    @PutMapping("/boards/{boardId}/approve")
    public ApiResponse<UserBoardRes> approveInvitation(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "boardId") Long boardId) {

        return new ApiResponse<>(HttpStatus.OK.value(), "초대 수락 성공",
            invitationService.approveInvitation(boardId, userDetails.getUser()));
    }

    @PutMapping("/boards/{boardId}/reject")
    public ApiResponse<Void> rejectInvitation(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "boardId") Long boardId) {

        invitationService.rejectInvitation(boardId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "초대 거절 성공");
    }

}
