package org.nbc.account.trollo.domain.invitation.dto.response;

public record ReceivedInvitationRes(Long boardId, String boardname,
                                    Long senderId, String senderNick) {

}
