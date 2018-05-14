package weixin.popular.corpapi;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import weixin.popular.bean.Token;
import weixin.popular.client.LocalHttpClient;

public class CorpTokenAPI extends CorpBaseAPI
{

	/**
	 * 获取access_token
	 * 
	 * @param corpid
	 * @param corpsecret
	 * @return
	 */
	public static Token token(String corpid, String corpsecret)
	{
		HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(CORP_BASE_URI + "/cgi-bin/gettoken").addParameter("corpid", corpid)
				.addParameter("corpsecret", corpsecret).build();
		return LocalHttpClient.executeJsonResult(httpUriRequest, Token.class);
	}
}
