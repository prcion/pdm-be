package com.findork.pdm.features.account.mapper;

import com.findork.pdm.features.account.AccountStatus;
import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.role.RoleService;
import com.findork.pdm.security.payload.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;


    @Override
    public User signUpRequestToUser(SignUpRequest signUpRequest) {
        User user = userMapper.signUpRequestToUser(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setRoles(Collections.singleton(roleService.getUserRole()));
        return user;
    }
}
