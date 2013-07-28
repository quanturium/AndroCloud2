package com.quanturium.androcloud.services;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.CloudAppImpl;
import com.quanturium.androcloud.databases.FilesDatabase;
import com.quanturium.androcloud.requests.FilesRequestAnswer;
import com.quanturium.androcloud.requests.FilesRequestQuery;

public class FilesService extends IntentService {

	public FilesService() {
		super("FilesService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		FilesRequestQuery query = (FilesRequestQuery) intent.getExtras().get("query");
		ResultReceiver receiver = (ResultReceiver) intent.getExtras().get("receiver");
		CloudApp api = new CloudAppImpl(query.username , query.password);
		List<CloudAppItem> items = null;
		FilesRequestAnswer answer;
		
		try
		{		
			items = api.getItems(query.page, query.nbFiles, null, query.trashed, null);
			FilesDatabase.getInstance(getApplicationContext()).addFiles(items.toArray(new CloudAppItem[items.size()]));
			answer = new FilesRequestAnswer(FilesRequestAnswer.RESULT_OK, null, query.page);

		} catch (CloudAppException e)
		{
			answer = new FilesRequestAnswer(FilesRequestAnswer.RESULT_ERROR, e, 0);
			e.printStackTrace();
		}
		
		Bundle resultData = new Bundle();
		resultData.putSerializable("result", answer);
		receiver.send(1, resultData);		
	}

}
