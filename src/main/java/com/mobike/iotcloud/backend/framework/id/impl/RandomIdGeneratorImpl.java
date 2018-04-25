package com.mobike.iotcloud.backend.framework.id.impl;


import com.mobike.iotcloud.backend.framework.id.IDGenerator;
import com.mobike.iotcloud.backend.framework.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 随机唯一id生成器，默认采用UUID
 */
@Service("idGenerator")
public class RandomIdGeneratorImpl implements IDGenerator {


    @Override
    public String nextStringID() {

        return UUID.randomUUID();
    }

    @Override
    public Long nextLongID() {

        throw new RuntimeException("不支持该方法！");
    }
}
