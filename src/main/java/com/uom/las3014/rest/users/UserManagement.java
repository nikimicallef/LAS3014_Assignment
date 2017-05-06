package com.uom.las3014.rest.users;

import com.uom.las3014.api.UserCredentialsBody;
import com.uom.las3014.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserManagement {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewUser(final @RequestBody @Valid UserCredentialsBody userCredentialsBody) {
        return userService.createNewUser(userCredentialsBody);
    }

    @RequestMapping(value = "/login")
    public ResponseEntity loginAndGenerateToken(final @RequestBody @Valid UserCredentialsBody userCredentialsBody) {
        return userService.loginAndGenerateToken(userCredentialsBody);
    }

    @RequestMapping(value = "/logout")
    public ResponseEntity logoutUser(final @RequestHeader String sessionToken) {
        return userService.logout(sessionToken);
    }
}
