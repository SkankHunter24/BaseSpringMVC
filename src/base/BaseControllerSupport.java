package base;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartResolver;


/**
 * Action基类 
 *
 */
public class BaseControllerSupport{
	private static final long serialVersionUID = 1L;  
	@Autowired 
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	/**
	 * 传入LOG发生的类名获取LOG实例
	 * logger.warn(String) 警告级别日志
	 * logger.info(String) 信息级别日志 
	 * logger.error(String)错误级别日志 
	 * @param className 类名
	 * @return LOG4J实例
	 * @throws Exception
	 */
	public Logger getLogger(String className)
	{

		return LogManager.getLogger(className);
	}

	/**
	 * 获取当前会话request
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 获取当前会话response
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	
	
	/**
	 *给当前会话的response 返回一段文本
	 * @return HttpServletResponse
	 */
	public void textToResponse(HttpServletResponse response , String text) throws Exception{
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(text);
		out.flush();
		out.close();
	}

}
