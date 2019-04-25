package org.thingsboard.server.fs.service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class CustomerAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final String token;

    public CustomerAuthenticationToken(String principal){
        super(null);
        this.token = principal;
    }

//    public CustomerAuthenticationToken(Object principal,Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//        this.token = (String) principal;
//    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }
}
