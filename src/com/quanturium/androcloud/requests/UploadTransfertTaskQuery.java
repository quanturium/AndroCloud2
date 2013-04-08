package com.quanturium.androcloud.requests;

import java.io.File;

public class UploadTransfertTaskQuery extends AbstractTaskQuery
{
	public final File file;
	
	public UploadTransfertTaskQuery(String username, String password, File file)
	{
		super(username, password);
		this.file = file;
	}
}
