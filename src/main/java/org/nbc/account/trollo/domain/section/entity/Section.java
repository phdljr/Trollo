package org.nbc.account.trollo.domain.section.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.card.converter.SequenceDirection;
import org.nbc.account.trollo.domain.card.entity.Card;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_SECTION")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "prev_section_id")
    private Section prevSection;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "next_section_id")
    private Section nextSection;

    @Builder
    public Section(final String name, final Board board,
        final Section prevSection, final Section nextSection) {
        this.name = name;
        this.board = board;
        this.prevSection = prevSection;
        this.nextSection = nextSection;
    }

    public void setNextSection(Section section) {
        this.nextSection = section;
    }

    public void setPrevSection(Section section) {
        this.prevSection = section;
    }

    public void changeSequence(final Section toSection, final SequenceDirection direction) {
        if (prevSection != null) {
            prevSection.setNextSection(nextSection);
        }
        if (nextSection != null) {
            nextSection.setPrevSection(prevSection);
        }

        switch (direction) {
            case PREVIOUS:
                Section toSectionPrevSection = toSection.getPrevSection();
                if (toSectionPrevSection != null) {
                    toSectionPrevSection.setNextSection(this);
                }
                toSection.setPrevSection(this);
                this.setNextSection(toSection);
                this.setPrevSection(toSectionPrevSection);
                break;
            case NEXT:
                Section toSectionNextSection = toSection.getNextSection();
                if (toSectionNextSection != null) {
                    toSectionNextSection.setPrevSection(this);
                }
                toSection.setNextSection(this);
                this.setPrevSection(toSection);
                this.setNextSection(toSectionNextSection);
                break;
        }
    }

    public void update(String name) {
        this.name = name;
    }

}
