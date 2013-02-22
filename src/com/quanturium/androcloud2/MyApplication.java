package com.quanturium.androcloud2;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import com.quanturium.androcloud2.requests.FilesTask;
import com.quanturium.androcloud2.services.MainService;

public class MyApplication extends Application
{
	private FilesTask	filesTask;

	public FilesTask getFilesTask()
	{
		return filesTask;
	}

	public void setFilesTask(FilesTask filesTask)
	{
		this.filesTask = filesTask;
	}

	public void sendToMainService(Intent intent)
	{
		ComponentName component = new ComponentName(this, MainService.class);
		intent.setComponent(component);

		startService(intent);
	}
}
