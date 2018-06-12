package com.lyz.backend.framework.controller.bean;

import com.lyz.backend.framework.util.ThreadLocalContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class UserAgent {
    private HttpServletRequest request;
    private HttpServletResponse response;

    // User-Agent and Accept HTTP request headers
    private String userAgent = "";
    private String httpAccept = "";
    private String uri;
    private boolean ajaxRequest = false;
    private boolean pjaxRequest = false;
    private boolean smartPhone = false;
    private String ip;

    private int width;
    private int height;
    private String jsonpCallback;

    private String styles;
    private String queryParam;

    public UserAgent() {

    }

    /**
     * Initialize the userAgent and httpAccept variables by getting the headers
     * from the HttpServletRequest
     *
     * @param request the HttpServletRequest to get the header information from
     */
    public UserAgent(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        String userAgent = request.getHeader("User-Agent");
        String httpAccept = request.getHeader("Accept");

        if (userAgent == null)
            userAgent = "";
        if (httpAccept == null)
            httpAccept = "";

        this.userAgent = userAgent.toLowerCase();
        this.httpAccept = httpAccept.toLowerCase();
        this.ajaxRequest = StringUtils.equals(request.getHeader("x-requested-with"), "XMLHttpRequest");
        if (ajaxRequest)
            this.pjaxRequest = StringUtils.isNotBlank(request.getHeader("x-pjax"));

        this.smartPhone = isAndroid() || isIphoneOrIpod() || isWindowsPhone() || isFirefoxOS();

        if (ajaxRequest) {
            width = NumberUtils.toInt(request.getParameter("_screen_width"), -1);
            height = NumberUtils.toInt(request.getParameter("_screen_height"), -1);
        }
        this.jsonpCallback = request.getParameter("_callback");
        this.setQueryParam(request.getQueryString());
    }

    public String getURL(boolean withDomain) {
        try {
            String queryString = request.getQueryString();
            StringBuilder sb = new StringBuilder();
            if (withDomain) {
                sb.append(request.getRequestURL().toString());
            } else {
                sb.append(request.getRequestURI());
            }
            if (StringUtils.isNotBlank(queryString)) {
                sb.append("?").append(queryString);
            }
            return sb.toString();
        } catch (Exception e) {// 编码都出错，game over吧、、、
            throw new RuntimeException(e);
        }
    }

    public String getStyles() {
        return request.getParameter("__styles__");
    }

    public boolean isOurReferer() {
        String referer = request.getHeader("referer");
        String host = request.getHeader("host");

        if (StringUtils.isNotBlank(referer)) {
            try {
                URL u = new URL(referer);
                return u.getHost().equals(host);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public static UserAgent current() {
        return ThreadLocalContext.get(UserAgent.class);
    }

    public String getUri() {
        if (uri == null) {
            String ctxPath = request.getContextPath();
            uri = request.getRequestURI();

            if (StringUtils.isNotBlank(ctxPath)) {// 去掉contextPath, content != null --> uri!=null
                uri = uri.substring(ctxPath.length());
            }
        }
        return uri;
    }

    public String getJsonpCallback() {
        return jsonpCallback;
    }

    public void setJsonpCallback(String jsonpCallback) {
        this.jsonpCallback = jsonpCallback;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public boolean isAjaxRequest() {
        return this.ajaxRequest;
    }

    public boolean isMicroMessenger() {
        return userAgent.toLowerCase().indexOf(deviceMicroMessage) != -1;
    }

    /**
     * 是否智能手机
     *
     * @return
     */
    public boolean isSmartPhone() {
        return smartPhone;
    }

    /**
     * Return the lower case HTTP_USER_AGENT
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Return the lower case HTTP_ACCEPT
     */
    public String getHttpAccept() {
        return httpAccept;
    }

    /**
     * iss if the current device is an iPhone.
     */
    public boolean isIphone() {
        // The iPod touch says it's an iPhone! So let's disambiguate.
        return userAgent.indexOf(deviceIphone) != -1 && !isIpod();
    }

    public boolean isIOS() {
        return userAgent.indexOf(deviceIphone) != -1 || userAgent.indexOf(deviceIpad) != -1
                || userAgent.indexOf(deviceIpod) != -1;
    }

    public boolean isIpad() {
        return userAgent.indexOf(deviceIpad) != -1;
    }

    /**
     * iss if the current device is an iPod Touch.
     */
    public boolean isIpod() {
        return userAgent.indexOf(deviceIpod) != -1;
    }

    /**
     * iss if the current device is an iPhone or iPod Touch.
     */
    public boolean isIphoneOrIpod() {
        // We repeat the searches here because some iPods may report themselves
        // as an iPhone, which would be okay.
        return userAgent.indexOf(deviceIphone) != -1 || userAgent.indexOf(deviceIpod) != -1;
    }

    /**
     * iss if the current device is an Android OS-based device.
     */
    public boolean isAndroid() {
        return userAgent.indexOf(deviceAndroid) != -1;
    }

    /**
     * iss if the current device is an Android OS-based device and the browser
     * is based on WebKit.
     */
    public boolean isAndroidWebKit() {
        return isAndroid() && isWebkit();
    }

    /**
     * iss if the current browser is based on WebKit.
     */
    public boolean isWebkit() {
        return userAgent.indexOf(engineWebKit) != -1;
    }

    /**
     * iss if the current browser is the S60 Open Source Browser.
     */
    public boolean isS60OssBrowser() {
        // First, test for WebKit, then make sure it's either Symbian or S60.
        return isWebkit() && (userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1);
    }

    /**
     * iss if the current device is any Symbian OS-based device, including older
     * S60, Series 70, Series 80, Series 90, and UIQ, or other browsers running
     * on these devices.
     */
    public boolean isSymbianOS() {
        return userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1
                || userAgent.indexOf(deviceS70) != -1 || userAgent.indexOf(deviceS80) != -1
                || userAgent.indexOf(deviceS90) != -1;
    }

    /**
     * iss if the current browser is a Windows Mobile device.
     */
    public boolean isWindowsMobile() {
        // Most devices use 'Windows CE', but some report 'iemobile'
        // and some older ones report as 'PIE' for Pocket IE.
        return userAgent.indexOf(deviceWinMob) != -1 || userAgent.indexOf(deviceIeMob) != -1
                || userAgent.indexOf(enginePie) != -1 || (isWapWml() && userAgent.indexOf(deviceWindows) != -1);
    }

    public boolean isWindowsPhone() {
        // Most devices use 'Windows CE', but some report 'iemobile'
        // and some older ones report as 'PIE' for Pocket IE.
        return userAgent.indexOf(deviceWindowsPhone) != -1;
    }

    /**
     * iss if the current browser is a BlackBerry of some sort.
     */
    public boolean isBlackBerry() {
        return userAgent.indexOf(deviceBB) != -1 || httpAccept.indexOf(vndRIM) != -1;
    }

    /**
     * iss if the current browser is a BlackBerry Touch device, such as the
     * Storm
     */
    public boolean isBlackBerryTouch() {
        return userAgent.indexOf(deviceBBStorm) != -1;
    }

    /**
     * iss if the current browser is on a PalmOS device.
     */
    public boolean isPalmOS() {
        // Most devices nowadays report as 'Palm', but some older ones reported
        // as Blazer or Xiino.
        if (userAgent.indexOf(devicePalm) != -1 || userAgent.indexOf(engineBlazer) != -1
                || userAgent.indexOf(engineXiino) != -1 && !isPalmWebOS()) {
            // Make sure it's not WebOS first
            if (isPalmWebOS()) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * iss if the current browser is on a Palm device running the new WebOS.
     */
    public boolean isPalmWebOS() {
        return userAgent.indexOf(deviceWebOS) != -1;
    }

    /**
     * iss whether the device is a Brew-powered device.
     */
    public boolean isBrewDevice() {
        return userAgent.indexOf(deviceBrew) != -1;
    }

    /**
     * iss the Danger Hiptop device.
     */
    public boolean isDangerHiptop() {
        return userAgent.indexOf(deviceDanger) != -1 || userAgent.indexOf(deviceHiptop) != -1;
    }

    /**
     * iss Opera Mobile or Opera Mini. Added by AHand
     */
    public boolean isOperaMobile() {
        return userAgent.indexOf(engineOpera) != -1 && (userAgent.indexOf(mini) != -1 || userAgent.indexOf(mobi) != -1);
    }

    /**
     * iss whether the device supports WAP or WML.
     */
    public boolean isWapWml() {
        return httpAccept.indexOf(vndwap) != -1 || httpAccept.indexOf(wml) != -1;
    }

    /**
     * The quick way to is for a mobile device. Will probably is most
     * recent/current mid-tier Feature Phones as well as smartphone-class
     * devices.
     */
    public boolean isMobileQuick() {
        // Ordered roughly by market share, WAP/XML > Brew > Smartphone.
        if (isWapWml()) {
            return true;
        }
        if (isBrewDevice()) {
            return true;
        }

        // Updated by AHand
        if (isOperaMobile()) {
            return true;
        }

        if (userAgent.indexOf(engineUpBrowser) != -1) {
            return true;
        }
        if (userAgent.indexOf(engineOpenWeb) != -1) {
            return true;
        }
        if (userAgent.indexOf(deviceMidp) != -1) {
            return true;
        }

        if (isSmartPhone()) {
            return true;
        }
        if (isDangerHiptop()) {
            return true;
        }

        if (isMidpCapable()) {
            return true;
        }

        if (userAgent.indexOf(devicePda) != -1) {
            return true;
        }
        if (userAgent.indexOf(mobile) != -1) {
            return true;
        }

        // is older phones from certain manufacturers and operators.
        if (userAgent.indexOf(uplink) != -1) {
            return true;
        }
        if (userAgent.indexOf(manuSonyEricsson) != -1) {
            return true;
        }
        if (userAgent.indexOf(manuericsson) != -1) {
            return true;
        }
        if (userAgent.indexOf(manuSamsung1) != -1) {
            return true;
        }
        if (userAgent.indexOf(svcDocomo) != -1) {
            return true;
        }
        if (userAgent.indexOf(svcKddi) != -1) {
            return true;
        }
        if (userAgent.indexOf(svcVodafone) != -1) {
            return true;
        }

        return false;
    }

    /**
     * iss if the current device is a Sony Playstation.
     */
    public boolean isSonyPlaystation() {
        return userAgent.indexOf(devicePlaystation) != -1;
    }

    /**
     * iss if the current device is a Nintendo game device.
     */
    public boolean isNintendo() {
        return userAgent.indexOf(deviceNintendo) != -1 || userAgent.indexOf(deviceWii) != -1
                || userAgent.indexOf(deviceNintendoDs) != -1;
    }

    /**
     * iss if the current device is a Microsoft Xbox.
     */
    public boolean isXbox() {
        return userAgent.indexOf(deviceXbox) != -1;
    }

    /**
     * iss if the current device is an Internet-capable game console.
     */
    public boolean isGameConsole() {
        return isSonyPlaystation() || isNintendo() || isXbox();
    }

    /**
     * iss if the current device supports MIDP, a mobile Java technology.
     */
    public boolean isMidpCapable() {
        return userAgent.indexOf(deviceMidp) != -1 || httpAccept.indexOf(deviceMidp) != -1;
    }

    /**
     * iss if the current device is on one of the Maemo-based Nokia Internet
     * Tablets.
     */
    public boolean isMaemoTablet() {
        return (userAgent.indexOf(maemo) != -1 || (userAgent.indexOf(maemoTablet) != -1 && userAgent.indexOf(linux) != -1));
    }

    /**
     * iss if the current device is an Archos media player/Internet tablet.
     */
    public boolean isArchos() {
        return userAgent.indexOf(deviceArchos) != -1;
    }

    /**
     * iss if the current browser is a Sony Mylo device. Updated by AHand
     */
    public boolean isSonyMylo() {
        return userAgent.indexOf(manuSony) != -1
                && (userAgent.indexOf(qtembedded) != -1 || userAgent.indexOf(mylocom2) != -1);
    }

    /**
     * The longer and more thorough way to is for a mobile device. Will probably
     * is most feature phones, smartphone-class devices, Internet Tablets,
     * Internet-enabled game consoles, etc. This ought to catch a lot of the
     * more obscure and older devices, also -- but no promises on thoroughness!
     */
    public boolean isMobileLong() {
        return isMobileQuick() || isMaemoTablet() || isGameConsole();
    }

    // *****************************
    // For Desktop Browsers
    // *****************************
    public boolean isDeskModernBrowser() {
        return isMacintosh() || (isMSIE9() || isMSIE10() || isChrome() || isFirefox());
    }

    public boolean isModernBrowser() {
        return isMacOS() || (isMSIE9() || isMSIE10() || isChrome() || isFirefox());
    }

    public boolean isMSIE() {
        return userAgent.indexOf(msie) != -1;
    }

    public boolean isMSIE6() {
        return userAgent.indexOf(msie60) != -1 && userAgent.indexOf(msie61) != -1;
    }

    public boolean isMSIE7() {
        return userAgent.indexOf(msie7) != -1;
    }

    public boolean isMSIE8() {
        return userAgent.indexOf(msie8) != -1;
    }

    public boolean isMSIE9() {
        return userAgent.indexOf(msie9) != -1;
    }

    public boolean isMSIE10() {
        return userAgent.indexOf(msie10) != -1;
    }

    public boolean isFirefox() {
        return userAgent.indexOf(firefox) != -1;
    }

    public boolean isFirefoxOS() {
        return userAgent.indexOf(firefox) != -1 && userAgent.indexOf("mobile") != -1;
    }

    public boolean isSafari() {
        return userAgent.indexOf(safari) != -1 && !isChrome();
    }

    public boolean isChrome() {
        return userAgent.indexOf(chrome) != -1;
    }

    public boolean isOpera() {
        return userAgent.indexOf(opera) != -1;
    }

    public boolean isWindows() {
        return userAgent.indexOf(windows) != -1;
    }

    public boolean isMacintosh() {
        return userAgent.indexOf(macintosh) != -1;
    }

    public boolean isMacOS() {
        return userAgent.indexOf(macos) != -1;
    }

    // *****************************
    // For Mobile Web Site Design
    // *****************************

    /**
     * The quick way to is for a tier of devices. This method iss for devices
     * which can display iPhone-optimized web content. Includes iPhone, iPod
     * Touch, Android, Palm WebOS, etc.
     */
    public boolean isTierIphone() {
        return isIphoneOrIpod() || isPalmWebOS() || isAndroid() || isAndroidWebKit();
    }

    /**
     * The quick way to is for a tier of devices. This method iss for all
     * smartphones, but excludes the iPhone Tier devices.
     */
    public boolean isTierSmartphones() {
        return isSmartPhone() && (!isTierIphone());
    }

    /**
     * The quick way to is for a tier of devices. This method iss for all other
     * types of phones, but excludes the iPhone and Smartphone Tier devices.
     */
    public boolean isTierOtherPhones() {
        return isMobileQuick() && (!isTierIphone()) && (!isTierSmartphones());
    }

    // Initialize some initial smartphone string variables.
    public static final String engineWebKit = "webkit";
    public static final String deviceAndroid = "android";
    public static final String deviceIphone = "iphone";
    public static final String deviceIpod = "ipod";
    public static final String deviceIpad = "ipad";
    public static final String deviceSymbian = "symbian";
    public static final String deviceS60 = "series60";
    public static final String deviceS70 = "series70";
    public static final String deviceS80 = "series80";
    public static final String deviceS90 = "series90";
    public static final String deviceWinMob = "windows ce";
    public static final String deviceWindows = "windows";
    public static final String deviceIeMob = "iemobile";
    public static final String enginePie = "wm5 pie"; // An old Windows Mobile
    public static final String deviceBB = "blackberry";
    public static final String vndRIM = "vnd.rim"; // isable when BB devices
    // emulate IE or Firefox
    public static final String deviceBBStorm = "blackberry95"; // Storm 1 and 2
    public static final String devicePalm = "palm";
    public static final String deviceWebOS = "webos"; // For Palm's new WebOS
    // devices

    public static final String engineBlazer = "blazer"; // Old Palm
    public static final String engineXiino = "xiino"; // Another old Palm

    // Initialize variables for mobile-specific content.
    public static final String vndwap = "vnd.wap";
    public static final String wml = "wml";

    // Initialize variables for other random devices and mobile browsers.
    public static final String deviceBrew = "brew";
    public static final String deviceDanger = "danger";
    public static final String deviceHiptop = "hiptop";
    public static final String devicePlaystation = "playstation";
    public static final String deviceNintendoDs = "nitro";
    public static final String deviceNintendo = "nintendo";
    public static final String deviceWii = "wii";
    public static final String deviceXbox = "xbox";
    public static final String deviceArchos = "archos";

    public static final String engineOpera = "opera"; // Popular browser
    public static final String engineNetfront = "netfront"; // Common embedded
    // OS browser
    public static final String engineUpBrowser = "up.browser"; // common on some
    // phones
    public static final String engineOpenWeb = "openweb"; // Transcoding by
    // OpenWave server
    public static final String deviceMidp = "midp"; // a mobile Java technology
    public static final String uplink = "up.link";

    public static final String devicePda = "pda"; // some devices report
    // themselves as PDAs
    public static final String mini = "mini"; // Some mobile browsers put "mini"
    // in their names.
    public static final String mobile = "mobile"; // Some mobile browsers put
    // "mobile" in their user
    // agent strings.
    public static final String mobi = "mobi"; // Some mobile browsers put "mobi"
    // in their user agent strings.

    // Use Maemo, Tablet, and Linux to test for Nokia"s Internet Tablets.
    public static final String maemo = "maemo";
    public static final String maemoTablet = "tablet";
    public static final String linux = "linux";
    public static final String qtembedded = "qt embedded"; // for Sony Mylo
    public static final String mylocom2 = "com2"; // for Sony Mylo also

    // In some UserAgents, the only clue is the manufacturer.
    public static final String manuSonyEricsson = "sonyericsson";
    public static final String manuericsson = "ericsson";
    public static final String manuSamsung1 = "sec-sgh";
    public static final String manuSony = "sony";

    // In some UserAgents, the only clue is the operator.
    public static final String svcDocomo = "docomo";
    public static final String svcKddi = "kddi";
    public static final String svcVodafone = "vodafone";

    // Standard desktop browser ision strings
    public static final String msie = "msie";
    public static final String msie60 = "msie 6.0";
    public static final String msie61 = "msie 6.1";
    public static final String msie7 = "msie 7";
    public static final String msie8 = "msie 8";
    public static final String msie9 = "msie 9";
    public static final String msie10 = "msie 10";
    public static final String firefox = "firefox";
    public static final String safari = "apple";
    public static final String chrome = "chrome";
    public static final String opera = "presto";

    // OS ision
    public static final String windows = "windows";
    public static final String macintosh = "macintosh";
    public static final String macos = "mac os";

    // Windows Phone
    public static final String deviceWindowsPhone = "windows phone";

    // 微信
    public static final String deviceMicroMessage = "micromessenger";

    public boolean isPjaxRequest() {
        return pjaxRequest;
    }

    public void setPjaxRequest(boolean pjaxRequest) {
        this.pjaxRequest = pjaxRequest;
    }

    public String getIp() {
        if (StringUtils.isBlank(ip)) {
            this.ip = parseIPAddress(this.request);
        }
        return this.ip;
    }

    public static String parseIPAddress(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }

        if (StringUtils.isEmpty(ip) || "127.0.0.1".equals(ip)) {
            ip = req.getHeader("X-Real-IP");
            if (StringUtils.isEmpty(ip) || "unKnown".equalsIgnoreCase(ip)) {
                ip = req.getRemoteAddr();
            }
        }
        return ip;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }
}