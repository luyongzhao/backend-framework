package weixin.popular.bean;

public class CorpWeixinUserInfoResp extends BaseResult{

    /**
     * 成员UserID
     */
    private String UserId;

    /**
     * 手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
     */
    private String DeviceId;

    /**
     * 成员票据，最大为512字节。
     scope为snsapi_userinfo或snsapi_privateinfo，且用户在应用可见范围之内时返回此参数。
     后续利用该参数可以获取用户信息或敏感信息。
     */
    private String user_ticket;

    /**
     * user_ticket的有效时间（秒），随user_ticket一起返回
     */
    private Long expires_in;

    /**
     * 非企业成员的标识，对当前企业唯一
     */
    private String OpenId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getUser_ticket() {
        return user_ticket;
    }

    public void setUser_ticket(String user_ticket) {
        this.user_ticket = user_ticket;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }
}
