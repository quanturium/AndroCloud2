package com.quanturium.androcloud;

public class FragmentInitParams
{
	public final String title;
	public final String subtitle;
	public final boolean isActionBarNavigationModeList;
	public final boolean hasOptionsMenu;
	public final int layout;
	
	public FragmentInitParams(int layout, String title, String subtitle, boolean isActionBarNavigationModeList, boolean hasOptionsMenu)
	{
		this.layout = layout;
		this.title = title;
		this.subtitle = subtitle;
		this.isActionBarNavigationModeList = isActionBarNavigationModeList;
		this.hasOptionsMenu = hasOptionsMenu;
	}
}
