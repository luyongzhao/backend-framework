package com.mobike.iotcloud.backend.framework.http.client;

import com.mobike.iotcloud.backend.framework.controller.bean.RestResponse;
import com.mobike.iotcloud.backend.framework.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回数据错误处理
 *
 * @author luyongzhao
 *
 */
public abstract class ResultErrorHandler {
	
	private static Logger logger = LoggerFactory.getLogger(ResultErrorHandler.class);

	/**
	 * 数据错误检查
	 * @param result 返回数据
	 * @return boolean
	 */
	private boolean isError(Object result) {

		RestResponse baseResult = (RestResponse)result;

		return !baseResult.isSuccess();

	}
	
	protected void doHandle(String uriId, HttpUriRequest request, Object result){
		if(this.isError(result)){
			String content = null;
			if(request instanceof HttpEntityEnclosingRequestBase){
				HttpEntityEnclosingRequestBase request_base = (HttpEntityEnclosingRequestBase)request;
				HttpEntity entity = request_base.getEntity();
				//MULTIPART_FORM_DATA 请求类型判断
				if(entity.getContentType().toString().indexOf(ContentType.MULTIPART_FORM_DATA.getMimeType()) == -1){
					try {
						content = EntityUtils.toString(entity);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(logger.isErrorEnabled()){
					logger.error("URI[{}] {} Content:{} Result:{}",
							uriId,
							request.getURI(),
							content == null ? "multipart_form_data" : content,
							result == null? null : JsonUtil.toJsonString(result));
				}
			}
			this.handle(uriId,request.getURI().toString(),content,result);
		}
	}
	
	/**
	 * 数据错误处理
	 * @param uriId	uriId
	 * @param uri	uri
	 * @param requestEntity 请求entity数据
	 * @param result 返回的数据
	 */
	protected abstract void handle(String uriId,String uri,String requestEntity,Object result);
}