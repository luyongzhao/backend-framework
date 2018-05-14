package weixin.popular.corpapi;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import weixin.popular.bean.CorpWeixinUserInfoResp;
import weixin.popular.bean.OAuthToken;
import weixin.popular.bean.WeixinUserInfoResp;
import weixin.popular.client.LocalHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 获取企业微信的授权
 * 
 * @author luyongzhao
 * 
 */
public class CorpOAuthAPI extends CorpBaseAPI
{


	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 *
	 * @param access_token
	 * @param code
	 * @return
	 */
	public static CorpWeixinUserInfoResp userinfo(String access_token, String code)
	{
		HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(CORP_BASE_URI + "/cgi-bin/user/getuserinfo")
				.addParameter("access_token", access_token).addParameter("code", code).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, CorpWeixinUserInfoResp.class);
	}

	/**
	 * 生成网页授权 URL
	 *
	 * @param corpid
	 * @param redirect_uri
	 *            自动URLEncoder
	 * @param snsapi_userinfo
	 *            是否需要用户信息
	 * @param state
	 *            可以为空
	 * @return
	 */
	public static String connectOauth2Authorize(String corpid, String redirect_uri, boolean snsapi_userinfo, String state)
	{
		return connectOauth2Authorize(corpid, redirect_uri, snsapi_userinfo, state, null);
	}

	/**
	 * 生成网页授权 URL (第三方平台开发)
	 *
	 * @param corpid
	 * @param redirect_uri
	 *            自动URLEncoder
	 * @param snsapi_userinfo
	 * @param state
	 *            可以为空
	 * @param component_appid
	 *            第三方平台开发，可以为空。 服务方的appid，在申请创建公众号服务成功后，可在公众号服务详情页找到
	 * @return
	 */
	public static String connectOauth2Authorize(String corpid, String redirect_uri, boolean snsapi_userinfo,
												String state, String component_appid)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(OPEN_URI + "/connect/oauth2/authorize?").append("appid=").append(corpid).append("&redirect_uri=")
					.append(URLEncoder.encode(redirect_uri, "utf-8")).append("&response_type=code").append("&scope=")
					.append(snsapi_userinfo ? "snsapi_userinfo" : "snsapi_base").append("&state=")
					.append(state == null ? "" : state);
			if (component_appid != null)
			{
				sb.append("&component_appid=").append(component_appid);
			}
			sb.append("#wechat_redirect");
			return sb.toString();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}



}
