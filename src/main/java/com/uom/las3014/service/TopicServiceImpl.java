package com.uom.las3014.service;

import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.springdata.TopicsDaoRepository;
import com.uom.las3014.httpconnection.HackernewsRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicsDaoRepository topicsDaoRepository;

    @Autowired
    private HackernewsRequester hackernewsRequester;

    @Cacheable("topicPojo")
    public Topic createNewTopicIfNotExists(final String topicName){
        //TODO: Change with CREATE-IF-NOT-EXISTS instead of 2 queries.
        final Optional<Topic> existingTopic = topicsDaoRepository.findTopicsByTopicName(topicName);

        return existingTopic.orElseGet(() -> topicsDaoRepository.save(new Topic(topicName)));
    }

//    public ResponseEntity<GenericMessageResponse> getNewItems(){
//        try {
//            hackernewsRequestor.getNewStories();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(new GenericMessageResponse("TEST"));
//    }

//    public ResponseEntity<GenericMessageResponse> getItem(final Integer item){
//        try {
//            hackernewsRequestor.getItem(item);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(new GenericMessageResponse("TEST"));
//    }
}
