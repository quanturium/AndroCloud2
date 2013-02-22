package com.quanturium.androcloud2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.holders.MenuItemViewHolder;
import com.quanturium.androcloud2.tools.Prefs;

public class MenuAdapter extends BaseAdapter
{
	private Context			context;
	private LayoutInflater	inflater			= null;
	private int[]			tasksRunning		= { 0, 0 };

	public final static int	ITEM_USER			= 0;
	public final static int	ITEM_SEPARATOR_1	= 1;
	public final static int	ITEM_HOME			= 2;
	public final static int	ITEM_ADD_FILE		= 3;
	public final static int	ITEM_ADD_FILES_BIS	= 4;
	public final static int	ITEM_SHOW_ALL		= 5;
	public final static int	ITEM_SHOW_TRASH		= 6;
	public final static int	ITEM_SEPARATOR_2	= 7;
	public final static int	ITEM_TRANSFERTS		= 8;
	public final static int	ITEM_SEPARATOR_3	= 9;
	public final static int	ITEM_PREFERENCES	= 10;
	public final static int	ITEM_ABOUT			= 11;

	public final static int	TYPE_NORMAL			= 0;
	public final static int	TYPE_TRANSFERT		= 1;
	public final static int	TYPE_SEPARATOR		= 2;
	public final static int	TYPE_ADDFILES		= 3;

	public MenuAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setBackgroundTasksRunning(int[] n)
	{
		this.tasksRunning = n;
	}

	@Override
	public int getCount()
	{
		return 12;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemViewType(int position)
	{
		switch (position)
		{
			case ITEM_USER:
			case ITEM_HOME:
			case ITEM_SHOW_ALL:
			case ITEM_SHOW_TRASH:
			case ITEM_ABOUT:
			case ITEM_PREFERENCES:
			case ITEM_ADD_FILE:

				return TYPE_NORMAL;

			case ITEM_TRANSFERTS:

				return TYPE_TRANSFERT;

			case ITEM_SEPARATOR_1:
			case ITEM_SEPARATOR_2:
			case ITEM_SEPARATOR_3:

				return TYPE_SEPARATOR;

			case ITEM_ADD_FILES_BIS:

				return TYPE_ADDFILES;
		}

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
		switch (position)
		{
			case ITEM_SEPARATOR_1:
			case ITEM_SEPARATOR_2:
			case ITEM_SEPARATOR_3:
			case ITEM_ADD_FILES_BIS:

				return false;

			default:

				return true;
		}
	}

	@Override
	public int getViewTypeCount()
	{
		return 4;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		MenuItemViewHolder viewHolder;

		if (convertView == null)
		{
			viewHolder = new MenuItemViewHolder();

			switch (getItemViewType(position))
			{
				case TYPE_NORMAL:

					convertView = this.inflater.inflate(R.layout.row_menu_normal, parent, false);
					viewHolder.name = (TextView) convertView.findViewById(R.id.row_menu_text);
					viewHolder.icon = (ImageView) convertView.findViewById(R.id.row_menu_icon);

					break;

				case TYPE_TRANSFERT:

					convertView = this.inflater.inflate(R.layout.row_menu_transfert, parent, false);
					viewHolder.name = (TextView) convertView.findViewById(R.id.row_menu_text);
					viewHolder.icon = (ImageView) convertView.findViewById(R.id.row_menu_icon);
					viewHolder.transfertLayout = (TableLayout) convertView.findViewById(R.id.row_menu_transfets);
					viewHolder.downloadCount = (TextView) convertView.findViewById(R.id.row_menu_transfert_download);
					viewHolder.uploadsCount = (TextView) convertView.findViewById(R.id.row_menu_transfert_upload);

					break;

				case TYPE_SEPARATOR:

					convertView = this.inflater.inflate(R.layout.row_menu_separator, parent, false);
					viewHolder.name = (TextView) convertView.findViewById(R.id.row_menu_text);
					viewHolder.icon = (ImageView) convertView.findViewById(R.id.row_menu_icon);

					break;

				case TYPE_ADDFILES :
					
					convertView = this.inflater.inflate(R.layout.row_menu_addfile_bis, parent, false);

					break;
			}

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (MenuItemViewHolder) convertView.getTag();
		}

		switch (position)
		{
			case ITEM_USER:

				viewHolder.name.setText(Prefs.getPreferences(context).getString(Prefs.EMAIL, "No account"));
				viewHolder.icon.setImageResource(R.drawable.ic_menu_user);

				break;

			case ITEM_HOME:

				viewHolder.name.setText("Home");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_home);

				break;

			case ITEM_ADD_FILE:

				viewHolder.name.setText("Add files");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_addfile);

				break;

			case ITEM_SHOW_ALL:

				viewHolder.name.setText("Show all");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_all);

				break;

			case ITEM_SHOW_TRASH:

				viewHolder.name.setText("Trash");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_trash);

				break;

			case ITEM_TRANSFERTS:

				viewHolder.name.setText("Transferts");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_transfert);

				if (this.tasksRunning[0] > 0 || this.tasksRunning[1] > 0)
					viewHolder.transfertLayout.setVisibility(View.VISIBLE);
				else
					viewHolder.transfertLayout.setVisibility(View.GONE);

				viewHolder.uploadsCount.setText("" + this.tasksRunning[0]);
				viewHolder.downloadCount.setText("" + this.tasksRunning[1]);

				break;

			case ITEM_PREFERENCES:

				viewHolder.name.setText("Preferences");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_settings);

				break;

			case ITEM_ABOUT:

				viewHolder.name.setText("About");
				viewHolder.icon.setImageResource(R.drawable.ic_menu_about);

				break;

			case ITEM_SEPARATOR_1:
			case ITEM_SEPARATOR_2:
			case ITEM_SEPARATOR_3:

				break;

		}

		return convertView;
	}
}
