package com.hexotic.shadow.logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.ssh.SshProperties;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshLog implements Log {
	public static final int SSH_WAIT = 1000;

	private String logId;
	private File configFile;
	// Alarm flags - Key is type of flag and value is the text required to trigger flag in line
	private Map<String, String> flags;
	private List<LogListener> listeners;
	
	private boolean isActivated = false;
	private boolean started = false;
	private SshProperties sshConfig;
	
	// JSch ssh fields
	private JSch jsch;
	private ChannelExec m_channelExec;
	private InputStream m_in;
	private Session session;
	private String remoteExecCmd;
	private boolean isSetup = false;
	
	private Thread workerThread;
	
	public SshLog(String logId, File configFile){
		this.logId = logId;
		this.configFile = configFile;
		
		flags = new TreeMap<String, String>();
		listeners = new ArrayList<LogListener>();
		
		// Add test flags
		refreshFlags();
		
		sshConfig = new SshProperties(configFile);
		sshConfig.readPropertiesFile();
	}
	
	public void refreshFlags() {
		this.flags.clear();
		Map<String, String> flags = Flags.getInstance().getLogFlags(logId);
		for(String flag : flags.keySet()){
			addFlag(flag, flags.get(flag));
		}
	}
	
	public void startShadow() {
		try {
			if(started){
				close();
			}
			if(!isSetup){
				connectionSetup();
			}
			workerThread = new Thread(new Runnable(){
				@Override
				public void run() {
					remoteTail();
				}
			});
			workerThread.start();
		} catch (JSchException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connectionSetup() throws JSchException, IOException{
		jsch = new JSch();
		session = jsch.getSession(sshConfig.getUsername(), sshConfig.getHostIp());
		session.setPort(sshConfig.getPort());
		session.setPassword(sshConfig.getPassword());
		Hashtable<String, String> config = new Hashtable<String, String>();
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
	    isSetup = true;
	    
	}
	
	
	public void addFlag(String type, String query) {
		flags.put(type,query);
	}
	
	/**
	 *  Read the line and see if there is any text that would trigger a flag
	 */
	public String checkFlags(String line){
		for(String flag : flags.keySet()) {
			if(line.matches(flags.get(flag))){
				return flag;
			}
		}
		return "";
	}
	
	public String getLogId() {
		return logId;
	}

	@Override
	public boolean isStarted() {
		return started;
	}


	@Override
	public void addLogListener(LogListener listener) {
		listeners.add(listener);
	}

	@Override
	public void close() {
	    started = false;
	    isSetup = false;
	    
	    if(m_channelExec != null){
	    	m_channelExec.disconnect();
	    }
	    
	    if(session != null){
	    	session.disconnect();
	    }
	    
	    // Close existing threads
	    if(workerThread != null){
	    	workerThread.interrupt();
	    	workerThread = null;
	    }
	
		started = false;
		for(LogListener listener : listeners){
			listener.logClosed(getLogId());
		}
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
		isActivated = active;		
	}

	@Override
	public boolean isActivated() {
		return isActivated;
	}

	@Override
	public File getFile() {
		return configFile;
	}

	private void remoteTail() {
		if(isSetup){
			for(LogListener listener : listeners){
				listener.logOpened(logId);
			}
			try {
				
				// Setup session
				session.connect(15000);
			    session.setServerAliveInterval(15000);
			    
			    m_channelExec = (ChannelExec) session.openChannel("exec");
			    remoteExecCmd = "tail -n "+Constants.LOG_MAX+" -f "+sshConfig.getLogFilePath();
			    m_channelExec.setCommand(remoteExecCmd);
			    
			    m_in = m_channelExec.getInputStream();
			    m_channelExec.setPty(true);
			    
				m_channelExec.connect();
				BufferedReader m_bufferedReader = new BufferedReader(new InputStreamReader(m_in));
				started = true;
				while (started) {
					if (m_bufferedReader.ready()) {
						String line = m_bufferedReader.readLine();
						for(LogListener listener : listeners){
							listener.lineAppended(new LogEvent(getLogId(), line, LogEvent.APPEND, checkFlags(line)));
						}
					} else{
						Thread.sleep(SSH_WAIT);
					}
				}
				m_bufferedReader.close();
			} catch (JSchException | IOException e) {
				// On error, notify the listeners that the file isn't being read
				for(LogListener listener : listeners){
					listener.logNotFound(logId);
				}
				
				// Clean up any loose ends
				close();
			} catch (InterruptedException interupt){
				// This is okay, the thread will be interrupted when closing
			}
		}
	}
	
	public Map<String, String> getFlags() {
		return flags;
	}
}
