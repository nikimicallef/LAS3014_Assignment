package com.uom.las3014.restcontroller.topics;

import com.uom.las3014.api.response.GenericMessageResponse;
import com.uom.las3014.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
public class TopicsController {
    @Autowired
    private TopicService topicService;

//    @RequestMapping(method = RequestMethod.GET, value ="/{itemNo}")
//    public ResponseEntity<GenericMessageResponse> getTopicItem(final @PathVariable Integer itemNo){
//        return topicService.getNewItems();
//    }
}
