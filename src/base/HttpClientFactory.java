package base;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientFactory {
	
	HttpClientFactory(){
		try {
			// 读取host配置文件
			String path = this.getClass().getClassLoader().getResource("/").getPath();
			path = path.substring(1, path.indexOf("classes"));
			path += "/classes/config/hosts.properties";
			path = path.replaceAll("%20", " ");
			prop.load(new FileInputStream(path));
			
			Object maxTotal = prop.getProperty("max_total");
			Object maxPerRoute = prop.getProperty("max_per_route");
			Object reqTimeOut = prop.getProperty("req_time_out");
			Object connTimeOut = prop.getProperty("conn_time_out");
			Object socketTimeOut = prop.getProperty("socket_time_out");
			Object connCleanTime = prop.getProperty("conn_clean_time");
			
			if(maxTotal!=null){
				HttpClientFactory.MAX_TOTAL = (int)maxTotal;
			}
			if(maxPerRoute!=null){
				HttpClientFactory.MAX_PER_ROUTE = (int)maxPerRoute;
			}
			if(maxPerRoute!=null){
				HttpClientFactory.REQ_TIMEOUT = (int)reqTimeOut;
			}
			if(maxPerRoute!=null){
				HttpClientFactory.CONN_TIMEOUT = (int)connTimeOut;
			}
			if(maxPerRoute!=null){
				HttpClientFactory.SOCK_TIMEOUT = (int)socketTimeOut;
			}
			if(connCleanTime!=null){
				HttpClientFactory.CONN_CLEAN_TIME = (int)connCleanTime;
			}
	
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	private final Logger log = LogManager.getLogger("HttpClientFactory");
	private Properties prop = new Properties();
	private static  Integer MAX_TOTAL = 300; // 连接池最大连接数
	private static  Integer MAX_PER_ROUTE = 50; // 单个路由默认最大连接数
	private static  Integer REQ_TIMEOUT = 5 * 1000; // 请求超时时间ms
	private static  Integer CONN_TIMEOUT = 5 * 1000; // 连接超时时间ms
	private static  Integer SOCK_TIMEOUT = 10 * 1000; // 读取超时时间ms
	public static  Integer CONN_CLEAN_TIME = 10;//闲置清理时间
	private static HttpClientConnectionMonitorThread thread; // HTTP链接管理器线程
	private static CloseableHttpClient httpClient = null;
	private final static Object syncLock = new Object();
	private static RequestConfig requestConfig = null;

	public static HttpClientConnectionMonitorThread getThread() {
		return thread;
	}

	public static void setThread(HttpClientConnectionMonitorThread thread) {
		HttpClientFactory.thread = thread;
	}

	//添加对https的支持
	public static CloseableHttpClient createSimpleHttpClient() {
		SSLConnectionSocketFactory sf = SSLConnectionSocketFactory.getSocketFactory();
		return HttpClientBuilder.create().setSSLSocketFactory(sf).build();
	}

	//httpclient链接池获取长链接
	public static CloseableHttpClient createHttpClient() {
		synchronized (syncLock) {
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
			poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
			requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQ_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).setSocketTimeout(SOCK_TIMEOUT).build();
			HttpClientFactory.thread = new HttpClientConnectionMonitorThread(poolingHttpClientConnectionManager); // 管理
			return HttpClients.custom()
					.setRedirectStrategy(new DefaultRedirectStrategy())
					.setConnectionManager(poolingHttpClientConnectionManager)
					.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
					.setDefaultRequestConfig(requestConfig).build();
		}
	}
	
	//封装参数 发送请求
	public static HttpResponse doRequest(HttpRequestBase httpMethod) throws Exception{
		    if(HttpClientFactory.httpClient==null){
		    	HttpClientFactory.httpClient = HttpClientFactory.createHttpClient();
		    }
		    RequestConfig localConfig = RequestConfig.copy(requestConfig) 
		            .setCookieSpec(CookieSpecs.STANDARD)  
		            .build();  
		    httpMethod.setConfig(localConfig);
			return httpClient.execute(httpMethod);
	}
	

	
	
}
