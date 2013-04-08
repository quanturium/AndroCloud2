package com.quanturium.androcloud;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyExceptionHandler implements UncaughtExceptionHandler 
{       
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        ex.printStackTrace();
    }
}
