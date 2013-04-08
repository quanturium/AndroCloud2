package com.quanturium.androcloud.listeners;



public interface FragmentListener
{
	public void onMenuItemSelected(int position);
	public void onFilesItemSelected(int itemId);
	public void onHomeItemSelected(int position);
	public void onAddFileMenuClicked();
	public void onAddFileActionClicked();	
	public void onAddFileItemSelected(int position);
	public void onUserLoggedOut();
}
