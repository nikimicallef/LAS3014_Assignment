package com.uom.las3014.restcontroller;

import com.uom.las3014.annotations.AuthBySessionToken;
import com.uom.las3014.api.response.TopicsTopStoryResponse;
import com.uom.las3014.service.StoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stories")
public class StoriesController {
    @Autowired
    private StoriesService storiesService;

    @AuthBySessionToken
    @RequestMapping(method = RequestMethod.GET, value = "/top")
    public ResponseEntity<TopicsTopStoryResponse> getTopStory(final @RequestHeader(name = "X-SessionToken") String sessionToken){
        //TODO: Cache this response?
        return storiesService.getTopStoryForTopics(sessionToken);
    }
}
