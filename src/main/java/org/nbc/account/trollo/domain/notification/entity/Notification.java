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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.notification.event.CardEvent;
import org.nbc.account.trollo.domain.usernotification.entity.UserNotification;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification(CardEvent event) {
        this.fieldName = event.notificationType();
        this.createdAt = LocalDateTime.now();
        this.board = event.board();
        this.user = event.user();
        this.fieldContent =
            event.user().getNickname()+" 회원에 의해 "+event.board().getName() + " BOARD에서 카드" + event.notificationType().getWord() + "되었습니다.";
    }
}
