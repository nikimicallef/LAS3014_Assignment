package com.uom.las3014.aop;

import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.UsersDaoRepository;
import com.uom.las3014.exceptions.InvalidCredentialsException;
import com.uom.las3014.service.UserService;
import com.uom.las3014.service.UserServiceImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Aspect
public class AuthSessionTokenAop {
    @Autowired
    private UserService userService;

    //TODO: Bind on the parameter name so you always catch the correct parameter even if it is not the first one.
    @Pointcut("execution(* com.uom.las3014.restcontroller..*.*(@org.springframework.web.bind.annotation.RequestHeader (java.lang.String),..)) && args(sessionToken,..)")
    public void sessionTokenPointcut(final String sessionToken) {}

    //TODO: Change to around so tokenLastUsed updated after each entry which requires validation
    @Before("sessionTokenPointcut(sessionToken) && @annotation(com.uom.las3014.annotations.AuthBySessionToken)")
    public void sessionTokenBefore(final String sessionToken){
        final Optional<User> user = userService.getUserFromDbUsingSessionToken(sessionToken);

        final User retrievedUser = user.orElseThrow(InvalidCredentialsException::new);

        if (!retrievedUser.hasActiveSessionToken()){
            userService.invalidateSessionToken(retrievedUser);

            throw new InvalidCredentialsException();
        }
    }
}
