package com.quanturium.androcloud.listeners;

import com.quanturium.androcloud.requests.FilesRequestAnswer;

public interface FilesTaskListener
{
	public void onTaskFinished(FilesRequestAnswer answer);
	public void onTaskCanceled();
}
