package com.quanturium.androcloud2.requests;


public class FilesTaskAnswer extends SimpleTaskAnswer
{
	public final int				page;

	public FilesTaskAnswer(int resultCode, Exception e, int page)
	{
		super(resultCode, e);
		this.page = page;
	}
}
