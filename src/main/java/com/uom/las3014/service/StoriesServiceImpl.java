package com.uom.las3014.service;

import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.springdata.StoriesDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class StoriesServiceImpl implements StoriesService{
    @Autowired
    private StoriesDaoRepository storiesDaoRepository;

    @Override
    public Story createNewOrUpdateExistingStory(final Story story) {
        return storiesDaoRepository.save(story);
    }

    @Override
    public List<Story> getLastWeeksUndeletedTopics() {
        return storiesDaoRepository.findAllByDateCreatedIsBetweenAndDeletedIs(new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)), new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)), false);
    }

    @Override
    public List<Story> get12HrsUndeletedTopics() {
        return storiesDaoRepository.findAllByDateCreatedIsBetweenAndDeletedIs(new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)), new Timestamp(System.currentTimeMillis()), false);
    }
}
