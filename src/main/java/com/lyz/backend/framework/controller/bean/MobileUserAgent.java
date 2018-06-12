package com.lyz.backend.framework.controller.bean;

import com.lyz.backend.framework.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

@Slf4j
public class MobileUserAgent {

    String os;// 操作系统---2
    String version;// 版本号--->非加密---1.5--》15000(01.50.00)
    String deviceToken;// 令牌 :推送消息使用
    Double lat;// 用户坐标（选填）
    Double lng;// 用户坐标（选填）
    Integer screenWidth;// 客户端屏幕宽度
    Integer screenHeight;// 客户端屏幕高度
    String uuid;// 手机唯一识别（IOS--随机生成；android--fingerPrint）
    String newble;// 是否首次安装（选填）0-false 1-true
    String channel;// 分发渠道， 仅android有效（选填）
    String device;// 设备信息
    String pkgID;// 安装包package或bundleID
    String lang;// 用户语言
    // ///////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////
    String token;// token（选填）
    String userID;// 用户编号（登录填写）
    String cityID;// 城市（app不填）
    String gpsCityID;// 定位城市（app不填）
    // ///////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////
    String sign;// 参数签名--备用（app不填）
    String ip;// 用户ip地址
    String uri;// 请求uri
    String queryParam;//get请求参数
    int requestType;// 请求类别---app为1

    //角色类型(app不填)
    String role;

    //rest
    String schoolLoginToken;
    String teacherLoginToken;

    public String getSchoolLoginToken() {
        return schoolLoginToken;
    }

    public void setSchoolLoginToken(String schoolLoginToken) {
        this.schoolLoginToken = schoolLoginToken;
    }

    public String getTeacherLoginToken() {
        return teacherLoginToken;
    }

    public void setTeacherLoginToken(String teacherLoginToken) {
        this.teacherLoginToken = teacherLoginToken;
    }

    public static final class Lang {
        public static final String CN = "cn";
        public static final String EN = "en";
    }

    public static Integer convertVersion(String version) {

        if (StringUtils.isBlank(version)) {
            log.error("error format version:" + version);
            return 0;
        }

        String verArray[] = version.split("\\.");
        if (verArray.length != 3) {
            log.error("error format version:" + version);
            return 0;
        }

        return NumberUtils.toInt(verArray[0]) * 10000 + NumberUtils.toInt(verArray[1]) * 100 + NumberUtils.toInt(verArray[2]);

    }

    public static final class OS {
        public static final String Android = "1";
        public static final String iOS = "2";
        public static final String WEB = "3";
    }

    public static final class RequestType {
        public static final int rest = 1;// rest请求, 通过http header获取的ticket
        public static final int web = 2;// 通过session获取的ticket
        // public static final int agent = 3;// 通过请求参数获取的ticket, 没有使用
        public static final int unkown = 4;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static MobileUserAgent current() {
        return ThreadLocalContext.get(MobileUserAgent.class);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isRestRequest() {
        return requestType == RequestType.rest;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getIp() {
        return ip;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPkgID() {
        return pkgID;
    }

    public void setPkgID(String pkgID) {
        this.pkgID = pkgID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    @Override
    public String toString() {
        return "MobileUserAgent [os=" + os + ", version=" + version + ", deviceToken=" + deviceToken + ", lat=" + lat
                + ", lng=" + lng + ", screenWidth=" + screenWidth + ", screenHeight=" + screenHeight + ", uuid=" + uuid
                + ", channel=" + channel + ", device=" + device + ", pkgID=" + pkgID + ", lang=" + lang + ", token="
                + token + ", userID=" + userID + ", cityID=" + cityID + ", sign=" + sign + ", ip=" + ip + ", uri="
                + uri + ", requestType=" + requestType + "]";
    }

    public String getNewble() {
        return newble;
    }

    public void setNewble(String newble) {
        this.newble = newble;
    }

    public String getGpsCityID() {
        return gpsCityID;
    }

    public void setGpsCityID(String gpsCityID) {
        this.gpsCityID = gpsCityID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }
}
