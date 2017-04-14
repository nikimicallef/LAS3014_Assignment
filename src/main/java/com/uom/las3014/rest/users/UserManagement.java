package com.uom.las3014.rest.users;

import com.uom.las3014.api.UserCredentialsBody;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserManagement {
    @Autowired
    private UsersDaoRepository usersDaoRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewUser(final @RequestBody @Valid UserCredentialsBody userCredentialsBody) {
        final User user = usersDaoRepository.findUsersByUsername(userCredentialsBody.getUsername());

        if (user != null) {
            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("error", "Account with that username already exists.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        } else {
            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("message", "Created successfully. Login to get your token and use your newly created account.");

            final User newUser = new User(userCredentialsBody.getUsername(), userCredentialsBody.getPassword());
            usersDaoRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    @RequestMapping(value = "/login")
    public ResponseEntity loginAndGenerateToken(final @RequestBody @Valid UserCredentialsBody userCredentialsBody) {
        final User user = usersDaoRepository.findUsersByUsername(userCredentialsBody.getUsername());

        if (user != null) {
            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("error", "Invalid credentials.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        } else {
            //check if user has a valid token
            //if(user.getSession_token() != null && user.get)
            final UUID uuid = UUID.randomUUID();
            final String sessionToken = uuid.toString();

            final Date sessionTokenCreated = new Date(System.currentTimeMillis());

            user.setSession_token(sessionToken);
            user.setSession_token_created(sessionTokenCreated);

            usersDaoRepository.save(user);

            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("message", "Created successfully.");
            jsonBodyKeyValuePair.put("session_token", sessionToken);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }

    @RequestMapping(value = "/{sessionToken}/logout")
    public ResponseEntity logoutUser(final @PathVariable String sessionToken) {
        final User user = usersDaoRepository.findUsersBySession_token(sessionToken);

        if (user != null) {
            user.setSession_token(null);
            user.setSession_token_created(null);
            user.setSession_token_last_used(null);

            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("message", "Logged out successfully.");

            usersDaoRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        } else {
            final Map<String, String> jsonBodyKeyValuePair = new HashMap<>();
            jsonBodyKeyValuePair.put("error", "No user exists with the provided session token.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Resources.jsonMessageBuilder(jsonBodyKeyValuePair));
        }
    }
}
