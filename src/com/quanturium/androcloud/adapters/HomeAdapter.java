package com.quanturium.androcloud.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanturium.androcloud.R;
import com.quanturium.androcloud.holders.HomeItemViewHolder;

public class HomeAdapter extends BaseAdapter
{
	private LayoutInflater	inflater			= null;

	public final static int	ITEM_ALL			= 0;
	public final static int	ITEM_SEPARATOR_1	= 1;
	public final static int	ITEM_IMAGES			= 2;
	public final static int	ITEM_BOOKMARKS		= 3;
	public final static int	ITEM_TEXT			= 4;
	public final static int	ITEM_ARCHIVES		= 5;
	public final static int	ITEM_AUDIO			= 6;
	public final static int	ITEM_VIDEO			= 7;
	public final static int	ITEM_OTHER			= 8;
	public final static int	ITEM_SEPARATOR_2	= 9;
	public final static int	ITEM_TRASH			= 10;

	public final static int	TYPE_NORMAL			= 0;
	public final static int	TYPE_SEPARATOR		= 1;

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
		return null;
	}

	@Override
	public long getItemId(int position)
	{
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
		switch(position)
		{
			case ITEM_SEPARATOR_1:
			case ITEM_SEPARATOR_2:
				return TYPE_SEPARATOR;
			default : 
				return TYPE_NORMAL;
		}
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		if (getItemViewType(position) == TYPE_NORMAL)
			return true;
		else
			return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (getItemViewType(position) == TYPE_NORMAL)
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
				case ITEM_ALL:

					title = "All files";
					icon = R.drawable.ic_filetype_all;

					break;

				case ITEM_IMAGES:

					title = "Images";
					icon = R.drawable.ic_filetype_image;

					break;

				case ITEM_BOOKMARKS:

					title = "Bookmarks";
					icon = R.drawable.ic_filetype_bookmark;

					break;

				case ITEM_TEXT:

					title = "Text";
					icon = R.drawable.ic_filetype_text;

					break;

				case ITEM_ARCHIVES:

					title = "Archives";
					icon = R.drawable.ic_filetype_archive;

					break;

				case ITEM_AUDIO:

					title = "Audio";
					icon = R.drawable.ic_filetype_audio;

					break;

				case ITEM_VIDEO:

					title = "Video";
					icon = R.drawable.ic_filetype_video;

					break;

				case ITEM_OTHER:

					title = "Other";
					icon = R.drawable.ic_filetype_unknown;

					break;

				case ITEM_TRASH:

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
