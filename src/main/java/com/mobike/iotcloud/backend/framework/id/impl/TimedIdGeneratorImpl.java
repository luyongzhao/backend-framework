package com.mobike.iotcloud.backend.framework.id.impl;


import com.mobike.iotcloud.backend.framework.id.IDGenerator;
import org.springframework.stereotype.Service;

/**
 * 该类主要用于测试使用
 */
@Service("idGenerator")
public class TimedIdGeneratorImpl implements IDGenerator {


    @Override
    public String nextStringID() {
        return System.currentTimeMillis()+"";
    }

    @Override
    public Long nextLongID() {

        return System.currentTimeMillis();
    }
}
