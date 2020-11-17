package com.findork.pdm.security.argumentConfig;

import com.findork.pdm.features.account.User;
import com.findork.pdm.security.userdetails.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.transaction.Transactional;

@Component
@Transactional
@Slf4j
@AllArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return is(parameter, User.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        // todo I know...stupid, we already have current user...why are we looking into the db again?
        //  I don't fucking know why we lose the transaction after we call findByUsername inside JwtFilter
        //  loadUserByUsername inside account service looses the transaction after return so here we get it again
        //  this will only apply when we expect current user to be used as argument inside controller methods
        //  fuck!
        // ----------------
        // todo update 1
        // inside JwtFilter we should not lookup the database. We should construct the principal from the jwt token
        // and only use those details to pull the user from db when needed. This way we avoid hitting the DB
        // each time we get a request and instead we use the code in this class to pull the user whenever we need his
        // details.
        // ----------------
        // todo update 2
        // SecurityContextHolder should only hold the user id,email or username. Using that, you can pull from
        // database all the other details when you need. You could also store entire object in SecurityContextHolder
        // but do you want to do that? first of all to do that you must access the database in the JWT token interceptor
        // for each request! Second, you don't need the user all the times. It is better approach to only inject the user
        // when the Account is specified as a method parameter and read it here and I the transaction also holds from this moment
        // on
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication.getPrincipal() instanceof UserPrincipal) { // logged in / user has token
            var principal = (UserPrincipal) authentication.getPrincipal();
            if (is(parameter, User.class)) {
                log.info(principal.getUsername());
                return principal.getUser();
            } else if (is(parameter, UserPrincipal.class)) {
                return principal;
            }
            log.info("acvv");
            throw new RuntimeException("Invalid parameter");
        }
        // we want to inject account/jwtuserprincipal but the token was not set in jwtFilter because the user is anonymous
        // and doesn't have one
        return null;
    }

    private boolean is(MethodParameter parameter, Class aClass) {
        return parameter.getParameterType().equals(aClass);
    }
}
