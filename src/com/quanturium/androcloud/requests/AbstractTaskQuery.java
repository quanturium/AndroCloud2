package com.quanturium.androcloud.requests;

import java.io.Serializable;


public class AbstractTaskQuery implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final String username;
	public final String password;
	
	public AbstractTaskQuery(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
}
