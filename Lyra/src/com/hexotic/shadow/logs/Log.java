package com.hexotic.shadow.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.hexotic.shadow.constants.Constants;

public class Log {

	private File log;
	private Tailer tailer;
	private List<LogListener> listeners;
	
	public Log(File log) {
		this.log = log;
		listeners = new ArrayList<LogListener>();
		bindListeners();
	}
	
	public Tailer getTailer() {
		return tailer;
	}
	
	public void addLogListener(LogListener listener){
		listeners.add(listener);
	}
	
	public void notifyListeners(String line, int event) {
		for(LogListener listener : listeners) {
			listener.lineAppeneded(line, event);
		}
	}
	
	private void bindListeners() {
	    tailer = Tailer.create(log, new TailerListener() {
			@Override
			public void fileNotFound() {
			}
			@Override
			public void fileRotated() {
			}
			@Override
			public void handle(String line) {
				notifyListeners(line, LogListener.APPEND);
			}
			@Override
			public void handle(Exception arg0) {
			}
			@Override
			public void init(Tailer arg0) {
				notifyListeners("", LogListener.INIT);
			}
	    	
	    }, Constants.REFRESH_RATE);
	}
}
