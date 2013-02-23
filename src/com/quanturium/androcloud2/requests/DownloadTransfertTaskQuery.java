package com.quanturium.androcloud2.requests;

import java.io.File;

public class DownloadTransfertTaskQuery extends AbstractTaskQuery
{
	public final File	file;
	public final String	url;

	public DownloadTransfertTaskQuery(String username, String password, File file, String url)
	{
		super(username, password);
		this.file = file;
		this.url = url;
	}

}
