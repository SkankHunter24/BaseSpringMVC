package base;

import java.sql.Connection;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mvel2.util.ThisLiteral;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("BaseDaoSupport")
@Scope("prototype") 
public class BaseServiceSupport{
	@Resource(name="sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}
	
	public <T> T getMapper(Class<T> mapper){
			return this.getSqlSessionTemplate().getMapper(mapper);

	}
	
	/**
	 * 传入LOG发生的类名获取LOG实例
	 * logger.warn(String) 警告级别日志
	 * logger.info(String) 信息级别日志 
	 * logger.error(String)错误级别日志 
	 * @param className 类名
	 * @return LOG4J实例
	 * @throws Exception
	 */
	public Logger getLogger(String className) throws Exception
	{
	
		return LogManager.getLogger(className);
	}


}
