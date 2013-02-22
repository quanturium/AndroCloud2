package com.quanturium.androcloud2.requests;

import java.io.File;

public class UploadTransfertTaskQuery extends SimpleTaskQuery
{
	public final File file;
	
	public UploadTransfertTaskQuery(String username, String password, File file)
	{
		super(username, password);
		this.file = file;
	}
}
