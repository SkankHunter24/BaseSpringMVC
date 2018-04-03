package base;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;

public class RequestMatching {
	public static Properties prop = null;
	private static Logger log = LogManager.getLogger("SecurityInterceptor");

	public RequestMatching() {
		try {
			if (prop == null) {
				// 读取host配置文件
				prop = new Properties();
				String path = this.getClass().getClassLoader().getResource("/").getPath();
				path = path.substring(1, path.indexOf("classes"));
				path += "/classes/config/hosts.properties";
				path = path.replaceAll("%20", " ");
				prop.load(new FileInputStream(path));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @return true:继续执行springmvc过程 false:已重定向请求提交数据并返回客户端 springmvc过程终止
	 * @throws Exception
	 */
	public boolean matchingRequestCaseWriting(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean res = isRedirectRequest(request, handler);
		boolean isOnWriteServer = isOnWriteServer(request);

		// 如果有写入操作并且不在写服务器
		if (res && !isOnWriteServer) {
			HttpResponse newResponse = null;
			// 获取写服务地址
			String writeServer = prop.getProperty("write_server");
			String methodType = request.getMethod();
			String uri = request.getRequestURI().toString();
			if (methodType != null && "GET".equals(methodType)) {
				// 转发get
				// 拷贝各种请求信息 初始化转发请求
				String strpParameters = getRequestParametersStr(request);
				HttpGet get = new HttpGet(writeServer + uri + "?" + strpParameters);
				copyRequestHeaders(request, get);
				// 转向写入服务器 发起请求
				newResponse = HttpClientFactory.doRequest(get);
				copyDoResponse(newResponse, response);
			} else if (methodType != null && "POST".equals(methodType)) {
				// 转发post
				HttpPost post = new HttpPost(writeServer + uri);
				copyRequestParameters(request, post);
				copyRequestHeaders(request, post);
				newResponse = HttpClientFactory.doRequest(post);
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "不支持的请求类型!");
			}
			return false;
		}
		// 如果有写入操作并且在写入服务器
		else if (res && isOnWriteServer) {
			checkSerializableRequest(request, handler);
			return true;
		}
		// 如果无写入操作
		else {
			return false;
		}
	}

	/**
	 * 为转发的post请求填充参数
	 * 
	 * @param request
	 * @param post
	 * @throws Exception
	 */
	public void copyRequestParameters(HttpServletRequest request, HttpPost post) throws Exception {
		Map<String, String[]> parameters = request.getParameterMap();
		List<NameValuePair> params = Lists.newArrayList();
		Iterator iter = parameters.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			String[] val = (String[]) entry.getValue();
			StringBuffer sB = new StringBuffer();
			for (int i = 0; i < val.length; i++) {
				sB.append(val[i]);
			}
			params.add(new BasicNameValuePair(key.toString(), sB.toString()));
		}
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	}

	/**
	 * 获取请求的url参数
	 * 
	 * @param request
	 * @return 请求参数字符串
	 * @throws Exception
	 */
	public String getRequestParametersStr(HttpServletRequest request) throws Exception {
		Map<String, String[]> parameters = request.getParameterMap();
		List<NameValuePair> params = Lists.newArrayList();
		Iterator iter = parameters.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			String[] val = (String[]) entry.getValue();
			StringBuffer sB = new StringBuffer();
			for (int i = 0; i < val.length; i++) {
				sB.append(val[i]);
			}
			params.add(new BasicNameValuePair(key.toString(), sB.toString()));
		}
		// 拷贝请求服务器的头信息
		String strpParameters = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
		return strpParameters;
	}

	/**
	 * 拷贝请求头
	 * 
	 * @param request
	 *            原始请求
	 * @param httpMethod
	 *            新请求
	 */
	public void copyRequestHeaders(HttpServletRequest request, HttpRequestBase httpMethod) {
		Enumeration enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			// 转发请求服务器的cookie至写入服务器
			if (name.equals("cookie")) {
				String cookie = request.getHeader(name);
				httpMethod.addHeader("cookie", cookie);
			}
			// 不复制host
			if (name.equals("host")) {
			} else {
				httpMethod.addHeader(name, request.getHeader(name));
			}
		}
	}

	/**
	 * 转发
	 * 
	 * @param newResponse
	 *            新的请求结果
	 * @param response
	 *            原始response
	 * @throws Exception
	 */
	public void copyDoResponse(HttpResponse newResponse, HttpServletResponse response) throws Exception {
		// 写回客户端
		Header[] headers = newResponse.getAllHeaders();
		for (Header header : headers) {
			response.setHeader(header.getName(), header.getValue());
		}
		HttpEntity he = newResponse.getEntity();
		response.setContentLengthLong(he.getContentLength());
		response.setStatus(newResponse.getStatusLine().getStatusCode());
		InputStream it = he.getContent();
		OutputStream os = response.getOutputStream();

		byte flush[] = new byte[1024];
		int len = 0;
		while (0 <= (len = it.read(flush))) {
			os.write(flush, 0, len);
		}
		os.close();
		it.close();
	}

	/**
	 * 判断是否向写入服务器转发
	 * 
	 * @param request
	 * @param handler
	 * @return
	 */
	public boolean isRedirectRequest(HttpServletRequest request, Object handler) {
		try {
			HandlerMethod method = (HandlerMethod) handler;
			// 校验controller是否含DB写入操作
			boolean res = method.getMethodAnnotation(ParallelizationWrite.class) != null
					|| method.getMethodAnnotation(SerializableWrite.class) != null;

			return res;// 需要转发

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean isOnWriteServer(HttpServletRequest request) {
		// 判断当前服务器是否是写入服务器
		String writeServer = prop.getProperty("write_server");
		String url = request.getRequestURL().toString();
		url = fixLoaclhostRequestUrl(url);
		boolean thisIsWriteServer = url.startsWith(writeServer);
		return thisIsWriteServer;
	}

	public void checkSerializableRequest(HttpServletRequest request, HttpServletResponse response,Object handler) {
		try {
			HandlerMethod method = (HandlerMethod) handler;
			boolean res = method.getMethodAnnotation(SerializableWrite.class) != null;
			if (res) {
				// 初始化串行请求对象
				SerializableRequest serializableRequest = new SerializableRequest();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public String fixLoaclhostRequestUrl(String url) {
		if (url != null && url.contains("127.0.0.1")) {
			return url.replace("127.0.0.1", "localhost");
		}
		return url;
	}

}
