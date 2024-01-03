package org.nbc.account.trollo.domain.invitation.dto.response;

public record SentInvitationRes(Long boardId, String boardname,
                                Long receiverId, String receiverNick) {

}
