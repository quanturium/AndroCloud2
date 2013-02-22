package com.quanturium.androcloud2.fragments;


public class FilesMainFragment extends FilesAbstractFragment
{
	protected boolean willDisplayTrash()
	{
		return false;
	}
	
	protected String getTitle()
	{
		return "Files";
	}
}
