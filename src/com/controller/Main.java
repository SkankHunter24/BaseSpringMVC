package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.LoginService2;

import base.BaseControllerSupport;
import base.SerializableWrite;

@Controller
@Scope("prototype") 

public class Main extends BaseControllerSupport{
	@Autowired
	LoginService2 loginService2;
	public String Welcome()
	{
		return "success";
	}
	
	
	@RequestMapping("/login2")
	@SerializableWrite
	public String Login2(String id,String pwd)
	{
		try
		{
			HttpSession session=this.getRequest().getSession();
	 	
			if(session.getAttribute("USER_ID")!=null){
				System.out.println(session.getAttribute("USER_ID").toString());
			}
			else{ 
				session.setAttribute("USER_ID","123");
				System.out.println("写入SESSION");
			}
			this.getLogger(this.getClass().getName()).info("用户登录,ID："+id);
			List list = this.loginService2.getUser(id);

			loginService2.insertUser();
			//处理
			return  "login/login";
		} 
		catch(Exception e)
		{
			return "faild";		
		}
		
	}
}
 