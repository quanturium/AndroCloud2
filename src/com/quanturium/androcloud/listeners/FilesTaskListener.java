package com.quanturium.androcloud.listeners;

import com.quanturium.androcloud.requests.FilesTaskAnswer;

public interface FilesTaskListener
{
	public void onTaskFinished(FilesTaskAnswer answer);
	public void onTaskCanceled();
}
