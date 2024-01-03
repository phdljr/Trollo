package org.nbc.account.trollo.global.security;

import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email + " is not found"));
        return new UserDetailsImpl(user);
    }

}
