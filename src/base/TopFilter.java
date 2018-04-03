package base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartResolver;

public class TopFilter implements Filter{

	@Autowired
	private MultipartResolver multipartResolver;  
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
			arg2.doFilter(getRequest(arg0), arg1); 
		// TODO Auto-generated method stub
		
	}
	
	private ServletRequest getRequest(ServletRequest req){  
	    String enctype = req.getContentType();  
	    if (enctype!=null && enctype.contains("multipart/form-data"))  
	        // 返回 MultipartHttpServletRequest 用于获取 multipart/form-data 方式提交的请求中上传的参数  
	        return multipartResolver.resolveMultipart((HttpServletRequest) req);  
	    else   
	        return req;  
	}  

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		ServletContext context = arg0.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
		multipartResolver = (MultipartResolver) ac.getBean("multipartResolver");
	}

}
