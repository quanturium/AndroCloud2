package com.quanturium.androcloud.requests;


public class FilesRequestQuery extends AbstractTaskQuery
{	
	private static final long serialVersionUID = 1L;
	
	public final int			page;
	public final int			nbFiles;
	public final boolean		trashed;

	public FilesRequestQuery(String username, String password, int page, int nbFiles, boolean trashed)
	{
		super(username, password);
		this.page = page;
		this.nbFiles = nbFiles;
		this.trashed = trashed;
	}
}
