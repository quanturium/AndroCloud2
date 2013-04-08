package com.quanturium.androcloud.models;

import java.util.HashMap;
import java.util.Map;

public class ChangelogEntryItemModel
{
	public enum Type
	{
		NEW("new"), FIXED("fixed");

		private final String					string;
		private static final Map<String, Type>	lookup	= new HashMap<String, Type>();

		static
		{
			for (Type d : Type.values())
				lookup.put(d.toString(), d);
		}

		private Type(String string)
		{
			this.string = string;
		}

		public String toString()
		{
			return string;
		}

		public static Type getEnumFromString(String string)
		{
			return lookup.get(string);
		}
	}

	private final Type		type;
	private final String	data;

	public ChangelogEntryItemModel(String type, String data)
	{
		this.type = Type.getEnumFromString(type);
		this.data = data;
	}

	public Type getType()
	{
		return type;
	}

	public String getData()
	{
		return data;
	}
}
