package com.quanturium.androcloud.requests;

import java.io.Serializable;


public class FilesRequestAnswer extends AbstractTaskAnswer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final int				page;

	public FilesRequestAnswer(int resultCode, Exception e, int page)
	{
		super(resultCode, e);
		this.page = page;
	}
}
