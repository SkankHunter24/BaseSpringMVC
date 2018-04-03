package com.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dao.TUserMapper;
import com.model.vo.TUser;

import base.BaseServiceSupport;

@Service("LoginService")
@Scope("prototype")

public class LoginService extends BaseServiceSupport{
	public List getUser(String userID){
		TUserMapper mapper  = this.getMapper(TUserMapper.class);
		return mapper.selectAll();
	}

	public void insertUser() throws Exception{
		TUser user = new TUser();
		user.setId("111"); 
		user.setuClassId("1");
		user.setuName("111"); 
		user.setuPassword("111"); 
		TUserMapper mapper  = this.getMapper(TUserMapper.class);
		mapper.insert(user); 
		
		user.setId("222");
		user.setuClassId("1"); 
		user.setuName("222");
		user.setuPassword("222");
		mapper.insert(user);
	}
	
	
	public void insertUser2() throws Exception{
		TUser user = new TUser();
		user.setId("111");
		user.setuClassId("1");
		user.setuName("111");
		user.setuPassword("111");
		TUserMapper mapper  = this.getMapper(TUserMapper.class);
		mapper.insert(user);
		
		user.setId("222");
		user.setuClassId("1");
		user.setuName("222");
		user.setuPassword("222");
		mapper.insert(user);
	}
}
