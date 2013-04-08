package com.quanturium.androcloud.requests;

public class AbstractTaskAnswer
{
	public final static int	RESULT_OK		= 1;
	public final static int	RESULT_ERROR	= 0;

	public final int		resultCode;
	public final Exception	e;

	public AbstractTaskAnswer(int resultCode, Exception e)
	{
		this.resultCode = resultCode;
		this.e = e;
	}
}
