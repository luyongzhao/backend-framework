package weixin.popular.corpapi;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

public abstract class CorpBaseAPI {

	//企业微信基础地址
	protected static final String CORP_BASE_URI = "https://qyapi.weixin.qq.com";

	protected static final String OPEN_URI = "https://open.weixin.qq.com";

	protected static Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_JSON.toString());
	protected static Header xmlHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_XML.toString());
}
