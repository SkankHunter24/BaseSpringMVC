package base;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
/**
 * @工具类基类
 *
 */
public class BaseUtilObject{
	
	public SqlSessionFactory getSessionFactory()
	{
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return (SqlSessionFactory)wac.getBean("sqlSessionFactory");
	}
}
