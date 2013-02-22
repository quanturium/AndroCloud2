package com.quanturium.androcloud2.requests;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;

public class TransfertNotification
{
	private final Context				context;
	private final int					id;
	private final int					type;
	private final String				description;
	private final boolean				isDiplayed;

	private NotificationManager			mNotificationManager;
	private NotificationCompat.Builder	mBuilder;

	public final static int				TYPE_UPLOAD		= 0;
	public final static int				TYPE_DOWNLOAD	= 1;

	public TransfertNotification(Context context, int id, int type, String description, boolean isDisplayed)
	{
		this.context = context;
		this.id = 100 + id;
		this.type = type;
		this.description = description;
		this.isDiplayed = isDisplayed;

		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
	}

	public int getId()
	{
		return this.id;
	}

	public void start()
	{
		if (isDiplayed)
		{
			String contentTitle = type == TYPE_DOWNLOAD ? "Download in progress" : "Upload in progress";
			String ticker = type == TYPE_DOWNLOAD ? "Download started" : "Upload started";
			int largeIcon = type == TYPE_DOWNLOAD ? R.drawable.ic_stat_download_running2 : R.drawable.ic_stat_upload_running2;

			mBuilder.setSmallIcon(R.drawable.ic_stat_transfert_ticker);
			mBuilder.setContentTitle(contentTitle);
			mBuilder.setTicker(ticker);
			mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
			mBuilder.setContentText(description);
			mBuilder.setProgress(0, 0, true);

			mBuilder.setOngoing(true);
			mBuilder.setAutoCancel(false);
			mBuilder.setOnlyAlertOnce(true);

//			Intent intent = new Intent(context, TransfertEventsReceiver.class);
//			intent.setAction(Constants.INTENT_ACTION_NOTIFICATION_CANCELED);
//			intent.putExtra("id", id);
//			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
			
//			mBuilder.setDeleteIntent(pendingIntent);
			
			Intent intent  = new Intent(context, MainActivity.class);
			intent.setAction(Constants.INTENT_ACTION_DISPLAY_TRANSFERTS);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
			
			mBuilder.setContentIntent(pendingIntent);

			mNotificationManager.notify(this.id, mBuilder.build());
		}
	}

	public void update(int percent)
	{
		if (isDiplayed)
		{
			mBuilder.setProgress(100, percent, false);

			mNotificationManager.notify(this.id, mBuilder.build());
		}
	}

	public void finish(Object result)
	{
		if (isDiplayed)
		{
			String ticker = type == TYPE_DOWNLOAD ? "Download finished" : "Upload finished";
			String contentTitle = ticker;

			mBuilder.setTicker(ticker);
			mBuilder.setContentTitle(contentTitle);
			mBuilder.setAutoCancel(true);
			mBuilder.setOngoing(false);
			mBuilder.setProgress(0, 0, false);
			mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_stat_transfert_finished));

			mNotificationManager.notify(this.id, mBuilder.build());
		}
	}

	public void cancel()
	{
		if (isDiplayed)
		{
			mNotificationManager.cancel(this.id);
		}
	}
}
