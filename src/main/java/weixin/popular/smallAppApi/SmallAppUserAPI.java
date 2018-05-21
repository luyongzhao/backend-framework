package weixin.popular.smallAppApi;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import weixin.popular.bean.BaseResult;
import weixin.popular.client.LocalHttpClient;

public class SmallAppUserAPI extends SmallAppBaseAPI {


    public static SessionResp jscode2session(String appid, String secret, String jsCode){

        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(BASE_URI + "/sns/jscode2session")
                .addParameter("appid", appid)
                .addParameter("js_code",jsCode)
                .addParameter("secret", secret)
                .build();
        return LocalHttpClient.executeJsonResult(httpUriRequest, SessionResp.class);

    }

    public static class SessionResp extends BaseResult{

        private String openid;

        private String session_key;

        private String unionid;


        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}
