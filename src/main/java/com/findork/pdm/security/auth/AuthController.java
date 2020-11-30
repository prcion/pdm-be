package com.findork.pdm.security.auth;

import com.findork.pdm.features.account.User;
import com.findork.pdm.features.account.UserRepository;
import com.findork.pdm.features.account.mapper.UserMapper;
import com.findork.pdm.security.constants.AuthConstants;
import com.findork.pdm.security.handler.RequestHandler;
import com.findork.pdm.security.jwt.JwtTokenProvider;
import com.findork.pdm.security.payload.*;
import com.findork.pdm.security.userdetails.CustomUserDetailsService;
import com.findork.pdm.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Auth Controller, An entry class for all incoming requests
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RequestHandler requestHandler;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final HttpResponseUtil httpResponseUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    /**
     * Validate the credentials and generate the jwt tokens
     *
     * @return access token and refresh token
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {

        var userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());

        authenticate(loginRequest.getUsername(), loginRequest.getPassword(), userDetails.getAuthorities());


        var refreshJwt = tokenProvider.generateRefreshToken(userDetails);
        var accessJwt = tokenProvider.generateAccessToken(userDetails);
        var user = userRepository.findByUsernameOrEmail(loginRequest.getUsername(), loginRequest.getUsername());
        return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(accessJwt).refreshToken(refreshJwt).userId(user.get().getId()).build());
    }

    private void authenticate(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, authorities));
    }

    /**
     * Validate the refresh token and generate access token
     *
     * @return access token
     */
    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader(AuthConstants.AUTH_KEY) String authRefreshToken) {
        System.out.println(authRefreshToken);
        try {
            if (StringUtils.hasText(authRefreshToken)) {

                String refreshJwt = requestHandler.getJwtFromStringRequest(authRefreshToken);
                String userName = tokenProvider.getUserNameFromJWT(refreshJwt);

                var user = customUserDetailsService.loadUserByUsername(userName);

                String accessJwtToken = tokenProvider.generateAccessToken(user);

                return ResponseEntity.ok(RefreshJwtAuthenticationResponse.builder().accessToken(accessJwtToken).build());
            } else
                return ResponseEntity.ok(httpResponseUtil.createHttpResponse(BAD_REQUEST, AuthConstants.EMPTY_TOKEN));
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context" + ex.getMessage());
            return ResponseEntity.ok(httpResponseUtil.createHttpResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()));
        }
    }

    /**
     * Validate the access token and returns user details
     *
     * @return status of token
     */
    @GetMapping("/userDetails")
    public ResponseEntity<?> details(@RequestHeader(AuthConstants.AUTH_KEY) String authToken) {
        String jwt = requestHandler.getJwtFromStringRequest(authToken);
        UserDetailsResponse response = tokenProvider.getUserNameAndRolesFromJWT(jwt);
        return ResponseEntity.ok(response);
    }

    /**
     * This is for user registration
     *
     * @return user registration status
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.verifyIfUsernameOrEmailExists(signUpRequest.getUsername(), signUpRequest.getEmail());

        User user = userMapper.signUpRequestToUser(signUpRequest);

        authService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(httpResponseUtil.createHttpResponse(HttpStatus.CREATED, "User registered successfully"));
    }

}
