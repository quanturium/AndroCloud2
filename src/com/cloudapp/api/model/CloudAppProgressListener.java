package com.cloudapp.api.model;

/**
 * Listener to receive notification as data's written to the output stream during upload.
 */
public interface CloudAppProgressListener {
    void transferred(long written, long length);
    boolean isRunning();
    
    public static CloudAppProgressListener NO_OP = new CloudAppProgressListener() {
        public void transferred(long written, long length) {}
        public boolean isRunning() {return true;}
    };   
}
