package com.hexotic.shadow.logs;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LogFactory {

	private static Map<String, Log> logs = new HashMap<String, Log>();
	
	private LogFactory() {
		
	}
	
	public static Log getLog(File logLocation){
		Log log = null;
		try {
			String id = generateLogId(logLocation);
			if(logs.containsKey(id)){
				log = logs.get(id);
			} else {
				log = new Log(id, logLocation);
				logs.put(id, log);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return log;
	}
	
	private static String generateLogId(File log) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String input = log.getAbsolutePath()+"--"+log.getName();
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.reset();
		byte[] buffer = input.getBytes("UTF-8");
		md.update(buffer);
		byte[] digest = md.digest();
		
		String logId = "";
		for (int i = 0; i < digest.length; i++) {
			logId +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return logId;
    }
	
}
