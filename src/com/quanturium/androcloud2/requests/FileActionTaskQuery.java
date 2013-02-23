package com.quanturium.androcloud2.requests;

import com.cloudapp.api.model.CloudAppItem;

public class FileActionTaskQuery extends AbstractTaskQuery
{
	public enum ActionType
	{
		DELETE, RENAME
	}

	public final ActionType	type;
	public final CloudAppItem item;
	public final String newName;

	public FileActionTaskQuery(String username, String password, CloudAppItem item, ActionType type, String newName)
	{
		super(username, password);
		this.type = type;
		this.item = item;
		this.newName = newName;
	}

}
