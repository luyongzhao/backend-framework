/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobike.iotcloud.backend.framework.auth;

/**
 * Created by haowei.yao on 2017/9/28.
 */

public abstract class Signer {

    public static final String HMAC_SHA1 = "HMAC_SHA1";
    public static final String SHA256_WITH_RSA = "SHA256_WITH_RSA";

    private final static Signer hmacSHA1Signer = new HmacSHA1Signer();
    private final static Signer sha256withRSASigner = new SHA256withRSASigner();

    public abstract String signString(String stringToSign, String accessKeySecret);
    public abstract String getSignerName();
    public abstract String getSignerVersion();
    public abstract String getSignerType();

    public static Signer getSigner(String signType) {

        if (HMAC_SHA1.equals(signType)){
            return hmacSHA1Signer;
        }else{
            return sha256withRSASigner;
        }
    }

}
