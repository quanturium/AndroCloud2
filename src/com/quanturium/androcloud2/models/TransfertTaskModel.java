package com.quanturium.androcloud2.models;

public class TransfertTaskModel
{
	public enum Type{
		DOWNLOAD,UPLOAD
	};
	
	private final int id;
	private final String name;
	private final int progress;
	private final Type type;
	
	public TransfertTaskModel(int id, String name, int progress, Type type)
	{
		this.id = id;
		this.name = name;
		this.progress = progress;
		this.type = type;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getProgress()
	{
		return this.progress;
	}
	
	public Type getType()
	{
		return this.type;
	}
}
