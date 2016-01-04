package com.hexotic.shadow.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;

public class LocalLog implements Log{

	private File logFile;
	private Tailer tailer;
	private List<LogListener> listeners;
	
	// Alarm flags - Key is type of flag and value is the text required to trigger flag in line
	private Map<String, String> flags;
	
	// Total count of different types of flags - Key is the type of flag and value is the total number of them
	private Map<String, Integer> flagCounts;
	private String logId;
	
	private boolean started = false;
	private boolean isActivated = false;
	
	public LocalLog(String id, File logFile) {
		this.logFile = logFile;
		this.logId = id;
		listeners = new ArrayList<LogListener>();
		flags = new HashMap<String, String>();
		flagCounts = new HashMap<String, Integer>();
		
		// Add test flags
		Map<String, String> flags = Flags.getInstance().getLogFlags(id);
		for(String flag : flags.keySet()){
			addFlag(flag, flags.get(flag));
		}

	}
	
	public File getFile() {
		return logFile;
	}
	
	
	public String getLogId() {
		return logId;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	
	public void setActive(boolean active){
		this.isActivated = active;
	}
	
	public void activate() {
		if(!isActivated){
			for(LogListener listener : listeners){
				listener.logActivated(getLogId());
			}
			isActivated = true;
		}
	}
	
	public void deactivate() {
		if(isActivated){
			for(LogListener listener : listeners){
				listener.logDeactivated(getLogId());
			}
			isActivated = false;
		}
	}
	
	public void close() {
		if(tailer != null) {
			tailer.stop();
		}
		started = false;
		for(LogListener listener : listeners){
			listener.logClosed(getLogId());
		}
	}
	
	public void addFlag(String type, String query) {
		flags.put(type,query);
	}
	
	public Tailer getTailer() {
		return tailer;
	}
	
	public void addLogListener(LogListener listener){
		listeners.add(listener);
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
	
	public boolean isStarted() {
		return started;
	}
	
	public void startShadow() {
		if(!started){
			tailer = Tailer.create(logFile, new TailerListener() {
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
			
			started = true;
		} else {
			// Reload file
			close();
			startShadow();
		}
	}
}
