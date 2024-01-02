package org.nbc.account.trollo.domain.section.entity;

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
import org.nbc.account.trollo.domain.board.entity.Board;

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
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "prev_section_id")
    private Section prevSection;

    @ManyToOne
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

    public void update(String name) {
        this.name = name;
    }

}
