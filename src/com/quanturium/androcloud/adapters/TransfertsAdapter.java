package com.quanturium.androcloud.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanturium.androcloud.R;
import com.quanturium.androcloud.holders.TransfertItemViewHolder;
import com.quanturium.androcloud.models.TransfertTaskModel;
import com.quanturium.androcloud.models.TransfertTaskModel.Type;
import com.quanturium.androcloud.requests.TransfertStorage;
import com.quanturium.androcloud.requests.AbstractTransfertTask;

public class TransfertsAdapter extends BaseAdapter
{
	private LayoutInflater		inflater;
	List<TransfertTaskModel>	items		= new ArrayList<TransfertTaskModel>();
	public boolean				isDisabled	= false;

	public TransfertsAdapter(Context context)
	{
		this.inflater = LayoutInflater.from(context);
		update();
	}

	public void update()
	{
		if (!isDisabled)
		{
			items = TransfertStorage.getInstance().getTasksAsList();
			notifyDataSetChanged();
		}
	}

	public void remove(int position)
	{	
		AbstractTransfertTask t = TransfertStorage.getInstance().getTask((int) getItemId(position));
		
		if(t != null)
			t.cancel(true);
		
		update();
	}

	@Override
	public int getCount()
	{
		return items.size();
	}
	
	public int getCountUpload()
	{
		int n = 0;
		for(int i = 0 ; i < items.size() ; i++)
			if(items.get(i).getType() == Type.UPLOAD)
				n++;
		
		return n;
	}
	
	public int getCountDownload()
	{
		int n = 0;
		for(int i = 0 ; i < items.size() ; i++)
			if(items.get(i).getType() == Type.DOWNLOAD)
				n++;
		
		return n;		
	}

	@Override
	public TransfertTaskModel getItem(int arg0)
	{
		if (arg0 > getCount() - 1)
			return null;
		else
			return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return getItem(arg0).getId();
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
		TransfertItemViewHolder viewHolder;
		
		if(v == null)
		{
			viewHolder = new TransfertItemViewHolder();
			v = inflater.inflate(R.layout.row_transfert, parent, false);
			viewHolder.icon = (ImageView) v.findViewById(R.id.transfertItemIcon);
			viewHolder.title = (TextView) v.findViewById(R.id.transfertItemTitle);
			viewHolder.progress = (ProgressBar) v.findViewById(R.id.transfertItemProgress);
			v.setTag(viewHolder);
		}
		else
		{
			viewHolder = (TransfertItemViewHolder) v.getTag();
		}
		
		TransfertTaskModel item = getItem(position);
		
		if(item.getType() == Type.UPLOAD)
			viewHolder.icon.setImageResource(R.drawable.ic_menu_upload);
		else
			viewHolder.icon.setImageResource(R.drawable.ic_menu_download);
		
		viewHolder.title.setText(item.getName());
		viewHolder.progress.setProgress(item.getProgress());
		
		
		return v;
	}
}
