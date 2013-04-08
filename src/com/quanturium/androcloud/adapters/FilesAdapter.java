package com.quanturium.androcloud.adapters;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.model.CloudAppItemImpl;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.databases.FilesDatabase;
import com.quanturium.androcloud.holders.FileItemViewHolder;
import com.quanturium.androcloud.tools.Tools;

public class FilesAdapter extends CursorAdapter
{
	public enum Status
	{
		NORMAL, LOADING, DISABLED;
	}

	private final Context			context;
	private final LayoutInflater	inflater;
	private Status					lastItemStatus	= Status.NORMAL;

	public static final int			TYPE_NORMAL		= 1;
	public static final int			TYPE_LOAD_MORE	= 0;

	private final static String		TAG				= "FilesAdapter";

	public FilesAdapter(Context context, Cursor c, int flags)
	{
		super(context, c, flags);
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount()
	{
		int count = super.getCount();
		return count > 0 ? count + 1 : 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		if (position == super.getCount()) // if position == displayItems we are outofBound of 1. Some it is the n
			return TYPE_LOAD_MORE;
		else
			return TYPE_NORMAL;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		if (position == super.getCount() && lastItemStatus == Status.DISABLED)
			return false;
		else
			return true;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	public void setLastItemStatus(FilesAdapter.Status status)
	{
		this.lastItemStatus = status;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.i(TAG, "get view with position " + position);
		
		if (getItemViewType(position) == TYPE_LOAD_MORE)
		{
			View v;
			if (convertView == null)
			{
				v = newView(context, null, parent);
			}
			else
			{
				v = convertView;
			}

			bindView(v, context, null);
			return v;
		}
		else
		{
			if (!getCursor().moveToPosition(position))
				throw new IllegalStateException("couldn't move cursor to position " + position);

			View v;
			if (convertView == null)
			{
				v = newView(context, getCursor(), parent);
			}
			else
			{
				v = convertView;
			}
			bindView(v, context, getCursor());
			return v;
		}
	}

	@Override
	public void bindView(View v, Context context, Cursor cursor)
	{				
		FileItemViewHolder itemViewHolder = (FileItemViewHolder) v.getTag();

		if (cursor == null) // type LOAD_MORE
		{
			switch (lastItemStatus)
			{
				case NORMAL:
				case DISABLED:

					itemViewHolder.progressBar.setVisibility(View.GONE);
					itemViewHolder.title.setVisibility(View.VISIBLE);

					break;

				case LOADING:

					itemViewHolder.title.setVisibility(View.GONE);
					itemViewHolder.progressBar.setVisibility(View.VISIBLE);

					break;
			}
		}
		else
		{
			try
			{
//				int date = cursor.getInt(cursor.getColumnIndex(FilesDatabase.COL_DATE));
				String json = cursor.getString(cursor.getColumnIndex(FilesDatabase.COL_DATA));
				CloudAppItem item = new CloudAppItemImpl(new JSONObject(json));

				itemViewHolder.title.setText(item.getName());
				itemViewHolder.count.setText(item.getViewCounter() + "");
				itemViewHolder.date.setText(Tools.getElapsedTimeFrom(item.getUpdatedAt()));
				

				switch (item.getItemType())
				{

					case AUDIO:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_audio);
						break;

					case BOOKMARK:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_bookmark);
						break;

					case IMAGE:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_image);
						break;

					case VIDEO:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_video);
						break;

					case TEXT:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_text);
						break;

					case ARCHIVE:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_archive);
						break;

					case UNKNOWN:
					default:
						itemViewHolder.icon.setImageResource(R.drawable.ic_filetype_unknown);
						break;
				}

			} catch (CloudAppException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		FileItemViewHolder itemViewHolder;
		View v;

		if (cursor == null) // type LOAD_MORE
		{
			itemViewHolder = new FileItemViewHolder();
			v = inflater.inflate(R.layout.row_file_more, null);
			itemViewHolder.title = (TextView) v.findViewById(R.id.fileMoreText);
			itemViewHolder.progressBar = (ProgressBar) v.findViewById(R.id.fileMoreProgress);
			v.setTag(itemViewHolder);
		}
		else
		{
			itemViewHolder = new FileItemViewHolder();
			v = inflater.inflate(R.layout.row_file_normal, null);
			itemViewHolder.title = (TextView) v.findViewById(R.id.fileItemTitle);
			itemViewHolder.date = (TextView) v.findViewById(R.id.fileItemDate);
			itemViewHolder.count = (TextView) v.findViewById(R.id.fileItemCount);
			itemViewHolder.icon = (ImageView) v.findViewById(R.id.fileItemIcon);
			itemViewHolder.layout = (LinearLayout) v.findViewById(R.id.fileItem);
			v.setTag(itemViewHolder);
		}

		return v;
	}
}
