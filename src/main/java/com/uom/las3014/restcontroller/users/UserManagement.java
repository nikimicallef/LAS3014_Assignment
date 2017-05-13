package com.uom.las3014.restcontroller.users;

import com.uom.las3014.annotations.AuthBySessionToken;
import com.uom.las3014.api.UserCreateBody;
import com.uom.las3014.api.UserLoginBody;
import com.uom.las3014.api.UserTopicsBody;
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
    public ResponseEntity createNewUser(final @RequestBody @Valid UserCreateBody userCreateBody) {
        return userService.createNewUser(userCreateBody);
    }

    @RequestMapping(value = "/login")
    public ResponseEntity loginAndGenerateToken(final @RequestBody @Valid UserLoginBody userLoginBody) {
        return userService.loginAndGenerateToken(userLoginBody);
    }

    @AuthBySessionToken
    @RequestMapping(value = "/logout")
    public ResponseEntity logoutUser(final @RequestHeader(name = "X-SessionToken") String sessionToken) {
        return userService.logout(sessionToken);
    }

    @AuthBySessionToken
    @RequestMapping(value = "/topics", method = RequestMethod.PUT)
    public ResponseEntity changeInterestedTopics(final @RequestHeader(name = "X-SessionToken") String sessionToken, final @RequestBody @Valid UserTopicsBody userTopicsBody) {
        return userService.changeInterestedTopics(sessionToken, userTopicsBody.getAdditions(), userTopicsBody.getRemovals());
    }
}
