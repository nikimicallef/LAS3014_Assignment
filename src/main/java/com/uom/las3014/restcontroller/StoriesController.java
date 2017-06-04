package com.uom.las3014.restcontroller;

import com.uom.las3014.annotations.AuthBySessionToken;
import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.dao.User;
import com.uom.las3014.service.DigestsService;
import com.uom.las3014.service.StoriesService;
import com.uom.las3014.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/stories")
public class StoriesController {
    @Autowired
    private StoriesService storiesService;

    @Autowired
    private DigestsService digestsService;

    @Autowired
    private UserService userService;

    @AuthBySessionToken
    @RequestMapping(method = RequestMethod.GET, value = "/top")
    public ResponseEntity<GroupTopStoriesByDateResponse> getTopStory(final @RequestHeader(name = "X-SessionToken") String sessionToken){
        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);

        return storiesService.getTopStoryForTopics(user);
    }

    @AuthBySessionToken
    @RequestMapping(method = RequestMethod.GET, value = "/digests/latest")
    public ResponseEntity<GroupTopStoriesByDateResponse> getLatestDigest(final @RequestHeader(name = "X-SessionToken") String sessionToken){
        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);

        return digestsService.getLatestWeeklyDigest(user);
    }

    @AuthBySessionToken
    @RequestMapping(method = RequestMethod.GET, value = "/digests/range")
    public ResponseEntity<MultipleTopStoriesPerDateResponse> getDigestsGroup(final @RequestHeader(name = "X-SessionToken") String sessionToken,
                                                                             final @RequestParam(value="from", required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                                                                             final @RequestParam(value="from", required = true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo){
        final User user = userService.getUserFromDbUsingSessionToken(sessionToken);

        return digestsService.getGroupOfWeeklyDigests(user, dateFrom, dateTo);
    }
}
