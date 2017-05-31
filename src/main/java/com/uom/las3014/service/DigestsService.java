package com.uom.las3014.service;

import com.uom.las3014.dao.Digest;

import java.sql.Timestamp;

public interface DigestsService {
    void saveAll(Iterable<? extends Digest> digests);

    void deleteDigestByDayOfWeekBefore(final Timestamp timestamp);
}
