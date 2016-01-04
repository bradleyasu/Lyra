package com.hexotic.shadow.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SshProperties {

	private Properties properties;
	private File sshadowFile;
	private String hostIp;
	private String username;
	private String password;
	private int port;
	private String logFilePath;
	
	public SshProperties(File sshadowFile) {
		this.sshadowFile = sshadowFile;
		properties = new Properties();
	}
	
	public void readPropertiesFile(){
		InputStream input = null;
		try{
			input = new FileInputStream(sshadowFile);
			properties.load(input);
			
			hostIp = properties.getProperty("ssh.ip", "");
			username = properties.getProperty("ssh.username", "");
			password = properties.getProperty("ssh.password", "");
			port = Integer.parseInt(properties.getProperty("ssh.port", "").replaceAll("[^0-9]", ""));
			logFilePath = properties.getProperty("ssh.path", "");
			
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void savePropertiesFile(String ip, String username, String password, int port, String logFilePath){
		OutputStream output = null;
		try{
			output = new FileOutputStream(sshadowFile);
			
			properties.setProperty("ssh.ip", ip);
			properties.setProperty("ssh.username", username);
			properties.setProperty("ssh.password", password);
			properties.setProperty("ssh.port", String.valueOf(port));
			properties.setProperty("ssh.path", logFilePath);
			
			properties.store(output, "SSHadow file\nContains connection information for remote shadow files\n\nHexotic Software");
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			if(output != null){
				try{
					output.close();
				} catch (IOException e){}
			}
		}
		// After properties file has been written, read the file back into memory
		readPropertiesFile();
	}

	public Properties getProperties() {
		return properties;
	}

	public File getSshadowFile() {
		return sshadowFile;
	}

	public String getHostIp() {
		return hostIp;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public String getLogFilePath() {
		return logFilePath;
	}
	
	
}
