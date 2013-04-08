package com.quanturium.androcloud.requests;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.model.CloudAppItemImpl;
import com.quanturium.androcloud.Constants;

public class TransfertEventsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();

		String data = intent.getStringExtra(Constants.NOTIFICATION_INTENT_DATA_KEY);
		int onClickAction = intent.getIntExtra(Constants.NOTIFICATION_INTENT_ACTION_KEY, -1);

		if (action.equals(Constants.INTENT_ACTION_NOTIFICATION_DOWNLOAD_ACTION))
		{
			switch (onClickAction)
			{
				case 0: // open file

					File file = new File(data);
					String uri = Uri.fromFile(file).toString();
					String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
					MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
					String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);

					Intent i0 = new Intent();
					i0.setAction(android.content.Intent.ACTION_VIEW);
					i0.setDataAndType(Uri.parse(uri), mimeType);
					i0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(i0);

					break;
			}
		}
		else if (action.equals(Constants.INTENT_ACTION_NOTIFICATION_UPLOAD_ACTION))
		{
			CloudAppItem item;
			try
			{
				item = new CloudAppItemImpl(new JSONObject(data));
				ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				
				switch (onClickAction)
				{
					case 0: // copy link to clipboard

						
						ClipData clipUrl = ClipData.newPlainText("url",item.getUrl());
						clipboard.setPrimaryClip(clipUrl);
						
						Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show();

						break;

					case 1: // copy direct link to clipboard

						ClipData clipRemoteUrl = ClipData.newPlainText("url",item.getRemoteUrl());
						clipboard.setPrimaryClip(clipRemoteUrl);
						
						Toast.makeText(context, "Direct link copied to clipboard", Toast.LENGTH_SHORT).show();

						break;

					case 2: // open link in web browser

						Intent i2 = new Intent("android.intent.action.VIEW", Uri.parse(item.getUrl()));
						i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i2);

						break;

					case 3: // open direct link in web browser

						Intent i3 = new Intent("android.intent.action.VIEW", Uri.parse(item.getRemoteUrl()));
						i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i3);

						break;
				}

			} catch (JSONException e1)
			{
				e1.printStackTrace();
			} catch (CloudAppException e)
			{
				e.printStackTrace();
			}
		}
	}
}
