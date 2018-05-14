package weixin.popular.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class LocalHttpClient
{
	private static Log log = LogFactory.getLog(LocalHttpClient.class);

//	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100, 10);
	protected static HttpClient httpClient = HttpClientFactory.createHttpClientNew();

	private static Map<String, HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();

	public static void init(int maxTotal, int maxPerRoute)
	{
		httpClient = HttpClientFactory.createHttpClient(maxTotal, maxPerRoute);
	}

	/**
	 * 初始化 MCH HttpClient KeyStore
	 * 
	 * @param mch_id
	 * @param keyStoreFilePath
	 */
	public static void initMchKeyStore(String mch_id, String keyStoreFilePath)
	{
		
		HttpClient httpClient = httpClient_mchKeyStore.get(mch_id);
		if(httpClient != null)
		{
			log.info("httpClient is inited!");
			return;
		}
		
		try
		{
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			System.out.println("===="+keyStore.toString());
			File file = new File(keyStoreFilePath);
			FileInputStream instream = new FileInputStream(file);
			keyStore.load(instream, mch_id.toCharArray());
			instream.close();
			httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id,
					new String[] { "TLSv1" });
			httpClient_mchKeyStore.put(mch_id, httpClient);
		} catch (KeyStoreException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (CertificateException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (IOException e)
		{
			e.printStackTrace();
			log.error(e);
		}
	}

	public static HttpResponse execute(HttpUriRequest request)
	{
		try
		{
			return httpClient.execute(request);
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (IOException e)
		{
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler)
	{
		try
		{
			return httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (IOException e)
		{
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	/**
	 * 数据返回自动JSON对象解析
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz)
	{
		return execute(request, JsonResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * 数据返回自动XML对象解析
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T executeXmlResult(HttpUriRequest request, Class<T> clazz)
	{
		return execute(request, XmlResponseHandler.createResponseHandler(clazz));
	}

	/**
	 * MCH keyStore 请求 数据返回自动XML对象解析
	 * 
	 * @param mch_id
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T keyStoreExecuteXmlResult(String mch_id, HttpUriRequest request, Class<T> clazz)
	{
		try
		{
			HttpClient client = httpClient_mchKeyStore.get(mch_id);
			log.info("mch_id="+mch_id+",client="+client);
			return client.execute(request, XmlResponseHandler.createResponseHandler(clazz));
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			log.error(e);
		} catch (IOException e)
		{
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}
}
