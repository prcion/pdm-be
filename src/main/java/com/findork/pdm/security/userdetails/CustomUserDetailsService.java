package com.findork.pdm.security.userdetails;

import com.findork.pdm.features.account.AccountStatus;
import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.UserRepository;
import com.findork.pdm.util.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * creating custom user details service from spring security's UserDetailsService interface
 */
@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    /**
     * Get userName from database and create a user principal
     *
     * @return user principal details
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
        validateLoginAttempt(user);
        return new UserPrincipal(user);
    }


    private void validateLoginAttempt(User user) {
        if(!user.isLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setAccountStatus(AccountStatus.LOCKED);
            } else {
                user.setAccountStatus(AccountStatus.ACTIVE);
            }
        } else {
            if (!loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setAccountStatus(AccountStatus.ACTIVE);
            }
        }
    }
}
