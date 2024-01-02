package org.nbc.account.trollo.domain.invitation.repository;

import java.util.List;
import java.util.Optional;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.invitation.entity.Invitation;
import org.nbc.account.trollo.domain.invitation.entity.InvitationId;
import org.nbc.account.trollo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {

    Optional<Invitation> findInvitationByIdReceiverAndIdBoard(User receiver, Board board);

    Optional<List<Invitation>> findAllByIdReceiver(User receiver);

    boolean existsByIdReceiverAndIdBoard(User receiver, Board board);

    Optional<List<Invitation>> findAllBySender(User sender);

}
