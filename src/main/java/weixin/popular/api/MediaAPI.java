package weixin.popular.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import weixin.popular.bean.Media;
import weixin.popular.bean.MediaType;
import weixin.popular.bean.UploadimgResult;
import weixin.popular.client.LocalHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.util.UUID;

public class MediaAPI extends BaseAPI {

	/**
	 * 上传媒体文件
	 * 媒体文件在后台保存时间为3天，即3天后media_id失效。
	 * @param access_token
	 * @param mediaType
	 * @param media  	多媒体文件有格式和大小限制，如下：
						图片（image）: 128K，支持JPG格式
						语音（voice）：256K，播放长度不超过60s，支持AMR\MP3格式
						视频（video）：1MB，支持MP4格式
						缩略图（thumb）：64KB，支持JPG格式
	 * @return
	 */
	public static Media mediaUpload(String access_token,MediaType mediaType,File media){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/upload");
		FileBody bin = new FileBody(media);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
        		 .addPart("media", bin)
                 .addTextBody("access_token", access_token)
                 .addTextBody("type",mediaType.uploadType())
                 .build();
        httpPost.setEntity(reqEntity);
		return LocalHttpClient.executeJsonResult(httpPost,Media.class);
	}

	/**
	 * 上传媒体文件
	 * 媒体文件在后台保存时间为3天，即3天后media_id失效。
	 * @param access_token
	 * @param mediaType
	 * @param inputStream  	多媒体文件有格式和大小限制，如下：
						图片（image）: 128K，支持JPG格式
						语音（voice）：256K，播放长度不超过60s，支持AMR\MP3格式
						视频（video）：1MB，支持MP4格式
						缩略图（thumb）：64KB，支持JPG格式
	 * @return
	 */
	public static Media mediaUpload(String access_token,MediaType mediaType,InputStream inputStream){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/upload");
        @SuppressWarnings("deprecation")
        InputStreamBody inputStreamBody = new InputStreamBody(inputStream, mediaType.mimeType(),"temp."+mediaType.fileSuffix());
		HttpEntity reqEntity = MultipartEntityBuilder.create()
        		 .addPart("media",inputStreamBody)
                 .addTextBody("access_token", access_token)
                 .addTextBody("type",mediaType.uploadType())
                 .build();
        httpPost.setEntity(reqEntity);
		return LocalHttpClient.executeJsonResult(httpPost,Media.class);
	}


	/**
	 * 上传媒体文件
	 * 媒体文件在后台保存时间为3天，即3天后media_id失效。
	 * @param access_token
	 * @param mediaType
	 * @param uri  	多媒体文件有格式和大小限制，如下：
						图片（image）: 128K，支持JPG格式
						语音（voice）：256K，播放长度不超过60s，支持AMR\MP3格式
						视频（video）：1MB，支持MP4格式
						缩略图（thumb）：64KB，支持JPG格式
	 * @return
	 */
	public static Media mediaUpload(String access_token,MediaType mediaType,URI uri){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/upload");
		CloseableHttpClient tempHttpClient = HttpClients.createDefault();
		try {
			HttpEntity entity = tempHttpClient.execute(RequestBuilder.get().setUri(uri).build()).getEntity();
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					 .addBinaryBody("media",EntityUtils.toByteArray(entity),ContentType.get(entity),"temp."+mediaType.fileSuffix())
			         .addTextBody("access_token", access_token)
			         .addTextBody("type",mediaType.uploadType())
			         .build();
			httpPost.setEntity(reqEntity);
			return LocalHttpClient.executeJsonResult(httpPost,Media.class);
		} catch (UnsupportedCharsetException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				tempHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 下载多媒体
	 * 视频文件不支持下载
	 * @param access_token
	 * @param media_id
	 * @return
	 */
	public static byte[] mediaGet(String access_token,String media_id){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
					.setUri(BASE_URI+"/cgi-bin/media/get")
					.addParameter("access_token", access_token)
					.addParameter("media_id", media_id)
					.build();
		HttpResponse httpResponse = LocalHttpClient.execute(httpUriRequest);
		try {
			return EntityUtils.toByteArray(httpResponse.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传图文消息内的图片获取URL
	 * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
	 * @param access_token
	 * @param media
	 * @return
	 */
	public static UploadimgResult mediaUploadimg(String access_token, File media){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/uploadimg");
		FileBody bin = new FileBody(media);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
        		 .addPart("media", bin)
                 .addTextBody("access_token", access_token)
                 .build();
        httpPost.setEntity(reqEntity);
		return LocalHttpClient.executeJsonResult(httpPost,UploadimgResult.class);
	}

	/**
	 * 上传图文消息内的图片获取URL
	 * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
	 * @param access_token
	 * @param inputStream
	 * @return
	 */
	public static UploadimgResult mediaUploadimg(String access_token, InputStream inputStream){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/uploadimg");
		InputStreamBody inputStreamBody =  new InputStreamBody(inputStream, ContentType.MULTIPART_FORM_DATA, UUID.randomUUID().toString()+".jpg");
		HttpEntity reqEntity = MultipartEntityBuilder.create()
        		 .addPart("media",inputStreamBody)
                 .addTextBody("access_token", access_token)
                 .build();
        httpPost.setEntity(reqEntity);
		return LocalHttpClient.executeJsonResult(httpPost,UploadimgResult.class);
	}


	/**
	 * 上传图文消息内的图片获取URL
	 * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
	 * @param access_token
	 * @param uri
	 * @return
	 */
	public static UploadimgResult mediaUploadimg(String access_token, URI uri){
		HttpPost httpPost = new HttpPost(MEDIA_URI+"/cgi-bin/media/uploadimg");
		CloseableHttpClient tempHttpClient = HttpClients.createDefault();
		try {
			HttpEntity entity = tempHttpClient.execute(RequestBuilder.get().setUri(uri).build()).getEntity();
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					 .addBinaryBody("media",EntityUtils.toByteArray(entity),ContentType.get(entity),UUID.randomUUID().toString()+".jpg")
			         .addTextBody("access_token", access_token)
			         .build();
			httpPost.setEntity(reqEntity);
			return LocalHttpClient.executeJsonResult(httpPost,UploadimgResult.class);
		} catch (UnsupportedCharsetException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				tempHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
