package org.thingsboard.server.fs.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthTokenProvider implements AuthenticationProvider {

    @Autowired
    TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated())
        {
            return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null, new ArrayList<>());
        }
        String token = (String) authentication.getPrincipal();
        if (token != null)
        {
            if (!tokenService.checkToken(token))
            {
                throw new CredentialsExpiredException("Access Token is expired. Please login again.");
            }
        }
        else
        {
            throw new BadCredentialsException("Invalid token String.");
        }
        return new UsernamePasswordAuthenticationToken(token, null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
