package weixin.popular.bean.pay;

/**
 * 支付 JS 请求
 * 
 * @author LiYi
 * 
 */
public class PayJsRequest
{
	private String appId;

	private String timeStamp;

	private String nonceStr;

	private String package_;

	private String signType;

	private String paySign;

	public String getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(String timestamp)
	{
		this.timeStamp = timestamp;
	}

	public String getNonceStr()
	{
		return nonceStr;
	}

	public void setNonceStr(String nonceStr)
	{
		this.nonceStr = nonceStr;
	}

	public String getSignType()
	{
		return signType;
	}

	public void setSignType(String signType)
	{
		this.signType = signType;
	}

	public String getPaySign()
	{
		return paySign;
	}

	public void setPaySign(String paySign)
	{
		this.paySign = paySign;
	}

	public String getPackage_()
	{
		return package_;
	}

	public void setPackage_(String package_)
	{
		this.package_ = package_;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}
}
