package com.findork.pdm.security.handler;

import com.findork.pdm.exception.AppException;
import com.findork.pdm.security.constants.AuthConstants;
import com.findork.pdm.security.constants.ErrorCodes;
import com.findork.pdm.security.payload.LoginRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * A handler class responsible to manipulate the request
 */
@Component
public class RequestHandler {

    /**
     * Decoding user credentials
     *
     * @return LoginRequest with user name and password
     */
    public LoginRequest decodeCredentials(String authCredentials) {
        System.out.println(authCredentials);
        String[] authParts = authCredentials.split("\\s+");
        String authInfo = authParts[0];
        String[] credentials = authInfo.split(":");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(credentials[0]);
        loginRequest.setPassword(credentials[1]);
        return loginRequest;
    }

    /**
     * Get jwt from request
     *
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTH_KEY);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new AppException("Jwt is empty or Bearer missing", ErrorCodes.UNNAUTHORIZED.toString());
    }

    /**
     * Get jwt from string request
     *
     * @return JWT
     */
    public String getJwtFromStringRequest(String request) {
        if (StringUtils.hasText(request) && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        throw new AppException("Jwt is empty or Bearer missing", ErrorCodes.UNNAUTHORIZED.toString());
    }
}
