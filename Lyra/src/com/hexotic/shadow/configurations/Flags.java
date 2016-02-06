package com.hexotic.shadow.configurations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.hexotic.shadow.constants.Constants;

public class Flags {

	
	public static final String COUNTER_SUCCESS = "0_SUCCESS";
	public static final String COUNTER_INFO = "1_INFO";
	public static final String COUNTER_WARNING = "2_WARNING";
	public static final String COUNTER_ERROR = "3_ERROR";
	public static final String COUNTER_BOOKMARK = "4_BOOKMARK";
	private static final String FLAG_FILE = Constants.CONFIG_DIR+"\\flags.properties";
	private static Flags instance = null;
	
	private Properties properties;
	
	private Flags() {
		properties = new Properties();
		loadProperties();
	}
	
	private synchronized void loadProperties(){
		InputStream input = null;
		try{
			input = new FileInputStream(FLAG_FILE);
			properties.load(input);
		} catch (IOException e){
			// Most likely, no file has been created
		} finally {
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {}
			}
		}
	}
	
	public Map<String, String> getLogFlags(String logId) {
		Map<String, String> flags = new TreeMap<String, String>();
		flags.put(COUNTER_SUCCESS, properties.getProperty(logId+"."+COUNTER_SUCCESS, "(.*)(?i)(finished|done|success)(.*)"));
		flags.put(COUNTER_INFO, properties.getProperty(logId+"."+COUNTER_INFO, "(.*)(?i)(info|notification|information)(.*)"));
		flags.put(COUNTER_WARNING, properties.getProperty(logId+"."+COUNTER_WARNING, "(.*)(?i)(warn|warning|caution)(.*)"));
		flags.put(COUNTER_ERROR, properties.getProperty(logId+"."+COUNTER_ERROR, "(.*)(?i)(error|fail|fatal)(.*)"));
		return flags;
	}
	
	
	public synchronized void setFlag(String logId, String flagType, String flagRegEx) {
		properties.setProperty(logId+"."+flagType, flagRegEx);
		
	}
	
	public synchronized void storeProperties() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(FLAG_FILE);
			properties.store(out, "Shadow Flag Configuration Settings");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null){
					out.close();
				}
			} catch (IOException e) { }
		}
	}
	
	public synchronized static Flags getInstance() {
		if(instance == null){
			instance = new Flags();
		}
		
		return instance;
	}
	
}
