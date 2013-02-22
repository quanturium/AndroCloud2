package com.quanturium.androcloud2.fragments;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.api.model.CloudAppItem.Type;
import com.cloudapp.impl.CloudAppImpl;
import com.cloudapp.impl.model.CloudAppItemImpl;
import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.holders.FileDetailsActionViewHolder;
import com.quanturium.androcloud2.listeners.FragmentListener;
import com.quanturium.androcloud2.tools.Cache;
import com.quanturium.androcloud2.tools.Prefs;
import com.quanturium.androcloud2.tools.Tools;

public class FileDetailsFragment extends Fragment implements OnClickListener
{
	private FragmentListener			mCallbacks			= null;
	private final static String			TAG					= "FileDetailsFragment";

	private FileDetailsActionViewHolder	viewHolder;

	private Handler						handler				= new Handler();
	private Activity					activity;
	private CloudAppItem				item;
	public Menu							menu;
	public boolean						currentlyLoading	= false;
	public int							currentlyAction		= -1;
	private ImageView					imageView;
	private ProgressBar					imageLoader;
	private Bitmap						imageBitmap			= null;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (!(activity instanceof FragmentListener))
			throw new IllegalStateException("Activity must implement fragment's callbacks.");

		this.mCallbacks = (FragmentListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_filedetails, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setActionBarNavigationModeList(false);

		Bundle bundle = this.getArguments();

		if (bundle != null)
		{
			if (getArguments().getString("json", null) != null)
			{
				if (getArguments().getString("json") != null && getView() != null)
				{
					setUI();
					loadItem(getArguments().getString("json"));
					fillUI();
				}
			}
		}

		viewHolder = new FileDetailsActionViewHolder();
		viewHolder.view = (ImageView) getActivity().findViewById(R.id.filedetailsActionView);
		viewHolder.view.setOnClickListener(this);
		viewHolder.save = (ImageView) getActivity().findViewById(R.id.filedetailsActionSave);
		viewHolder.save.setOnClickListener(this);
		viewHolder.share = (ImageView) getActivity().findViewById(R.id.filedetailsActionShare);
		viewHolder.share.setOnClickListener(this);
		viewHolder.edit = (ImageView) getActivity().findViewById(R.id.filedetailsActionEdit);
		viewHolder.edit.setOnClickListener(this);
		viewHolder.delete = (ImageView) getActivity().findViewById(R.id.filedetailsActionDelete);
		viewHolder.delete.setOnClickListener(this);

		setHandler();
		// setHasOptionsMenu(true);

	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// switch (item.getItemId())
	// {
	//
	// case R.id.menuItemView:
	//
	// openFile();
	//
	// break;
	//
	// case R.id.menuItemSave:
	//
	// File saveFile = null;
	// try
	// {
	// saveFile = new File(getSavingFolder(), this.item.getName());
	//
	// if (saveFile.exists())
	// {
	// AlertDialog.Builder alert = new AlertDialog.Builder(this.activity);
	//
	// alert.setTitle("Open file");
	// alert.setMessage("A file with the same name already exists. Do you want to overwrite it ?");
	// alert.setNegativeButton("Cancel", null);
	// alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
	// {
	// public void onClick(DialogInterface dialog, int whichButton)
	// {
	// saveFile(false);
	// }
	// });
	//
	// alert.show();
	// }
	// else
	// {
	// saveFile(false);
	// }
	// } catch (CloudAppException e1)
	// {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// break;
	//
	// case R.id.menuItemShare:
	//
	// shareFile();
	//
	// break;
	//
	// case R.id.menuItemRename:
	//
	// AlertDialog.Builder alert = new AlertDialog.Builder(this.activity);
	//
	// alert.setTitle("New name");
	//
	// // Set an EditText view to get user input
	// final EditText input = new EditText(this.activity);
	// input.setText(getNameWithoutExtension());
	// input.setSelectAllOnFocus(true);
	// alert.setView(input);
	// alert.setNegativeButton("Cancel", null);
	// alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
	// {
	// public void onClick(DialogInterface dialog, int whichButton)
	// {
	// String value = input.getText().toString().trim();
	//
	// if (!value.equals("") && !value.equals(getNameWithoutExtension()))
	// {
	// String ext = getExtention();
	//
	// if (ext != null)
	// renameFile(value + '.' + getExtention());
	// else
	// renameFile(value);
	// }
	//
	// }
	// });
	//
	// alert.show();
	//
	// break;
	//
	// case R.id.menuItemDelete:
	//
	// deleteFile();
	//
	// break;
	//
	// default:
	//
	// return false;
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	// {
	// inflater.inflate(R.menu.fragment_filedetails, menu);
	//
	// if (currentlyLoading)
	// {
	// int order = menu.findItem(currentlyAction).getOrder();
	// Log.i(TAG, "Order " + order + "");
	// menu.add(Menu.NONE, Menu.NONE, order, "Loading").setActionView(R.layout.progress).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	// menu.removeItem(currentlyAction);
	// }
	//
	// super.onCreateOptionsMenu(menu, inflater);
	// }

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.filedetailsActionView:

				break;

			case R.id.filedetailsActionSave:

				break;

			case R.id.filedetailsActionShare:

				break;

			case R.id.filedetailsActionEdit:

				break;

			case R.id.filedetailsActionDelete:

				break;
		}
	}

	public void onStartAction(int res)
	{
		currentlyLoading = true;
		currentlyAction = res;
		getActivity().invalidateOptionsMenu();
	}

	public void onStopAction()
	{
		currentlyLoading = false;
		currentlyAction = -1;
		getActivity().invalidateOptionsMenu();
	}

	private void displayView(View view)
	{
		this.imageView.setVisibility(View.GONE);
		this.imageLoader.setVisibility(View.GONE);

		view.setVisibility(View.VISIBLE);
	}

	private void setUI()
	{
		this.imageView = (ImageView) activity.findViewById(R.id.imageView);
		this.imageLoader = (ProgressBar) activity.findViewById(R.id.imageLoader);
	}

	private void fillUI()
	{
		ActionBar actionBar = activity.getActionBar();

		try
		{
			actionBar.setSubtitle(item.getName());
		} catch (CloudAppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			Integer drawableRessource = null;

			switch (item.getItemType())
			{

				case AUDIO:
					drawableRessource = R.drawable.ic_filetype_audio;
					break;

				case BOOKMARK:
					drawableRessource = R.drawable.ic_filetype_bookmark;
					break;

				case IMAGE:

					break;

				case VIDEO:
					drawableRessource = R.drawable.ic_filetype_video;
					break;

				// case TEXT:
				// drawableRessource = R.drawable.file_text;
				// break;
				//
				// case ARCHIVE:
				// drawableRessource = R.drawable.file_archive;
				// break;

				case UNKNOWN:
				default:
					drawableRessource = R.drawable.ic_filetype_unknown;
					break;
			}

			if (drawableRessource == null)
			{
				String fileName = "bitmap." + Tools.md5(item.getHref()) + ".png";
				if ((imageBitmap = Cache.getCachedBitmap(activity, fileName, Cache.CACHE_TIME_BITMAP)) != null)
				{
					displayView(imageView);
					imageView.setImageBitmap(imageBitmap);
				}
				else
				{
					displayView(imageLoader);
					loadPicture();
				}
			}
			else
			{
				Display display = activity.getWindowManager().getDefaultDisplay();

				imageView.setAdjustViewBounds(true);
				imageView.setMaxHeight((int) (display.getHeight() * 0.7));
				imageView.setMaxWidth((int) (display.getWidth() * 0.7));
				imageView.setImageDrawable(getResources().getDrawable(drawableRessource));

				displayView(imageView);
			}
		} catch (CloudAppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getMIME()
	{
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		return mime.getMimeTypeFromExtension(getExtention());
	}

	public String getSavingFolder()
	{
		String folder = null;

		try
		{
			Type type = item.getItemType();

			switch (type)
			{
				case IMAGE:

					folder = Prefs.getPreferences(activity).getString(Prefs.DIRECTORY_PICTURES, null);

					break;

				case AUDIO:

					folder = Prefs.getPreferences(activity).getString(Prefs.DIRECTORY_MUSICS, null);

					break;

				case VIDEO:

					folder = Prefs.getPreferences(activity).getString(Prefs.DIRECTORY_MOVIES, null);

					break;

				// case TEXT:
				//
				// folder = Prefs.getPreferences(activity).getString(Prefs.DIRECTORY_TEXTS, null);
				//
				// break;
				//
				// case ARCHIVE:
				case BOOKMARK:
				case UNKNOWN:
				default:

					folder = Prefs.getPreferences(activity).getString(Prefs.DIRECTORY_UNKOWN, null);

					break;
			}
		} catch (CloudAppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return folder;
	}

	private String getExtention()
	{
		try
		{
			int index = item.getRemoteUrl().lastIndexOf(".") + 1;

			if (index == 0) // no file extention
				throw new Exception();

			return item.getRemoteUrl().substring(index);
		} catch (CloudAppException e)
		{
			return null;
		} catch (Exception e)
		{
			return null;
		}
	}

	private String getNameWithoutExtension()
	{
		try
		{
			int index = item.getName().lastIndexOf(".") + 1;

			if (index == 0) // no file extention
			{
				return item.getName();
			}
			else
			{
				return item.getName().substring(0, index - 1);
			}
		} catch (CloudAppException e)
		{
			return null;
		}
	}

	private void loadItem(String jsonString)
	{
		if (jsonString != null)
		{
			try
			{
				this.item = new CloudAppItemImpl(new JSONObject(jsonString));
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void loadPicture()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				URL urlImage;

				try
				{
					urlImage = new URL(item.getRemoteUrl());
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

					Cache.setCachedBitmap(activity, "bitmap." + Tools.md5(item.getHref()) + ".png", imageBitmap);

				} catch (CloudAppException e)
				{
					e.printStackTrace();
				} catch (MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{
					e.printStackTrace();
				} finally
				{
					handler.sendEmptyMessage(Constants.FILE_ACTION_PICTURE_LOADED);
				}

			}
		}).start();
	}

	private void deleteFile()
	{
		if (!currentlyLoading)
		{
			// onStartAction(R.id.menuItemDelete);

			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					Message message = new Message();
					message.what = Constants.FILE_ACTION_DELETE;

					CloudApp api = new CloudAppImpl(Prefs.getPreferences(activity).getString(Prefs.EMAIL, ""), Prefs.getPreferences(activity).getString(Prefs.PASSWORD, ""));

					try
					{
						api.delete(item);
						// TODO Delete the local file if it exists
						message.arg1 = 1; // success
						message.obj = item.getHref();
					} catch (CloudAppException e)
					{
						message.arg1 = 0; // fail
						e.printStackTrace();
					} finally
					{
						handler.sendMessage(message);
					}
				}
			}).start();

		}
	}

	private void renameFile(final String newName)
	{
		if (!currentlyLoading)
		{
			// onStartAction(R.id.menuItemRename);

			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					Message message = new Message();
					message.what = Constants.FILE_ACTION_RENAME;

					CloudApp api = new CloudAppImpl(Prefs.getPreferences(activity).getString(Prefs.EMAIL, ""), Prefs.getPreferences(activity).getString(Prefs.PASSWORD, ""));

					try
					{
						api.rename(item, newName);
						// TODO Rename the local file if it exists
						message.arg1 = 1; // success
						String[] data = { item.getHref(), newName };
						message.obj = data;
					} catch (CloudAppException e)
					{
						message.arg1 = 0; // fail
						e.printStackTrace();
					} finally
					{
						handler.sendMessage(message);
					}
				}
			}).start();

		}
	}

	private void saveFile(boolean openWhenSaved)
	{
		if (!currentlyLoading)
		{
			// onStartAction(R.id.menuItemSave);

			// try
			// {
			// // TransfertTask task = new DownloadTask(activity, item, item.getName(), this, openWhenSaved);
			// // task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "test");
			// } catch (CloudAppException e)
			// {
			// e.printStackTrace();
			// }
		}
	}

	private void shareFile()
	{
		// TODO Auto-generated method stub

		Message message = new Message();
		message.what = Constants.FILE_ACTION_SHARE;

		try
		{
			ClipData clip = ClipData.newPlainText(item.getName() + " URL", item.getUrl());

			ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setPrimaryClip(clip);

			message.arg1 = 1; // success
			message.obj = item.getHref();
		} catch (CloudAppException e)
		{
			message.arg1 = 0; // fail
			e.printStackTrace();
		} finally
		{
			handler.sendMessage(message);
		}
	}

	private void openFile()
	{
		if (!currentlyLoading)
		{
			try
			{
				File saveFile = new File(getSavingFolder(), item.getName());

				if (saveFile.exists())
				{
					Intent i = new Intent();
					i.setAction(android.content.Intent.ACTION_VIEW);
					i.setDataAndType(Uri.fromFile(saveFile), getMIME());
					startActivity(i);
				}
				else
				// We propose to save it before opening it
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(this.activity);

					alert.setTitle("Open file");
					alert.setMessage("The file has not been found on your device. You have to save it before being able to open it. Would you like to download and save the file ?");
					alert.setNegativeButton("Cancel", null);
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							saveFile(true);
						}
					});

					alert.show();
				}
			} catch (CloudAppException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setHandler()
	{
		this.handler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case Constants.FILE_ACTION_DELETE:

						onStopAction();

						try
						{
							if (msg.arg1 == 1)
							{
								Toast.makeText(activity, "Deleting " + item.getName() + " : success", Toast.LENGTH_SHORT).show();
								// mCallbacks.onFileDeleted(msg);
							}
							else
								Toast.makeText(activity, "Deleting " + item.getName() + " : error", Toast.LENGTH_SHORT).show();
						} catch (CloudAppException e)
						{
							e.printStackTrace();
						}

						break;

					case Constants.FILE_ACTION_OPEN:

						break;

					case Constants.FILE_ACTION_RENAME:

						onStopAction();

						try
						{
							if (msg.arg1 == 1)
							{
								String[] data = (String[]) msg.obj;

								Toast.makeText(activity, "Renaming " + data[1] + " : success", Toast.LENGTH_SHORT).show();

								activity.getActionBar().setSubtitle(data[1]);

								// mCallbacks.onFileRenamed(msg);
								// item.setName(data[1]);
							}
							else
								Toast.makeText(activity, "Renaming " + item.getName() + " : error", Toast.LENGTH_SHORT).show();
						} catch (CloudAppException e)
						{
							e.printStackTrace();
						}

						break;

					case Constants.FILE_ACTION_SAVE:

						onStopAction();

						try
						{
							if (msg.arg1 == 1)
							{
								Toast.makeText(activity, "Downloading " + item.getName() + " : success", Toast.LENGTH_SHORT).show();

								if (msg.arg2 == 1)
									openFile();
							}
							else if (msg.arg1 == 0)
							{
								Toast.makeText(activity, "Downloading " + item.getName() + " : canceled", Toast.LENGTH_SHORT).show();
							}

						} catch (CloudAppException e)
						{
							e.printStackTrace();
						}

						break;

					case Constants.FILE_ACTION_SHARE:

						if (msg.arg1 == 1)
						{
							Toast.makeText(activity, "Copying link to clipboard : success", Toast.LENGTH_SHORT).show();
						}
						else
							Toast.makeText(activity, "Copying link to clipboard : error", Toast.LENGTH_SHORT).show();

						break;

					case Constants.FILE_ACTION_PICTURE_LOADED:

						imageLoader.setVisibility(View.GONE);

						if (imageBitmap != null)
						{
							displayView(imageView);

							imageView.setImageBitmap(imageBitmap);
						}
						else
						{
							displayView(imageView);
							Display display = activity.getWindowManager().getDefaultDisplay();

							imageView.setAdjustViewBounds(true);
							imageView.setMaxHeight((int) (display.getHeight() * 0.5));
							imageView.setMaxWidth((int) (display.getWidth() * 0.5));
							imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_nopreview3));
						}

						break;
				}
			}
		};
	}

	public Handler getHandler()
	{
		return this.handler;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if (imageBitmap != null)
		{
			imageBitmap.recycle();
			imageBitmap = null;
		}
	}
}
