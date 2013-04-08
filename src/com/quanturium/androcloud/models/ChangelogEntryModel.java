package com.quanturium.androcloud.models;

import java.util.ArrayList;
import java.util.List;

public class ChangelogEntryModel implements Comparable<ChangelogEntryModel>
{
	private int								versionCode	= 0;
	private String							versionName	= "";
	private String							date		= null;
	private List<ChangelogEntryItemModel>	items		= new ArrayList<ChangelogEntryItemModel>();

	public String getVersionName()
	{
		return versionName;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public void setVersionCode(int version)
	{
		this.versionCode = version;
	}

	public void addItem(ChangelogEntryItemModel item)
	{
		this.items.add(item);
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public List<ChangelogEntryItemModel> getItems()
	{
		return items;
	}

	@Override
	public int compareTo(ChangelogEntryModel another)
	{
		return this.versionCode > another.getVersionCode() ? -1 : 1;
	}
}
