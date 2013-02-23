package com.quanturium.androcloud2.fragments;

import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;

public class AboutFragment extends AbstractFragment
{
	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_about,"About", null, false, false);
	}

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
	}
}
