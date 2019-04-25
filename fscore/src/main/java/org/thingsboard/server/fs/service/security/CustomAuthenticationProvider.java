package org.thingsboard.server.fs.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.isAuthenticated()){
            return authentication;
        }

        CustomerAuthenticationToken customerAuthenticationToken = (CustomerAuthenticationToken) authentication;
        String token = (String) customerAuthenticationToken.getPrincipal();
//        if(!StringUtils.isNotEmpty(token)){
//            return authentication;
//        }
        if(token==null||!token.equals("testToken")){
//            throw new InternalAuthenticationServiceException("Invalid Token");
            UserDetails user = User.builder()
                    .username("api1")
                    .password("")
                    .authorities(Role.USER)
                    .build();
            // 返回新的认证信息，带上 token 和反查出的用户信息
            Authentication auth = new PreAuthenticatedAuthenticationToken(user, token, user.getAuthorities());
            auth.setAuthenticated(true);
            return auth;
        }

        UserDetails user = User.builder()
                .username("api")
                .password("")
                .authorities(Role.API)
                .build();
        // 返回新的认证信息，带上 token 和反查出的用户信息
        Authentication auth = new PreAuthenticatedAuthenticationToken(user, token, user.getAuthorities());
        auth.setAuthenticated(true);
        return auth;
//        if (shouldAuthenticateAgainstThirdPartySystem()) {
//
//            // use the credentials
//            // and authenticate against the third-party system
//            return new UsernamePasswordAuthenticationToken(
//                    name, password, new ArrayList<>());
//        } else {
//            return null;
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
//        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
        return (CustomerAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
