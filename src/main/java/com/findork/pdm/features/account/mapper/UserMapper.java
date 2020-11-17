package com.findork.pdm.features.account.mapper;

import com.findork.pdm.features.account.User;
import com.findork.pdm.security.payload.SignUpRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    User signUpRequestToUser(SignUpRequest signUpRequest);

}
