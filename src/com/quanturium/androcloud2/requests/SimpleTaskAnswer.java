package com.quanturium.androcloud2.requests;

public class SimpleTaskAnswer
{
	public final static int	RESULT_OK		= 1;
	public final static int	RESULT_ERROR	= 0;

	public final int		resultCode;
	public final Exception	e;

	public SimpleTaskAnswer(int resultCode, Exception e)
	{
		this.resultCode = resultCode;
		this.e = e;
	}
}
