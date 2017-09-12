package com.lsm.ssq.service;

import org.springframework.transaction.annotation.Transactional;

public interface ISSQService {

    @Transactional
    boolean insert(String url, Integer periods) throws Exception;
}
