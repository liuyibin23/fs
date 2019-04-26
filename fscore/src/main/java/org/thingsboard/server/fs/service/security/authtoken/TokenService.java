package org.thingsboard.server.fs.service.security.authtoken;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public boolean checkToken(String token){
        return token.equals("testToken");
    }

}
