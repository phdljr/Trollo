package org.nbc.account.trollo.domain.comment.repository;

import java.util.List;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long commentId);

    Comment findCommentByIdAndUserId(Long commentId, Long userId);

    List<Comment> findByUserNickname(String nickname);

    List<Comment> findByCardId(Long cardId);
}
