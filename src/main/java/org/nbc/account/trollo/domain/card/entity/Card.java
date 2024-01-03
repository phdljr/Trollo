package org.nbc.account.trollo.domain.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.checklist.entity.CheckList;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.nbc.account.trollo.domain.section.entity.Section;
import org.nbc.account.trollo.domain.worker.entity.Worker;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_CARD")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    private String color;

    private LocalDateTime deadline;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "prev_card_id")
    private Card prevCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "next_card_id")
    private Card nextCard;

    @OneToMany(mappedBy = "card")
    private final List<CheckList> checkList = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private final List<Worker> workers = new ArrayList<>();

    @Builder
    public Card(final String title, final String content, final String color,
        final LocalDateTime deadline, final Section section,
        final Card prevCard, final Card nextCard) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.deadline = deadline;
        this.section = section;
        this.prevCard = prevCard;
        this.nextCard = nextCard;
    }

    public void setNextCard(Card card) {
        this.nextCard = card;
    }

    public void setPrevCard(Card card) {
        this.prevCard = card;
    }

    public void update(String title, String content, String color, LocalDateTime deadline) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.deadline = deadline;
    }

    public void changeSequence(final Card toCard, final SequenceDirection direction) {
        if (prevCard != null) {
            prevCard.setNextCard(nextCard);
        }
        if (nextCard != null) {
            nextCard.setPrevCard(prevCard);
        }

        switch (direction) {
            case PREVIOUS:
                Card toCardPrevCard = toCard.getPrevCard();
                if (toCardPrevCard != null) {
                    toCardPrevCard.setNextCard(this);
                }
                toCard.setPrevCard(this);
                this.setNextCard(toCard);
                this.setPrevCard(toCardPrevCard);
                break;
            case NEXT:
                Card toCardNextCard = toCard.getNextCard();
                if (toCardNextCard != null) {
                    toCardNextCard.setPrevCard(this);
                }
                toCard.setNextCard(this);
                this.setPrevCard(toCard);
                this.setNextCard(toCardNextCard);
                break;
        }
    }

    public void changeSection(final Section section) {
        if (prevCard != null) {
            prevCard.setNextCard(nextCard);
        }
        if (nextCard != null) {
            nextCard.setPrevCard(prevCard);
        }

        this.setPrevCard(null);
        this.setNextCard(null);

        this.section = section;
    }
}
