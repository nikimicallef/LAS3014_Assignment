package com.uom.las3014.aop;

import com.uom.las3014.annotations.AuthBySessionToken;
import com.uom.las3014.dao.User;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * This aspect aims to catch methods which
 * a) Are within the {@link com.uom.las3014.restcontroller} package
 * b) The first parameter being annotated with {@link RequestHeader}
 * c) Annotated with the {@link AuthBySessionToken}
 *
 * The aim of this aspect is to check whether the provided session token in the {@link RequestHeader} is valid.
 * If the session token is not valid, an {@link InvalidCredentialsException} is thrown.
 * If the session token is valid then it changes the session token last used of the {@link User} pertaining to that session token.
 */
@Component
@Aspect
public class AuthSessionTokenAop {
    @Autowired
    private UserService userService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Pointcut("execution(* com.uom.las3014.restcontroller..*.*(@org.springframework.web.bind.annotation.RequestHeader (java.lang.String),..)) && args(sessionToken,..)")
    public void sessionTokenPointcut(final String sessionToken) {}

    /**
     * Check whether the provided session token in the {@link RequestHeader} is valid.
     * If the session token is not valid, an {@link InvalidCredentialsException} is thrown.
     * If the session token is valid then it changes the session token last used of the {@link User} pertaining to that session token.
     * @param sessionToken provided in as a {@link RequestHeader}
     */
    @Before("sessionTokenPointcut(sessionToken) && @annotation(com.uom.las3014.annotations.AuthBySessionToken)")
    public void sessionTokenBefore(final String sessionToken){
        logger.debug("Entered sessionTokenBefore AOP method for provided session token " + sessionToken);

        final User retrievedUser = userService.getUserFromDbUsingSessionToken(sessionToken);

        if (!retrievedUser.hasActiveSessionToken()){
            userService.invalidateSessionToken(retrievedUser);

            throw new InvalidCredentialsException("Invalid Credentials.");
        }

        userService.updateSessionTokenLastUsed(retrievedUser);
    }
}
