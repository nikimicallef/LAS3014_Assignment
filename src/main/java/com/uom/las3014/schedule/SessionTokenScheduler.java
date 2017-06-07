package com.uom.las3014.schedule;

import com.uom.las3014.dao.User;
import com.uom.las3014.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A {@link Scheduled} task which nulls invalid {@link User#sessionToken}
 */
@Component
public class SessionTokenScheduler {
    @Autowired
    private UserService userService;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Scheduled(cron = "0 */10 * * * *")
    public void performInvalidateInactiveSessionTokensJob() throws InterruptedException {
        logger.debug("Running scheduled task which invalidates inactive session tokens.");
        userService.invalidateInactiveSessionTokens();
    }
}
