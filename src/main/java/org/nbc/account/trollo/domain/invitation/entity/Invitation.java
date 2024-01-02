package org.nbc.account.trollo.domain.invitation.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_INVITATION")
public class Invitation {

    @EmbeddedId
    private InvitationId id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Builder
    public Invitation(User receiver, User sender, Board board) {
        this.id = new InvitationId();
        id.setReceiver(receiver);
        id.setBoard(board);
        this.sender = sender;
    }

}
