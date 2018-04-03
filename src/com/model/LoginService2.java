package com.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dao.TUserMapper;
import com.model.vo.TUser;

import base.BaseServiceSupport;

@Service("LoginService2")
@Scope("prototype")

public class LoginService2 extends BaseServiceSupport{
	public List getUser(String userID){
		TUserMapper mapper  = this.getMapper(TUserMapper.class);
		return mapper.selectAll();
	}


	public void insertUser() throws Exception{
		TUserMapper mapper  = this.getMapper(TUserMapper.class);
	
		List list = mapper.selectAll();
		TUser user = new TUser();
		user.setId("333");
		user.setuClassId("1");
		user.setuName("111");
		user.setuPassword("111");

		mapper.insert(user);
		
		user.setId("444"); 
		user.setuClassId("1");
		user.setuName("222");  
		user.setuPassword("222");
		mapper.insert(user); 
		list = mapper.selectAll();   
		String s = null;
		s.split("1");

	} 
	
	
  
}
