package com.lyz.backend.framework.http.client;

import com.lyz.backend.framework.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonResponseHandler{

	private static Logger logger = LoggerFactory.getLogger(JsonResponseHandler.class);

	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz){
		return new JsonResponseHandlerImpl<T>(null,clazz);
	}

	public static class JsonResponseHandlerImpl<T> extends LocalResponseHandler implements ResponseHandler<T> {
		
		private Class<T> clazz;
		
		public JsonResponseHandlerImpl(String uriId, Class<T> clazz) {
			this.uriId = uriId;
			this.clazz = clazz;
		}

		public T handleResponse(HttpResponse response){

			try{
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					String str = EntityUtils.toString(entity,"utf-8");
					logger.info("URI[{}] elapsed time:{} ms RESPONSE DATA:{}",super.uriId,System.currentTimeMillis()-super.startTime,str);
					return JsonUtil.parseJSONObject(str, clazz);
				} else {
					//throw new ClientProtocolException("Unexpected response status: " + status);
					logger.error("Unexpected response status: {}",status);
					return null;
				}
			}catch (IOException e){

				logger.error("",e);

				return null;
			}

		}

	}
}
