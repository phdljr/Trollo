package org.nbc.account.trollo.domain.board.combine;

import org.nbc.account.trollo.domain.board.dto.BoardReadResponseDto;
import org.nbc.account.trollo.domain.user.entity.User;

public interface BoardCombine {

    BoardReadResponseDto getCardAndSectionInBoard(Long boardId, User user);

}
