package com.uom.las3014.restcontroller;

import com.uom.las3014.annotations.AuthBySessionToken;
import com.uom.las3014.api.request.UserCreateRequestBody;
import com.uom.las3014.api.request.UserLoginRequestBody;
import com.uom.las3014.api.request.UserTopicsRequestBody;
import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.api.response.SessionTokenAndMessageResponse;
import com.uom.las3014.dao.User;
import com.uom.las3014.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GenericMessageResponse> createNewUser(final @RequestBody @Valid UserCreateRequestBody userCreateRequestBody) {
        return userService.createNewUser(userCreateRequestBody);
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<SessionTokenAndMessageResponse> loginAndGenerateToken(final @RequestBody @Valid UserLoginRequestBody userLoginRequestBody) {
        return userService.loginAndGenerateToken(userLoginRequestBody);
    }

    @AuthBySessionToken
    @RequestMapping(value = "/logout")
    public ResponseEntity<GenericMessageResponse> logoutUser(final @RequestHeader(name = "X-SessionToken") String sessionToken) {
        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);

        return userService.logout(user);
    }

    @AuthBySessionToken
    @RequestMapping(value = "/topics", method = RequestMethod.PUT)
    public ResponseEntity<GenericMessageResponse> changeInterestedTopics(final @RequestHeader(name = "X-SessionToken") String sessionToken, final @RequestBody @Valid UserTopicsRequestBody userTopicsRequestBody) {
        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);
        //TODO: Get user from pointcut
        return userService.changeInterestedTopics(user, userTopicsRequestBody.getAdditions(), userTopicsRequestBody.getRemovals());
    }
}
