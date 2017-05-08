package com.uom.las3014.annotations;


import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation created for AOP purposes. Required so methods will check the validity of the authorization token before executing.
 * The AOP defined for this annotation assumes that the {@link RequestHeader} session token will be the first parameter in the method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthBySessionToken {}
