package com.uom.las3014.service;

import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity createNewUser(final UserCreateBody userCreateBody);

    ResponseEntity loginAndGenerateToken(final UserLoginBody userLoginBody);

    ResponseEntity logout(final String sessionToken);
}
