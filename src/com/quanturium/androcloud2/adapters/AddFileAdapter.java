package com.quanturium.androcloud2.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.holders.AddFileItemViewHolder;

public class AddFileAdapter extends BaseAdapter
{
	public final static int ITEM_IMAGE = 0;
	public final static int ITEM_BOOKMARK = 1;
	public final static int ITEM_TEXT = 2;
	public final static int ITEM_AUDIO = 3;
	public final static int ITEM_VIDEO = 4;
	public final static int ITEM_UNKNOWN = 5;
	
	public final static int	STYLE_DROPDOWN	= 1;
	public final static int	STYLE_INLINE	= 2;
	
	private int				style			= 1;
	
	private LayoutInflater	inflater		= null;
	private Context			context			= null;

	public AddFileAdapter(Context context, int style)
	{
		this.inflater = LayoutInflater.from(context);
		this.style = style;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return 6;
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
		AddFileItemViewHolder viewHolder;

		if (v == null)
		{
			viewHolder = new AddFileItemViewHolder();

			if (style == STYLE_DROPDOWN)
			{
				v = inflater.inflate(R.layout.row_addfile_dropdown, parent, false);
				viewHolder.icon = (ImageView) v.findViewById(R.id.addfileItemIcon2);

				v.setX(200);
				ObjectAnimator animator = ObjectAnimator.ofFloat(v, "x", 0);
				animator.setDuration(300);
				animator.setStartDelay(80 * position);
				animator.start();
			}
			else if (style == STYLE_INLINE)
			{
				v = inflater.inflate(R.layout.row_addfile_inline, parent, false);
				viewHolder.icon = (ImageView) v.findViewById(R.id.addfileItemIcon);

				ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1);
				animation.setDuration(200);
				animation.setStartOffset(80 * position);
				v.setAnimation(animation);
			}

			v.setTag(viewHolder);
		}
		else
		{
			viewHolder = (AddFileItemViewHolder) v.getTag();
		}

		String title = null;
		int icon = 0;

		switch (position)
		{
			case ITEM_IMAGE:

				icon = R.drawable.ic_filetype_image_gray;
				title = "Images";

				break;

			case ITEM_BOOKMARK:

				icon = R.drawable.ic_filetype_bookmark_gray;
				title = "Bookmarks";

				break;

			case ITEM_TEXT:

				icon = R.drawable.ic_filetype_text_gray;
				title = "Text";

				break;

			case ITEM_AUDIO:

				icon = R.drawable.ic_filetype_audio_gray;
				title = "Audio";

				break;

			case ITEM_VIDEO:

				icon = R.drawable.ic_filetype_video_gray;
				title = "Video";

				break;

			case ITEM_UNKNOWN:

				icon = R.drawable.ic_filetype_unknown_gray;
				title = "Other file";

				break;
		}

		viewHolder.icon.setImageResource(icon);

		return v;
	}

}
