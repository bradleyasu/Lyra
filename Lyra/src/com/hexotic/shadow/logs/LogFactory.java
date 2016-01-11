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
				if(logLocation.getName().endsWith(".sshadow")){
					log = generateSshLog(id, logLocation);
				} else if(logLocation.getName().endsWith(".htshadow")) {
					log = new HttpLog(id, logLocation);
				} else {
					log = new LocalLog(id, logLocation);
				}
				logs.put(id, log);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return log;
	}
	
	private static Log generateSshLog(String id, File sshadowFile){
		Log log = new SshLog(id, sshadowFile);
		return log;
	}
	
	public static Log getLog(String id){
		Log log = null;
		if(logs.containsKey(id)){
			log = logs.get(id);
		}
		return log;
	}
	
	public static void removeLog(String id){
		logs.remove(id);
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
