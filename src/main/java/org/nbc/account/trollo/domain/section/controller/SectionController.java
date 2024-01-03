package org.nbc.account.trollo.domain.section.controller;

import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.section.dto.SectionCreateRequestDto;
import org.nbc.account.trollo.domain.section.service.SectionService;
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
@RequestMapping("/api/v1")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/boards/{boardId}/sections")
    public ApiResponse<Void> createSection(
        @PathVariable Long boardId,
        @RequestBody SectionCreateRequestDto sectionCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sectionService.createSection(boardId, sectionCreateRequestDto, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "색션 생성");
    }

    @PutMapping("/sections/{sectionId}")
    public ApiResponse<Void> updateSection(
        @PathVariable Long sectionId,
        @RequestBody SectionCreateRequestDto sectionCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sectionService.updateSection(sectionId, sectionCreateRequestDto, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "색션 이름 수정");
    }

    @PutMapping("/sections/{fromSectionId}/to/{toSectionId}/{direction}")
    public ApiResponse<Void> changeCardSequence(
        @PathVariable Long fromSectionId,
        @PathVariable Long toSectionId,
        @PathVariable SequenceDirection direction,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sectionService.changeSectionSequence(fromSectionId, toSectionId, direction,
            userDetails.getUser());
        return new ApiResponse<>(HttpStatus.OK.value(), "색션 위치 변경");
    }

    @DeleteMapping("/sections/{sectionId}")
    public ApiResponse<Void> deleteSection(
        @PathVariable Long sectionId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sectionService.deleteSection(sectionId, userDetails.getUser());
        return new ApiResponse<>(HttpStatus.CREATED.value(), "색션 삭제");
    }


}

