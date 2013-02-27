package com.quanturium.androcloud2.services;

import java.io.File;
import java.util.ArrayList;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.MyExceptionHandler;
import com.quanturium.androcloud2.listeners.TransfertTaskListener;
import com.quanturium.androcloud2.requests.DownloadTransfertTask;
import com.quanturium.androcloud2.requests.DownloadTransfertTaskQuery;
import com.quanturium.androcloud2.requests.AbstractTaskQuery;
import com.quanturium.androcloud2.requests.TransfertNotification;
import com.quanturium.androcloud2.requests.TransfertStorage;
import com.quanturium.androcloud2.requests.AbstractTransfertTask;
import com.quanturium.androcloud2.requests.UploadTransfertTask;
import com.quanturium.androcloud2.requests.UploadTransfertTaskQuery;
import com.quanturium.androcloud2.tools.Prefs;
import com.quanturium.androcloud2.tools.Tools;

public class MainService extends Service implements TransfertTaskListener
{
	private final static String		TAG		= "MainService";
	private final TransfertStorage	storage	= TransfertStorage.getInstance();

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		Log.i(TAG, "Service created");
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null)
		{
			String action = intent.getAction();

			if (action != null)
			{

				if (action.equals(Constants.INTENT_ACTION_DOWNLOAD))
				{
					String url = intent.getStringExtra(Constants.DOWNLOAD_URL_KEY);
					String path = intent.getStringExtra(Constants.DOWNLOAD_PATH_KEY);
					String name = intent.getStringExtra(Constants.DOWNLOAD_NAME_KEY);

					if (url != null && path != null && name != null)
					{
						actionStartDownload(url, path, name);
					}
				}
				else if (action.equals(Constants.INTENT_ACTION_UPLOAD))
				{
					// TODO handle intent here
					String fileString = intent.getDataString();

					if (fileString != null)
					{
						Uri fileUri = Uri.parse(fileString);

						if (fileUri != null)
						{
							File file = Tools.uriToFile(this, fileUri);
							actionStartUpload(file);
						}
					}
				}
				else if (action.equals(Intent.ACTION_SEND)) // from share button in an other application
				{
					Uri fileUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM); // Handle single file being sent

					if (fileUri == null)
					{
						Log.e(TAG, "Sharing file's uri is null");
						Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show();
					}
					else
					{
						File file = Tools.uriToFile(this, fileUri);

						Log.i(TAG, "Sharing file's path : " + file.toString());

						actionStartUpload(file);
					}
				}
				else if (action.equals(Intent.ACTION_SEND_MULTIPLE)) // from share button in an other application
				{
					ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM); // Handle multiple files being sent

					if (fileUris == null || fileUris.size() == 0)
					{
						Log.e(TAG, "Sharing file's uri is null");
						Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show();
					}
					else
					{
						File file = null;

						for (Uri fileUri : fileUris)
						{
							file = Tools.uriToFile(this, fileUri);

							Log.i(TAG, "Sharing file's path : " + file.toString());

							actionStartUpload(file);
						}
					}
				}
			}
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		Log.i(TAG, "Service destroyed");
		storage.kill();
		super.onDestroy();
	}

	private void actionStartDownload(String url, String path, String name)
	{
		AbstractTransfertTask task = new DownloadTransfertTask(this);
		int id = storage.addTask(task);
		task.setId(id);

		File file = new File(path, name);
		boolean isDisplayed = Prefs.getPreferences(getApplicationContext()).getBoolean(Prefs.DOWNLOAD_NOTIFICATION_SHOW, true);
		TransfertNotification notification = new TransfertNotification(getApplicationContext(), id, TransfertNotification.TYPE_DOWNLOAD, "Url " + file.getName(), isDisplayed);
		storage.addNotification(id, notification);
				
		AbstractTaskQuery query = new DownloadTransfertTaskQuery(Prefs.getPreferences(getApplicationContext()).getString(Prefs.EMAIL, ""), Prefs.getPreferences(getApplicationContext()).getString(Prefs.PASSWORD, ""), file, url);
		
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
	}

	private void actionStartUpload(File file)
	{
		AbstractTransfertTask task = new UploadTransfertTask(this);
		int id = storage.addTask(task);
		task.setId(id);

		boolean isDisplayed = Prefs.getPreferences(getApplicationContext()).getBoolean(Prefs.UPLOAD_NOTIFICATION_SHOW, true);
		TransfertNotification notification = new TransfertNotification(getApplicationContext(), id, TransfertNotification.TYPE_UPLOAD, "File " + file.getName(), isDisplayed);
		storage.addNotification(id, notification);

		AbstractTaskQuery query = new UploadTransfertTaskQuery(Prefs.getPreferences(getApplicationContext()).getString(Prefs.EMAIL, ""), Prefs.getPreferences(getApplicationContext()).getString(Prefs.PASSWORD, ""), file);

		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
	}

	private void sendCountTransfert()
	{
		int[] n = storage.countTransfert();

		Intent i = new Intent(Constants.INTENT_ACTION_TRANSFERTS_RUNNING);
		i.putExtra(Constants.INTENT_ACTIONN_TASKS_RUNNING_NUMBER_KEY, n);
		sendBroadcast(i);

		Prefs.getPreferences(getApplicationContext()).edit().putInt(Prefs.NUMBER_BACKGROUND_UPLOADS_RUNNING, n[0]).commit();
		Prefs.getPreferences(getApplicationContext()).edit().putInt(Prefs.NUMBER_BACKGROUND_DOWNLOADS_RUNNING, n[1]).commit();
	}

	/**
	 * CALLBACKS
	 */

	@Override
	public void onTaskStart(int id)
	{
		Log.i(TAG, "Task's callback : #" + id + " started");

		if (storage.countNotifications() == 1)
		{
			Notification dummy = new Notification(0, null, System.currentTimeMillis());
			dummy.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
			startForeground(1337, dummy);
			Log.i(TAG, "startForeground");
		}

		storage.getNotification(id).start();
		sendCountTransfert();
	}

	@Override
	public void onTaskProgress(int id, int percent)
	{
		Log.i(TAG, "Task's callback : #" + id + " progress = " + percent + "%");

		storage.getNotification(id).update(percent);
	}

	@Override
	public void onTaskFinish(int id, String result)
	{
		Log.i(TAG, "Task's callback : #" + id + " finished");

		storage.getNotification(id).finish(result);
		stopForeground(true);

		storage.removeTask(id);
		storage.removeNotification(id);

		int count = storage.countNotifications();

		if (count == 0)
		{
			Log.i(TAG, "stop foreground");
			stopForeground(true);
		}

		sendCountTransfert();
	}

	@Override
	public void onCancel(int id)
	{
		Log.i(TAG, "Task's callback : #" + id + " canceled");

		storage.getNotification(id).cancel();

		storage.removeTask(id);
		storage.removeNotification(id);

		int count = storage.countNotifications();

		if (count == 0)
		{
			Log.i(TAG, "stop foreground");
			stopForeground(true);
		}

		sendCountTransfert();
	}
}
