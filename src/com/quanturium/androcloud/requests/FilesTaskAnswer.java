package com.quanturium.androcloud.requests;


public class FilesTaskAnswer extends AbstractTaskAnswer
{
	public final int				page;

	public FilesTaskAnswer(int resultCode, Exception e, int page)
	{
		super(resultCode, e);
		this.page = page;
	}
}
