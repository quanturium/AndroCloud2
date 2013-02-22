package com.quanturium.androcloud2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.holders.HomeItemViewHolder;

public class HomeAdapter extends BaseAdapter
{
	private LayoutInflater	inflater	= null;

	public HomeAdapter(Context context)
	{
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (position == 1 || position == 9)
			return 1;
		else
			return 0;
	}
	
	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		if(getItemViewType(position) == 0)
			return true;
		else
			return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (getItemViewType(position) == 0)
		{
			HomeItemViewHolder viewHolder;

			if (convertView == null)
			{
				viewHolder = new HomeItemViewHolder();
				convertView = this.inflater.inflate(R.layout.row_home_normal, parent, false);
				viewHolder.title = (TextView) convertView.findViewById(R.id.homeItemTitle);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.homeItemIcon);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (HomeItemViewHolder) convertView.getTag();
			}

			String title = null;
			int icon = 0;
			switch (position)
			{
				case 0:

					title = "All files";
					icon = R.drawable.ic_filetype_all;

					break;
					
				case 2 :  
					
					title = "Images";
					icon = R.drawable.ic_filetype_image;
					
					break;
					
				case 3 :  
					
					title = "Bookmarks";
					icon = R.drawable.ic_filetype_bookmark;
					
					break;
					
				case 4 :  
					
					title = "Text";
					icon = R.drawable.ic_filetype_text;
					
					break;
					
				case 5 :  
					
					title = "Archives";
					icon = R.drawable.ic_filetype_archive;
					
					break;
					
				case 6 :  
					
					title = "Audio";
					icon = R.drawable.ic_filetype_audio;
					
					break;
					
				case 7 :  
					
					title = "Video";
					icon = R.drawable.ic_filetype_video;
					
					break;
					
				case 8 :  
					
					title = "Other";
					icon = R.drawable.ic_filetype_unknown;
					
					break;
					
				case 10 :  
					
					title = "Trash";
					icon = R.drawable.ic_filetype_trash;
					
					break;
			}

			viewHolder.title.setText(title);
			viewHolder.icon.setImageResource(icon);
		}
		else
		{
			if (convertView == null)
			{				
				convertView = this.inflater.inflate(R.layout.row_home_separator, parent, false);
			}
		}
		
		return convertView;
	}

}
