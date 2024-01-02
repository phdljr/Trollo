package org.nbc.account.trollo.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.notification.event.CardEvent;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType fieldName;

    @Column
    private String fieldContent;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification(CardEvent event) {
        this.fieldName = event.notificationType();
        this.createdAt = LocalDateTime.now();
        this.board = event.board();
        this.user = event.user();
        this.fieldContent =
            event.board() + "번 BOARD에서 카드" + event.notificationType().getWord() + "되었습니다.";
    }
}
