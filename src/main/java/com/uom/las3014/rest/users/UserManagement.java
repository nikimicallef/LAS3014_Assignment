package com.uom.las3014.rest.users;

import com.uom.las3014.api.UserCredentialsBody;
import com.uom.las3014.dao.User;
import com.uom.las3014.resources.Resources;
import com.uom.las3014.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

    //TODO: Refactor logout to use user service

//    @RequestMapping(value = "/logout")
//    public ResponseEntity logoutUser(final @RequestHeader String sessionToken) {
//        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);
//
//        if (user != null) {
//            user.setSessionToken(null);
//            user.setSessionTokenCreated(null);
//            user.setSessionTokenLastUsed(null);
//
//            jsonBodyKeyValuePair.put("message", "Logged out successfully.");
//
//            usersDaoRepository.save(user);
//
//            return ResponseEntity.status(HttpStatus.OK)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
//        } else {
//            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
//            jsonBodyKeyValuePair.put("error", "No user exists with the provided session token.");
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
//        }
//    }
}
