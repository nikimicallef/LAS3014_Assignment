package com.uom.las3014.service;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StoriesServiceImpl implements StoriesService{
    @Autowired
    private StoriesDaoRepository storiesDaoRepository;

    @Override
    public Story createNewOrUpdateExistingStory(final Story story) {
        final Optional<Story> storyDb = storiesDaoRepository.findStoryByStoryId(story.getStoryId());

        final Story returnStory;

        if(storyDb.isPresent()){
            returnStory = storyDb.get();
            returnStory.setScore(story.getScore());
        } else {
            returnStory = storiesDaoRepository.save(story);
        }

        return returnStory;
    }
}
