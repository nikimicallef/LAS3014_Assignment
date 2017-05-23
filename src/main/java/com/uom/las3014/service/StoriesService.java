package com.uom.las3014.service;

import com.uom.las3014.dao.Story;

import java.util.List;

public interface StoriesService {
    Story createNewOrUpdateExistingStory(Story story);

    List<Story> getLastWeeksUndeletedTopics();

    List<Story> get12HrsUndeletedTopics();
}
