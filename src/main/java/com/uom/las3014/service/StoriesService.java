package com.uom.las3014.service;

import com.uom.las3014.dao.Story;

import java.sql.Timestamp;
import java.util.List;

public interface StoriesService {
    Story createNewOrUpdateExistingStory(Story story);

    List<Story> getLastWeeksUndeletedTopics();
}
