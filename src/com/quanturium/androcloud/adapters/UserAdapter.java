package com.quanturium.androcloud.adapters;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanturium.androcloud.R;
import com.quanturium.androcloud.holders.UserItemViewHolder;

public class UserAdapter extends BaseAdapter
{
	private LayoutInflater			inflater;
	private Map<Integer, String>	datas;

	public final static int			ITEM_SUBSCRIBED			= 0;
	public final static int			ITEM_SUBSCRIBED_EXPIRES	= 1;
	public final static int			ITEM_DOMAIN				= 2;
	public final static int			ITEM_SEPARATOR_1		= 3;
	public final static int			ITEM_LOG_OUT			= 4;

	public final static int			TYPE_NORMAL				= 0;
	public final static int			TYPE_SEPARATOR			= 1;

	public UserAdapter(Context context, Map<Integer, String> items)
	{
		this.inflater = LayoutInflater.from(context);
		this.datas = items;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return 5;
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
		switch (position)
		{
			case ITEM_DOMAIN:
			case ITEM_SUBSCRIBED:
			case ITEM_SUBSCRIBED_EXPIRES:
			case ITEM_LOG_OUT:
				return TYPE_NORMAL;
			case ITEM_SEPARATOR_1:
				return TYPE_SEPARATOR;
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
			case ITEM_DOMAIN:
			case ITEM_SUBSCRIBED:
			case ITEM_SUBSCRIBED_EXPIRES:
			case ITEM_SEPARATOR_1:
				return false;

			case ITEM_LOG_OUT:
				return true;
		}

		return false;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
		UserItemViewHolder viewHolder;

		if (v == null)
		{
			viewHolder = new UserItemViewHolder();

			if (getItemViewType(position) == TYPE_NORMAL)
			{
				v = inflater.inflate(R.layout.row_user_normal, parent, false);
				viewHolder.icon = (ImageView) v.findViewById(R.id.userItemIcon);
				viewHolder.title = (TextView) v.findViewById(R.id.userItemTitle);
				viewHolder.value = (TextView) v.findViewById(R.id.userItemValue);
			}
			else
			{
				v = inflater.inflate(R.layout.row_user_separator, parent, false);
			}

			v.setTag(viewHolder);
		}
		else
		{
			viewHolder = (UserItemViewHolder) v.getTag();
		}

		if (getItemViewType(position) == TYPE_NORMAL)
		{

			String title = null;
			int icon = 0;
			String value = null;

			switch (position)
			{

				case ITEM_SUBSCRIBED:

					title = "Suscribed";
					icon = R.drawable.ic_user_suscribed;
					value = datas.get(position);

					break;

				case ITEM_SUBSCRIBED_EXPIRES:

					title = "Suscription expires at";
					icon = R.drawable.ic_user_expiration;
					value = datas.get(position);

					break;

				case ITEM_DOMAIN:

					title = "Domain";
					icon = R.drawable.ic_user_domain;
					value = datas.get(position);

					break;

				case ITEM_LOG_OUT:

					title = "Log out";
					icon = R.drawable.ic_user_logout;
					value = null;

					break;
			}

			viewHolder.title.setText(title);
			viewHolder.icon.setImageResource(icon);
			viewHolder.value.setText(value);
		}

		return v;
	}

}
