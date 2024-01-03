package org.nbc.account.trollo.domain.invitation.dto.response;

import java.util.List;

public record InvitationsRes(List<ReceivedInvitationRes> receivedInvitations, List<SentInvitationRes> sentInvitations) {

}
