package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.model.LoginService;

import base.BaseControllerSupport;
import base.ParallelizationWrite;

@Controller
@Scope("prototype") 

public class LoginAction extends BaseControllerSupport{
	private static final long serialVersionUID = 1L;  
	@Autowired
	LoginService loginService;
	
	@RequestMapping("/welcome")
	public ModelAndView Welcome()
	{
		return new ModelAndView("login/login");
	}
	
	@RequestMapping("/login")
	//
	public String Login(String id,String pwd)
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
			List list = this.loginService.getUser(id);

			loginService.insertUser();
			//处理
			return  "login/login";
		} 
		catch(Exception e)
		{
			return "faild";		
		}
		
	}
	

	
	@RequestMapping("/write")
	@ParallelizationWrite
	public ModelAndView writeTest()
	{
		try
		{
			String r = "";
			HttpSession session=this.getRequest().getSession();
			String nowUrl = this.getRequest().getRequestURL().toString();
			r = r + "你现在在："+nowUrl + "\r\n";
			System.out.println("你现在在："+nowUrl);
			
			if(session.getAttribute("test")!=null){
				System.out.println("session已存在：");
				r = r + "session已存在：";
				System.out.println(session.getAttribute("test").toString());
				r = r + session.getAttribute("test").toString() + "\r\n";
			}
			else{
				session.setAttribute("test","this is session value");
				System.out.println("写入session");
				r = r + "写入session" + "\r\n";
			}
			 ModelAndView mav = new ModelAndView();
			 mav.addObject("r",r);
			 mav.setViewName("test");
			//处理
			return  mav;
		}
		catch(Exception e)
		{
			return null;		
		}
		
	}
	
	
	@RequestMapping("/read")

	public ModelAndView readTest()
	{
		try
		{
			String r = "";
			HttpSession session=this.getRequest().getSession();
			String nowUrl = this.getRequest().getRequestURL().toString();
			r = r + "你现在在："+nowUrl + "\r\n";
			System.out.println("你现在在："+nowUrl);
			
			if(session.getAttribute("test")!=null){
				System.out.println("session已存在：");
				r = r + "session已存在：";
				System.out.println(session.getAttribute("test").toString());
				r = r + session.getAttribute("test").toString() + "\r\n";
			}
			else{
				session.setAttribute("test","this is session value");
				System.out.println("写入session");
				r = r + "写入session" + "\r\n";
			}
			ModelAndView mav = new ModelAndView();
			 mav.addObject("r",r);
			 mav.setViewName("test");
			//处理
			return  mav;
		}
		catch(Exception e)
		{
			return null;		
		}
		
	}
}
