package com.quanturium.androcloud2.requests;

import com.quanturium.androcloud2.databases.FilesDatabase;

public class FilesTaskQuery extends SimpleTaskQuery
{
	public final int				page;
	public final int				nbFiles;
	public final FilesDatabase		database;

	public FilesTaskQuery(String username, String password, FilesDatabase database, int page, int nbFiles)
	{
		super(username, password);
		this.page = page;
		this.nbFiles = nbFiles;
		this.database = database;
	}
}
