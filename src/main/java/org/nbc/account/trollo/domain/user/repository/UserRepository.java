package org.nbc.account.trollo.domain.user.repository;

import java.util.Optional;
import org.nbc.account.trollo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findUserById(Long id);

    User findByNickname(String nickname);

}
