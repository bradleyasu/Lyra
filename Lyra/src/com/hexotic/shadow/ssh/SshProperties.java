package com.hexotic.shadow.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class SshProperties {

	private static final char[] PASSWORD = "NOTREALPWD".toCharArray();
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
	
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
			username = decrypt(properties.getProperty("ssh.username", ""));
			password = decrypt(properties.getProperty("ssh.password", ""));
			
			port = Integer.parseInt(properties.getProperty("ssh.port", "").replaceAll("[^0-9]", ""));
			logFilePath = properties.getProperty("ssh.path", "");
			
			
		} catch(IOException e){
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public void savePropertiesFile(String ip, String username, String password, int port, String logFilePath){
		OutputStream output = null;
		try{
			output = new FileOutputStream(sshadowFile);
			
			properties.setProperty("ssh.ip", ip);
			properties.setProperty("ssh.username", encrypt(username));
			properties.setProperty("ssh.password", encrypt(password));
			properties.setProperty("ssh.port", String.valueOf(port));
			properties.setProperty("ssh.path", logFilePath);
			
			properties.store(output, "\n\nSSHadow file\nContains connection information for remote shadow files\n\nHexotic Software\n\n");
		} catch(IOException e){
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
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
	
	private String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    private String base64Encode(byte[] bytes) {
        return new Base64().encodeAsString(bytes);
    }

    private String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private byte[] base64Decode(String property) throws IOException {
        return new Base64().decode(property);
    }
	
	
}
