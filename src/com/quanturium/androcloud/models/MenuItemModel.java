package com.quanturium.androcloud.models;

public class MenuItemModel
{
	private final String	name;
	private final int		icon;

	public MenuItemModel(String name, int icon)
	{
		this.name = name;
		this.icon = icon;
	}

	public String getName()
	{
		return this.name;
	}

	public int getIconRes()
	{
		return this.icon;
	}
}
