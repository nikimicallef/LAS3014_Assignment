package com.uom.las3014.service;

import com.uom.las3014.dao.Digest;
import com.uom.las3014.dao.springdata.DigestsDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DigestsServiceImpl implements DigestsService {
    @Autowired
    private DigestsDaoRepository digestsDaoRepository;

    @Override
    public void saveAll(Iterable<? extends Digest> digests) {
        digestsDaoRepository.save(digests);
    }
}
