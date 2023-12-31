package org.nbc.account.trollo.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationEnum {
    CREATED("가 생성"),
    UPDATED("가 수정"),
    DELETED("가 삭제"),
    MOVED("가 이동"),
    ADD_COMMENTS("에 댓글이 작성");

    private final String word;

}
