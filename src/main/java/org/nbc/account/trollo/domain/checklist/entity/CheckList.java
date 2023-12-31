package org.nbc.account.trollo.domain.checklist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nbc.account.trollo.domain.card.entity.Card;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CHECKLIST")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private boolean checkSign;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Builder
    public CheckList(Card card, String description, boolean checkSign) {
        this.description = description;
        this.checkSign = checkSign;
        this.card = card;
    }

    public void update(String description, boolean checkSign) {
        this.description = description;
        this.checkSign = checkSign;
    }
}
