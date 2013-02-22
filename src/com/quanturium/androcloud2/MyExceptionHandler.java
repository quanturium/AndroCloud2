package com.quanturium.androcloud2;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyExceptionHandler implements UncaughtExceptionHandler 
{       
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        ex.printStackTrace();
    }
}
