package org.nbc.account.trollo.domain.comment.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    CommentEntity findByCommentId(Long commentId);

    CommentEntity findByCommentIdAndUserNickname(Long commentId, String nickname);

    List<CommentEntity> findByUserNickname(String nickname);
}
