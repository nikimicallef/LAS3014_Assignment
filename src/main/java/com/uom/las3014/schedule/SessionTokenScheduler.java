package com.uom.las3014.schedule;

import com.uom.las3014.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionTokenScheduler {
    @Autowired
    private UserService userService;

    private final Log logger = LogFactory.getLog(this.getClass());

    //TODO: Set to run every 10 minutes
    @Scheduled(fixedDelay = 600_000, initialDelay = 60_000)
    //@Scheduled(fixedDelay = 5_000)
    public void invalidateInactiveSessionTokensScheduledTask() throws InterruptedException {
        logger.debug("Running scheduled task which invalidates inactive session tokens.");
        userService.invalidateInactiveSessionTokens();
    }
}
