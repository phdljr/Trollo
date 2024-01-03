package org.nbc.account.trollo.domain.card.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.nbc.account.trollo.domain.checklist.dto.response.CheckListResponseDto;
import org.nbc.account.trollo.domain.comment.dto.res.CommentReadResponseDto;
import org.nbc.account.trollo.domain.worker.dto.response.WorkerResponseDto;

@Builder
public record CardReadDetailResponseDto(
    Long id,
    String title,
    String content,
    String color,
    LocalDateTime deadline,
    List<CheckListResponseDto> checkList,
    Float rate,
    List<WorkerResponseDto> workers,
    List<CommentReadResponseDto> comments
) {

}
