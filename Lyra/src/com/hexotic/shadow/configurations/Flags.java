package com.hexotic.shadow.configurations;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Flags {

	
	public static final String COUNTER_SUCCESS = "0_SUCCESS";
	public static final String COUNTER_INFO = "1_INFO";
	public static final String COUNTER_WARNING = "2_WARNING";
	public static final String COUNTER_ERROR = "3_ERROR";
	public static final String COUNTER_BOOKMARK = "4_BOOKMARK";
	
	private static Flags instance = null;
	
	private Flags() {
		
	}
	
	public Map<String, String> getLogFlags(String logId) {
		Map<String, String> flags = new TreeMap<String, String>();
		
		return flags;
	}
	
	public static Flags getInstance() {
		if(instance == null){
			instance = new Flags();
		}
		
		return instance;
	}
	
}
