package com.findork.pdm.security.auth;

import com.findork.pdm.exception.AlreadyExistsException;
import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.UserRepository;
import com.findork.pdm.security.constants.AuthConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;


    public void verifyIfUsernameOrEmailExists(String username, String email) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(AuthConstants.USERNAME_OR_EMAIL_EXIST);
        }
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByUsernameOrEmail(String userNameOrEmail) {
        return userRepository.findByUsernameOrEmail(userNameOrEmail, userNameOrEmail)
                .orElseThrow(() -> new AlreadyExistsException(AuthConstants.USERNAME_OR_EMAIL_EXIST));
    }
}
