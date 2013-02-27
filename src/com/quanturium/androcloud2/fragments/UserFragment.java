package com.quanturium.androcloud2.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.impl.model.CloudAppAccountImpl;
import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.UserAdapter;
import com.quanturium.androcloud2.tools.Prefs;
import com.quanturium.androcloud2.tools.Tools;

public class UserFragment extends AbstractListFragment implements OnItemClickListener
{
	private UserAdapter	adapter;
	
	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_user, "Account", null, false, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		adapter = new UserAdapter(getActivity(), getUserData());
		configureListview(getListView());
		setListAdapter(adapter);
	}
	
	public Map<Integer,String> getUserData()
	{
		Map<Integer,String> datas = new HashMap<Integer, String>();
		
		try
		{
			CloudAppAccountImpl account = new CloudAppAccountImpl(new JSONObject(Prefs.getPreferences(getActivity()).getString(Prefs.USER_INFOS, null)));			
			datas.put(UserAdapter.ITEM_SUBSCRIBED, account.isSubscribed() ? "Yes" : "No");
			datas.put(UserAdapter.ITEM_SUBSCRIBED_EXPIRES, account.SubscriptionExpiresAt() == null ? "n/a" : Tools.getDateFormated(account.SubscriptionExpiresAt(), Tools.dateFormatSimple));
			datas.put(UserAdapter.ITEM_DOMAIN, (account.getDomain() == null || account.getDomain().equals("") || account.getDomain().equals("null")) ? "n/a" : account.getDomain());
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloudAppException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return datas;
	}

	private void configureListview(ListView listView)
	{
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
		switch(position)
		{
			case UserAdapter.ITEM_LOG_OUT : 								
				
				this.mCallbacks.onUserLoggedOut();
				
				break;
		}
	}
}
