package com.quanturium.androcloud;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AnimatedLinearLayout extends LinearLayout
{
	public AnimatedLinearLayout(Context context)
	{
		super(context);
	}

	public AnimatedLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public AnimatedLinearLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public float getXFraction()
	{
		return getX() / getWidth(); // TODO: guard divide-by-zero
	}

	public void setXFraction(float xFraction)
	{
		final int width = getWidth();
		setX((width > 0) ? (xFraction * width) : -9999);
	}
}
