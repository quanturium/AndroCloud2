package com.quanturium.androcloud2.listeners;

import android.graphics.Bitmap;

public interface ImageCacheTaskListener
{
	public void onImageCacheTaskFinish(Bitmap bitmap);
	public void onImageCacheTaskCancel();
}
