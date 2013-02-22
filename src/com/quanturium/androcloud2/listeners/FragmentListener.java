package com.quanturium.androcloud2.listeners;



public interface FragmentListener
{
	public void onMenuItemSelected(int position);
	public void onFilesItemSelected(int itemId);
	public void onHomeItemSelected(int position);
	public void onAddFilesMenuClicked();
	public void onAddFilesActionClicked();	
	public void onAddFilesItemSelected(int position);
	public void onUserLoggedOut();
}
