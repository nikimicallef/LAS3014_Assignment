package com.uom.las3014.service;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.springdata.DigestDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DigestsServiceImpl implements DigestsService {
    @Autowired
    private DigestDaoRepository digestDaoRepository;

    @Override
    public void saveAll(Iterable<? extends Digest> digests) {
        digestDaoRepository.save(digests);
    }

    @Override
    public void deleteDigestByDayOfWeekBefore(Timestamp timestamp) {
        digestDaoRepository.deleteDigestByDayOfWeekBefore(timestamp);
    }
}
