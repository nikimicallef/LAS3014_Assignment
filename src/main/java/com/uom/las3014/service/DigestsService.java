package com.uom.las3014.service;

import com.uom.las3014.dao.Digest;

public interface DigestsService {
    void saveAll(Iterable<Digest> digests);
}
