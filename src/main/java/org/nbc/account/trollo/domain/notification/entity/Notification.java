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
import org.nbc.account.trollo.domain.card.entity.Card;
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
    private NotificationEnum fieldName;

    @Column
    private String fieldContent;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Notification(CardEvent event) {
        this.fieldName = event.notificationEnum();
        this.card = event.card();
        this.createdAt = LocalDateTime.now();
        this.fieldContent =
            event.card().getId() + "번 카드" + event.notificationEnum().getWord() + "되었습니다.";
    }
}
