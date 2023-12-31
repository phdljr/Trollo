package org.nbc.account.trollo.domain.comment.repository;

import java.util.List;
import org.nbc.account.trollo.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByCommentId(Long commentId);

    Comment findByCommentIdAndUserNickname(Long commentId, String nickname);

    List<Comment> findByUserNickname(String nickname);
}
