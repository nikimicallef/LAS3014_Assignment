package com.uom.las3014.service;

import com.uom.las3014.api.UserCredentialsBody;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity createNewUser(final UserCredentialsBody userCredentialsBody);

    ResponseEntity loginAndGenerateToken(final UserCredentialsBody userCredentialsBody);
}
