package com.quanturium.androcloud2.listeners;

public interface TransfertTaskListener
{
	public void onTaskStart(int id);
	public void onTaskProgress(int id, int percentage);
	public void onTaskFinish(int id, String result);
	public void onCancel(int id);
}
