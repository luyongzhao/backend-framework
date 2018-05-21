package weixin.popular.smallAppApi;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

public abstract class SmallAppBaseAPI {
	protected static final String BASE_URI = "https://api.weixin.qq.com";

	protected static Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_JSON.toString());
}
