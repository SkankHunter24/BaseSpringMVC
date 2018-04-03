package com.controller;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import base.BaseControllerSupport;

@Controller
@Scope("prototype") 

public class TestAction extends BaseControllerSupport{
	private static final long serialVersionUID = 1L;  
	
	public String test()
	{ 
		try
		{
			return "success";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "faild";
		}
	
	}
	
	
}
