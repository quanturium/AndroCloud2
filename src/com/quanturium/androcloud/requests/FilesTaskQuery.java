package com.quanturium.androcloud.requests;

import com.quanturium.androcloud.databases.FilesDatabase;

public class FilesTaskQuery extends AbstractTaskQuery
{
	public final int			page;
	public final int			nbFiles;
	public final FilesDatabase	database;
	public final boolean		trashed;

	public FilesTaskQuery(String username, String password, FilesDatabase database, int page, int nbFiles, boolean trashed)
	{
		super(username, password);
		this.page = page;
		this.nbFiles = nbFiles;
		this.database = database;
		this.trashed = trashed;
	}
}
