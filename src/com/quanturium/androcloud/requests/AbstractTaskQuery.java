package com.quanturium.androcloud.requests;


public class AbstractTaskQuery
{
	public final String username;
	public final String password;
	
	public AbstractTaskQuery(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
}
