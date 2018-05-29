package com.mobike.iotcloud.backend.framework.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HtmlResponseHandler {

	private static Logger logger = LoggerFactory.getLogger(HtmlResponseHandler.class);

	public static <String> ResponseHandler<String> createResponseHandler(){
		return new JsonResponseHandlerImpl<String>(null);
	}

	public static class JsonResponseHandlerImpl<T> extends LocalResponseHandler implements ResponseHandler<T> {
		

		public JsonResponseHandlerImpl(String uriId){

			this.uriId = uriId;

		}

		public T handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String str = EntityUtils.toString(entity,"utf-8");
                logger.info("URI[{}] elapsed time:{} ms RESPONSE DATA:{}",super.uriId,System.currentTimeMillis()-super.startTime,str);
                return (T)str;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
		}
		
	}
}
