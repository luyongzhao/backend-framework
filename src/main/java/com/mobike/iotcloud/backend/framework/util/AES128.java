package com.mobike.iotcloud.backend.framework.util;

import com.alibaba.druid.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES128加解密处理<br />
 * 使用过程中如果使用的是有限密钥， 则最好将本类进行单例化后再进行使用， 可提升效率
 *
 * @author leo
 */
public class AES128 {
    // 密钥算法
    public static final String KEY_ALGORITHM = "AES";

    // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private Cipher cipherDecrypt;
    private Cipher cipherEncrypt;

    public static final String DEFAULT_IV = "0102030405060708";

    public AES128(String key) {
        this.init(key, DEFAULT_IV);
    }

    public AES128(String key, String ivStr) {
        this.init(key, ivStr);
    }

    /**
     * 构造函数
     *
     * @param key   密钥::16位byte符串
     * @param ivStr 向量: 0102030405060708
     */
    private void init(String key, String ivStr) {
        // 判断Key是否正确
        if (key == null) {
            throw new RuntimeException("Key为空null");
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            throw new RuntimeException("Key长度不是16位");
        }

        try {
            // 0102030405060708
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());

            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            // KeyGenerator _generator = KeyGenerator.getInstance("AES");
            // SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // secureRandom.setSeed(key.getBytes());
            // _generator.init(128, secureRandom);
            // SecretKey skeySpec = _generator.generateKey();

            cipherDecrypt = Cipher.getInstance(CIPHER_ALGORITHM);
            cipherDecrypt.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            cipherEncrypt = Cipher.getInstance(CIPHER_ALGORITHM);
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化失败");
        }
    }

    public String encryptWithBase64(String plainText) {
        byte[] encrypted = encrypt(plainText);
        return Base64.byteArrayToBase64(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public byte[] encrypt(String plainText) {
        try {
            return cipherEncrypt.doFinal(plainText.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String decryptWithBase64(String encryptData) {
        try {
            byte[] encrypted1 = Base64.base64ToByteArray(encryptData);// Base64Decoder.decodeToBytes(encryptData);//
            // 先用base64解密
            byte[] original = decrypt(encrypted1);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public byte[] decrypt(byte[] encryptData) {
        try {
            return cipherDecrypt.doFinal(encryptData);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}