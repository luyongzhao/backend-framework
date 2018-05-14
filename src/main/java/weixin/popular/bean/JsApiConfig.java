package weixin.popular.bean;

/**
 * JSAPI配置信息
 * 
 * @author Leo
 * 
 */
public class JsApiConfig
{
	private String appId;
	private String nonceStr;
	private String timestamp;
	private String sign;

	public JsApiConfig()
	{
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getNonceStr()
	{
		return nonceStr;
	}

	public void setNonceStr(String nonceStr)
	{
		this.nonceStr = nonceStr;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

}
