package weixin.popular.corpapi;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import weixin.popular.bean.Ticket;
import weixin.popular.client.LocalHttpClient;

/**
 * JSAPI ticket
 * @author LiYi
 *
 */
public class CorpTicketAPI extends CorpBaseAPI {

	/**
	 * 获取 jsapi_ticket
	 * @param access_token
	 * @return
	 */
	public static Ticket ticket(String access_token){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri(CORP_BASE_URI + "/cgi-bin/get_jsapi_ticket")
				.addParameter("access_token",access_token)
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,Ticket.class);
	}
}
