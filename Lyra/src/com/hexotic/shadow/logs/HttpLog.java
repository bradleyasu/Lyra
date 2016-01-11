package com.hexotic.shadow.logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;

public class HttpLog implements Log{
	
	private static final int DOWNLOAD_WAIT = 1000;
	
	private String logId;
	private File configFile;
	// Alarm flags - Key is type of flag and value is the text required to trigger flag in line
	private Map<String, String> flags;
	private List<LogListener> listeners;
	
	private boolean isActivated = false;
	private boolean started = false;
	private Thread workerThread;
	private Tailer tailer;
	
	private File downloadCache;
	private URL url;

	public HttpLog(String logId, File configFile) {
		this.logId = logId;
		this.configFile = configFile;
		this.listeners = new ArrayList<LogListener>();
		
		
		flags = Flags.getInstance().getLogFlags(logId);
		for(String flag : flags.keySet()){
			addFlag(flag, flags.get(flag));
		}
		
		try {
			downloadCache = new File("C:\\Users\\Bradley\\AppData\\Roaming\\Shadow\\connections\\download.dlog");
			if(downloadCache.exists()){
				downloadCache.delete();
			}
			url = new URL("http://192.168.1.211");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	
	public void addFlag(String type, String query) {
		flags.put(type,query);
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void startShadow() {
		if(started){
			close();
		}
		
		started = true;
		
		workerThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(started){
					try {
						updateDownload();
					} catch (IOException e) { /* Typically this means the log isn't updated */ }
					try {
						Thread.sleep(DOWNLOAD_WAIT);
					} catch (InterruptedException e) { }
				}
			}
		});
		workerThread.start();
		tail();
	}

	private void tail() {
		tailer = Tailer.create(downloadCache, new TailerListener() {
			@Override
			public void fileNotFound() {
				for(LogListener listener : listeners){
					listener.logNotFound(logId);
				}
			}
			@Override
			public void fileRotated() {
				System.out.println("Rotated");
			}
			@Override
			public void handle(String line) {
				for(LogListener listener : listeners){
					listener.lineAppended(new LogEvent(getLogId(), line, LogEvent.APPEND, checkFlags(line)));
				}
			}
			@Override
			public void handle(Exception arg0) {
				arg0.printStackTrace();
			}
			@Override
			public void init(Tailer arg0) {
				for(LogListener listener : listeners){
					listener.logOpened(getLogId());
				}
			}
			
		}, Constants.REFRESH_RATE);
	}
	
	/**
	 *  Read the line and see if there is any text that would trigger a flag
	 */
	public String checkFlags(String line){
		for(String flag : flags.keySet()) {
			if(line.contains(flags.get(flag))){
				return flag;
			}
		}
		return "";
	}
	
	private void updateDownload() throws IOException {
        URLConnection con = url.openConnection();
 
        InputStream fis = con.getInputStream();
        OutputStream out;
         
        if(downloadCache.exists()) {
            out = new FileOutputStream(downloadCache, true); //makes the stream append if the file exists
        } else {
            out = new FileOutputStream(downloadCache); //creates a new file.
        }
 
        fis.skip(downloadCache.length());
 
        byte buf[] = new byte[1024];
        int len;
 
        while((len = fis.read(buf))>0) {
            out.write(buf, 0, len);
        }
 
        fis.close();
        out.close();
	}

	
	@Override
	public void close() {
	    // Close existing threads
	    if(workerThread != null){
	    	workerThread.interrupt();
	    	workerThread = null;
	    }
	    if(tailer != null){
	    	tailer.stop();
	    	tailer = null;
	    }
		started = false;
		for(LogListener listener : listeners){
			listener.logClosed(getLogId());
		}
	}
	
	
	@Override
	public void addLogListener(LogListener listener) {
		listeners.add(listener);
	}

	@Override
	public void activate() {
		if(!isActivated){
			for(LogListener listener : listeners){
				listener.logActivated(getLogId());
			}
			isActivated = true;
		}
	}

	@Override
	public void deactivate() {
		if(isActivated){
			for(LogListener listener : listeners){
				listener.logDeactivated(getLogId());
			}
			isActivated = false;
		}
	}

	@Override
	public void setActive(boolean active) {
		isActivated = true;
	}

	@Override
	public boolean isActivated() {
		return isActivated;
	}

	@Override
	public String getLogId() {
		return logId;
	}

	@Override
	public File getFile() {
		return configFile;
	}
}
