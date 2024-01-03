package org.nbc.account.trollo.domain.invitation.service;

import org.nbc.account.trollo.domain.invitation.dto.response.InvitationsRes;
import org.nbc.account.trollo.domain.user.entity.User;

public interface InvitationService {

    void createInvitation(Long boardId, Long userId, User user);

    InvitationsRes getInvitations(User user);

    void approveInvitation(Long boardId, User user);

    void rejectInvitation(Long boardId, User user);

    void cancelInvitation(Long boardId, Long userId, User user);

}
