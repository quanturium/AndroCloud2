package com.quanturium.androcloud2.listeners;

import com.quanturium.androcloud2.requests.FilesTaskAnswer;

public interface FilesTaskListener
{
	public void onTaskFinished(FilesTaskAnswer answer);
	public void onTaskCanceled();
}
