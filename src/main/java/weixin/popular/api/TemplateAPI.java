package weixin.popular.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import weixin.popular.bean.templatemessage.IndustryInfo;
import weixin.popular.bean.templatemessage.TemplateList;
import weixin.popular.client.LocalHttpClient;

public class TemplateAPI extends BaseAPI {
    
	
	public static TemplateList allTemplate(String access_token){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(BASE_URI+"/cgi-bin/template/get_all_private_template")
				.addParameter("access_token", access_token)
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,TemplateList.class);
	}
	
	
	public static IndustryInfo getIndustry(String access_token){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(BASE_URI+"/cgi-bin/template/get_industry")
				.addParameter("access_token", access_token)
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,IndustryInfo.class);
	}


}