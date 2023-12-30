package org.nbc.account.trollo.domain.comment.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    CommentEntity findByCommentId(Long commentId);

    CommentEntity findByCommentIdAndUserNickname(Long commentId, String nickname);

}
