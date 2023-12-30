package org.nbc.account.trollo.domain.checklist.service;

import org.nbc.account.trollo.domain.checklist.dto.request.CheckListRequestDto;
import org.nbc.account.trollo.global.security.UserDetailsImpl;

public interface CheckListService {

    void createList(Long cardId, CheckListRequestDto requestDto, UserDetailsImpl userDetails);

    void updateCheckList(Long cardId, Long checkListId, CheckListRequestDto requestDto, UserDetailsImpl userDetails);
}
