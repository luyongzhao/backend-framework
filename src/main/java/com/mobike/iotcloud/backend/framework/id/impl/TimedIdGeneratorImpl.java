package com.mobike.iotcloud.backend.framework.id.impl;


import com.mobike.iotcloud.backend.framework.id.IDGenerator;
import com.mobike.iotcloud.backend.framework.util.NumberX;
import org.springframework.stereotype.Service;

/**
 * 该类主要用于测试使用
 */
@Service("timedIdGenerator")
public class TimedIdGeneratorImpl implements IDGenerator {


    @Override
    public String nextStringID() {
        return NumberX.Number62.number10ToX(System.currentTimeMillis());
    }

    @Override
    public Long nextLongID() {

        return System.currentTimeMillis();
    }


}
