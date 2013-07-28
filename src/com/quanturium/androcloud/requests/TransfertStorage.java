package com.quanturium.androcloud.requests;

import java.util.ArrayList;
import java.util.List;

import com.quanturium.androcloud.models.TransfertTaskModel;
import com.quanturium.androcloud.models.TransfertTaskModel.Type;
import com.quanturium.androcloud.tools.Logger;

public class TransfertStorage
{
	private static TransfertStorage		instance;
	private List<AbstractTransfertTask>			tasks			= new ArrayList<AbstractTransfertTask>();
	private List<TransfertNotification>	notifications	= new ArrayList<TransfertNotification>();

	private TransfertStorage()
	{
	}

	public static TransfertStorage getInstance()
	{
		if (null == instance)
		{
			instance = new TransfertStorage();
		}

		return instance;
	}

	public AbstractTransfertTask getTask(int i)
	{
		return tasks.get(i);
	}

	public List<TransfertTaskModel> getTasksAsList()
	{
		List<TransfertTaskModel> list = new ArrayList<TransfertTaskModel>();

		for (int i = 0; i < tasks.size(); i++)
		{
			AbstractTransfertTask t = tasks.get(i);

			if (t != null && !t.isCancelled())
			{
				int id = t.getId();
				String name = t.getname();
				int progress = t.getProgress();
				TransfertTaskModel.Type type = null;

				if (t instanceof UploadTransfertTask)
					type = Type.UPLOAD;
				else if (t instanceof DownloadTransfertTask)
					type = Type.DOWNLOAD;

				list.add(new TransfertTaskModel(id, name, progress, type));
			}
		}

		return list;
	}

	public int[] countTransfert()
	{
		int i;
		int n[] = { 0, 0 };

		for (i = 0; i < tasks.size(); i++)
		{
			AbstractTransfertTask task = tasks.get(i);

			if (task instanceof UploadTransfertTask && !task.isCancelled())
				n[0]++;
			else if (task instanceof DownloadTransfertTask && !task.isCancelled())
				n[1]++;
		}

		return n;
	}
	
	public Type getTaskType(int id)
	{
		if(getTask(id) instanceof UploadTransfertTask)
			return Type.UPLOAD;
		else if(getTask(id) instanceof DownloadTransfertTask)
			return Type.DOWNLOAD;
		else
			return null;
	}

	public int addTask(AbstractTransfertTask task)
	{
		this.tasks.add(task);
		int id = this.tasks.indexOf(task);

		task.setId(id);
		Logger.v("Task #" + id, "added to storage");
		return id;
	}

	public void removeTask(int i)
	{
		tasks.set(i, null);
	}

	public TransfertNotification getNotification(int i)
	{
		return notifications.get(i);
	}

	public void addNotification(int index, TransfertNotification notification)
	{
		this.notifications.add(index, notification);
	}

	public void removeNotification(int i)
	{
		notifications.set(i, null);
	}
	
	public int countNotifications()
	{
		int i;
		int n = 0;

		for (i = 0; i < notifications.size(); i++)
		{
			TransfertNotification notification = notifications.get(i);
			
			if(notification != null)
				n++;
		}

		return n;
	}
	
	public void kill()
	{
		for(int i = 0 ; i < tasks.size() ; i++)
		{
			AbstractTransfertTask task = tasks.get(i);
			
			if(task != null)
				task.cancel(true);
		}
		
		for (int i = 0; i < notifications.size(); i++)
		{
			TransfertNotification notification = notifications.get(i);
			
			if(notification != null)
				notification.cancel();
		}
	}
}
