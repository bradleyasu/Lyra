package com.hexotic.shadow.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.hexotic.shadow.components.panels.FooterBar;
import com.hexotic.shadow.constants.Constants;

public class Log {

	private File log;
	private Tailer tailer;
	private List<LogListener> listeners;
	
	// Alarm flags - Key is type of flag and value is the text required to trigger flag in line
	private Map<String, String> flags;
	
	private boolean started = false;
	
	public Log(File log) {
		this.log = log;
		listeners = new ArrayList<LogListener>();
		flags = new HashMap<String, String>();
		
		// Add test flags
		addFlag(FooterBar.COUNTER_WARNING, "WARN");
		addFlag(FooterBar.COUNTER_ERROR, "ERROR");
		addFlag(FooterBar.COUNTER_INFO, "DEBUG");
		addFlag(FooterBar.COUNTER_SUCCESS, "INFO");
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
	
	public void notifyListeners(String line, int event, String flag) {
		for(LogListener listener : listeners) {
			listener.lineAppeneded(line, event, flag);
		}
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
	
	public void startShadow() {
		if(!started){
			tailer = Tailer.create(log, new TailerListener() {
				@Override
				public void fileNotFound() {
				}
				@Override
				public void fileRotated() {
				}
				@Override
				public void handle(String line) {
					notifyListeners(line, LogListener.APPEND, checkFlags(line));
				}
				@Override
				public void handle(Exception arg0) {
					arg0.printStackTrace();
				}
				@Override
				public void init(Tailer arg0) {
					notifyListeners("", LogListener.INIT, "");
				}
				
			}, Constants.REFRESH_RATE);
			
			started = true;
		}
	}
}
