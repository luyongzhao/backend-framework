package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.SoftReferenceObjectPool;

/**
 * AES128加密算法池，解决算法初始化慢和多线程并发不能使用的问题
 *
 * @author luyongzhao
 */
public class AES128Factory implements PoolableObjectFactory {
    private static Log log = LogFactory.getLog(AES128Factory.class);

    private SoftReferenceObjectPool aesPoolFacotry = null;

    private String key;

    public AES128Factory(String key) {
        this.key = key;
        this.aesPoolFacotry = new SoftReferenceObjectPool(this);
    }

    public String decrypt(String ticket) {
        AES128 aes128 = null;
        try {
            aes128 = (AES128) aesPoolFacotry.borrowObject();
            return aes128.decryptWithBase64(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            try {
                aesPoolFacotry.returnObject(aes128);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }
        return null;
    }

    public String encrypt(String ticket) {
        AES128 aes128 = null;
        try {
            aes128 = (AES128) aesPoolFacotry.borrowObject();
            return aes128.encryptWithBase64(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            try {
                aesPoolFacotry.returnObject(aes128);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }
        return null;
    }

    @Override
    public Object makeObject() throws Exception {
        return new AES128(key);
    }

    @Override
    public void destroyObject(Object paramObject) throws Exception {

    }

    @Override
    public boolean validateObject(Object paramObject) {
        return true;
    }

    @Override
    public void activateObject(Object paramObject) throws Exception {

    }

    @Override
    public void passivateObject(Object paramObject) throws Exception {

    }
}
