package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.User;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Date;

public interface DigestsService {
    void saveAll(Iterable<? extends Digest> digests);

    void deleteDigestByDayOfWeekBefore(final Timestamp timestamp);

    ResponseEntity<GroupTopStoriesByDateResponse> getLatestWeeklyDigest(User user);

    ResponseEntity<MultipleTopStoriesPerDateResponse> getGroupOfWeeklyDigests(User user, Date dateFrom, Date dateTo);
}
