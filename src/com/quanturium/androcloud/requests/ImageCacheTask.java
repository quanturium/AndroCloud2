package com.quanturium.androcloud.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.cloudapp.api.CloudAppException;
import com.quanturium.androcloud.Constants;
import com.quanturium.androcloud.listeners.ImageCacheTaskListener;
import com.quanturium.androcloud.tools.Cache;
import com.quanturium.androcloud.tools.Tools;

public class ImageCacheTask extends AsyncTask<String, Integer, Bitmap>
{
	private Context							context	= null;
	private volatile ImageCacheTaskListener	mCallback;

	public ImageCacheTask(Context context, ImageCacheTaskListener listener)
	{
		this.context = context.getApplicationContext();
		this.mCallback = listener;
	}

	public void setCallback(ImageCacheTaskListener listener)
	{
		this.mCallback = listener;
	}

	@Override
	protected Bitmap doInBackground(String... params)
	{
		String url = params[0];
		URL urlImage;
		String fileName = "bitmap." + Tools.md5(url) + ".png";
		Bitmap imageBitmap = null;

		if ((imageBitmap = Cache.getCachedBitmap(context, fileName, Cache.CACHE_TIME_BITMAP)) != null)
		{
			return imageBitmap;
		}
		else
		{

			try
			{
				urlImage = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
				connection.setInstanceFollowRedirects(true);
				InputStream inputStream = connection.getInputStream();
				imageBitmap = BitmapFactory.decodeStream(inputStream);
				int new_width = 0, new_height = 0;
				float ratio = (float) imageBitmap.getHeight() / imageBitmap.getWidth();

				if (imageBitmap.getWidth() > Constants.MAX_IMAGE_SIZE_PX && imageBitmap.getHeight() > Constants.MAX_IMAGE_SIZE_PX)
				{

					if (imageBitmap.getWidth() > imageBitmap.getHeight())
					{
						new_width = Constants.MAX_IMAGE_SIZE_PX;
						new_height = (int) (Constants.MAX_IMAGE_SIZE_PX * ratio);
					}
					else
					{
						new_width = (int) (Constants.MAX_IMAGE_SIZE_PX / ratio);
						new_height = Constants.MAX_IMAGE_SIZE_PX;
					}

					imageBitmap = Bitmap.createScaledBitmap(imageBitmap, new_width, new_height, false);
				}
				else if (imageBitmap.getWidth() > Constants.MAX_IMAGE_SIZE_PX)
				{
					new_width = Constants.MAX_IMAGE_SIZE_PX;
					new_height = (int) (Constants.MAX_IMAGE_SIZE_PX * ratio);

					imageBitmap = Bitmap.createScaledBitmap(imageBitmap, Constants.MAX_IMAGE_SIZE_PX, new_height, false);
				}
				else if (imageBitmap.getHeight() > Constants.MAX_IMAGE_SIZE_PX)
				{
					new_width = (int) (Constants.MAX_IMAGE_SIZE_PX / ratio);
					new_height = Constants.MAX_IMAGE_SIZE_PX;

					imageBitmap = Bitmap.createScaledBitmap(imageBitmap, new_width, Constants.MAX_IMAGE_SIZE_PX, false);
				}

				Cache.setCachedBitmap(context, fileName, imageBitmap);
				
				return imageBitmap;

			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		if (this.mCallback != null)
			this.mCallback.onImageCacheTaskFinish(bitmap);
	}

	@Override
	protected void onCancelled()
	{
		if (this.mCallback != null)
			this.mCallback.onImageCacheTaskCancel();
	}
}
