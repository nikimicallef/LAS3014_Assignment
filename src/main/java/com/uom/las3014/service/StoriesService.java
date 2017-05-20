package com.uom.las3014.service;

import com.uom.las3014.dao.Story;

public interface StoriesService {
    Story createNewOrUpdateExistingStory(Story story);
}
