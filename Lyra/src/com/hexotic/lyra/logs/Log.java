package com.hexotic.lyra.logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.hexotic.lyra.constants.Constants;

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
	
	public void notifyListeners(String line) {
		for(LogListener listener : listeners) {
			listener.lineAppeneded(line);
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
				notifyListeners(line);
			}
			@Override
			public void handle(Exception arg0) {
			}
			@Override
			public void init(Tailer arg0) {
			}
	    	
	    }, Constants.REFRESH_RATE);
	}
}
