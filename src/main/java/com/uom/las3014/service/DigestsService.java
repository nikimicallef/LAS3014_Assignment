package com.uom.las3014.service;

import com.uom.las3014.api.response.GroupTopStoriesByDateResponse;
import com.uom.las3014.api.response.MultipleTopStoriesPerDateResponse;
import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.Story;
import com.uom.las3014.dao.Topic;
import com.uom.las3014.dao.User;
import com.uom.las3014.dao.springdata.DigestDaoRepository;
import org.springframework.http.ResponseEntity;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Service for the {@link Digest} {@link Entity}. This is the only place which interacts with the {@link DigestDaoRepository}
 */
public interface DigestsService {
    /**
     * @param digestsToSave {@link Iterable} of {@link Digest} to persist
     */
    void saveAll(Iterable<? extends Digest> digestsToSave);

    /**
     * @param dayOfWeekBefore Deletes all {@link Digest} where {@link Digest#dayOfWeek} is before this param
     */
    void deleteDigestByDayOfWeekBefore(Timestamp dayOfWeekBefore);

    /**
     * @param user Gets latest weekly {@link Digest} assigned to {@link User}
     * @return Top {@link Story} for each {@link Topic} for a specific date
     */
    ResponseEntity<GroupTopStoriesByDateResponse> getLatestWeeklyDigest(User user);

    /**
     * @param user Gets {@link Digest} assigned to this {@link User}
     * @param dateFrom Gets {@link Digest} where {@link Digest#dayOfWeek} is after dateFrom
     * @param dateTo Gets {@link Digest} where {@link Digest#dayOfWeek} is after dateTo
     * @return Top {@link Story} for each {@link Topic} for multiple dates
     */
    ResponseEntity<MultipleTopStoriesPerDateResponse> getGroupOfWeeklyDigests(User user, Date dateFrom, Date dateTo);
}
