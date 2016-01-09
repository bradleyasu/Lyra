package com.hexotic.shadow.components.frames;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.buttons.SoftButton;
import com.hexotic.lib.ui.input.textfield.ModernPasswordField;
import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.ssh.SshProperties;

public class SshFrame extends JInternalFrame{

	private JPanel sshPanel;
	private ModernTextField sshName;
	private ModernTextField sshIp;
	private ModernTextField sshUser;
	private ModernPasswordField sshPassword;
	private ModernTextField sshPort;
	private ModernTextField sshPath;
	private SoftButton saveButton;
	private SoftButton cancelButton;
	private Image banner;
	
	private List<DialogListener> listeners;
	
	public SshFrame() {
		this.setSize(500, 330);
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
	
		listeners = new ArrayList<DialogListener>();
		
		this.setLayout(new BorderLayout());
		sshPanel = new SshPanel();
		this.add(sshPanel);
		try {
			banner = Resources.getInstance().getImage("sshBanner.png");
		} catch (ResourceException e) { 
			e.printStackTrace();
		}
	}
	
	public void reset(){
		sshIp.setText("");
		sshUser.setText("");
		sshPassword.setText("");
		sshPath.setText("");
		sshPort.setText("22");
		sshPort.setAccepted(true);
	}
	
	public void addDialogListener(DialogListener listener){
		listeners.add(listener);
	}
	
	class SshPanel extends JPanel {
		private Font font;
		private JLabel statusLabel;
		
		public SshPanel() {
			this.setBackground(Theme.DIALOG_COLOR);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			font = Theme.FOOTER_FONT.deriveFont((float)14.0).deriveFont(Font.BOLD);
			
			JLabel spaceLabel = new JLabel();
			spaceLabel.setPreferredSize(new Dimension(500, 90));
			
			
			
			sshName = new ModernTextField("", "Shadow Name (name this shadow connection)");
			sshName.setPreferredSize(new Dimension(434, 30));
			sshName.setFont(font);
			sshName.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshName.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshName.setAccepted((sshName.getText().length() > 0));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
			
			
			sshIp = new ModernTextField("", "Remote IP address");
			sshIp.setPreferredSize(new Dimension(350, 30));
			sshIp.setFont(font);
			sshIp.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshIp.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshIp.setAccepted(isValidIp(sshIp.getText()));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
			
			sshPort = new ModernTextField("", "Port");
			sshPort.setPreferredSize(new Dimension(80, 30));
			sshPort.setFont(font);
			sshPort.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshPort.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshPort.setAccepted(isValidPort(sshPort.getText()));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
			
			sshUser = new ModernTextField("", "Username for remote login");
			sshUser.setPreferredSize(new Dimension(434, 30));
			sshUser.setFont(font);
			sshUser.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshUser.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshUser.setAccepted((sshUser.getText().length() > 0));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
			
			sshPassword = new ModernPasswordField("", "Password for remote login");
			sshPassword.setPreferredSize(new Dimension(434, 30));
			sshPassword.setFont(font);
			sshPassword.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshPassword.setEchoChar('*');
			sshPassword.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshPassword.setAccepted((sshPassword.getPassword().length > 0));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
	
			
			sshPath = new ModernTextField("", "Remote path to file (ex: /home/shadow.log)");
			sshPath.setPreferredSize(new Dimension(434, 30));
			sshPath.setFont(font);
			sshPath.setSelectionColor(Theme.LINE_SELECT_COLOR);
			sshPath.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg0) {
				}
				@Override
				public void keyReleased(KeyEvent arg0) {
					sshPath.setAccepted(isValidPath(sshPath.getText()));
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
	
			statusLabel = new JLabel();
			statusLabel.setPreferredSize(new Dimension(183, 50));
			statusLabel.setHorizontalAlignment(JLabel.RIGHT);
			statusLabel.setVerticalAlignment(JLabel.CENTER);
			statusLabel.setFont(font);
			
			cancelButton = new SoftButton("Cancel");
			cancelButton.setForegroundColor(Theme.FOOTER_BACKGROUND_DARKER);
			cancelButton.setBackgroundColor(Theme.DIALOG_COLOR);
			cancelButton.setArc(2);
			cancelButton.setFont(font);
			cancelButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					for(DialogListener listener : listeners){
						listener.eventPerformed(new DialogEvent(DialogEvent.CANCELLED));
					}
				}
			});
			
			saveButton = new SoftButton("Shadow File");
			saveButton.setForegroundColor(Theme.FOOTER_FONT_COLOR);
			saveButton.setBackgroundColor(Theme.FOOTER_BACKGROUND);
			saveButton.setArc(2);
			saveButton.setFont(font);
			saveButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					File sshadowFile = new File(Constants.SSH_DIR+"\\"+sshName.getText()+".sshadow");
					SshProperties properties = new SshProperties(sshadowFile);
					String ip = sshIp.getText();
					String username = sshUser.getText();
					String password = new String(sshPassword.getPassword());
					int port = Integer.parseInt(sshPort.getText().replaceAll("[^0-9]", ""));
					String logFilePath = sshPath.getText();
					
					properties.savePropertiesFile(ip, username, password, port, logFilePath);
					for(DialogListener listener : listeners){
						listener.eventPerformed(new DialogEvent(DialogEvent.FILE_SUBMIT, sshadowFile));
					}
				}
			});
			
			this.add(spaceLabel);
			this.add(sshName);
			this.add(sshIp);
			this.add(sshPort);
			this.add(sshUser);
			this.add(sshPassword);
			this.add(sshPath);
			this.add(statusLabel);
			this.add(saveButton);
			this.add(cancelButton);
			
		}
		
		/**
		 * Checks to see if the IP address provides is valid or not
		 * @param ip
		 * @return True if valid, false otherwise
		 */
		private boolean isValidIp (String ip) {
		    try {
		        if ( ip == null || ip.isEmpty() ) {
		            return false;
		        }
		        String[] parts = ip.split( "\\." );
		        if ( parts.length != 4 ) {
		            return false;
		        }

		        for ( String s : parts ) {
		            int i = Integer.parseInt( s );
		            if ( (i < 0) || (i > 255) ) {
		                return false;
		            }
		        }
		        if ( ip.endsWith(".") ) {
		            return false;
		        }

		        return true;
		    } catch (NumberFormatException nfe) {
		        return false;
		    }
		}
		
		private boolean isValidPort(String port){
			boolean valid = false;
			port = port.replaceAll("[^0-9]", "");
			if(!port.isEmpty()){
				int p = Integer.parseInt(port);
				if(p > 0 && p <= 65535){
					valid = true;
				}
			}
			return valid;
		}
		private boolean isValidPath(String path){
			boolean valid = false;
			if(path.startsWith("/") && path.length() > 1){
				valid = true;
			}
			return valid;
		}
		
		private void setStatus(String status, boolean error){
			statusLabel.setText(status);
			if(error){
				statusLabel.setForeground(Theme.ERROR_COLOR);;
			} else {
				statusLabel.setForeground(Theme.FOOTER_BACKGROUND_DARKER);
			}
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Theme.DIALOG_BORDER_COLOR);
			g2d.setStroke(new BasicStroke(3));
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			g2d.fillRect(0, 0, getWidth(), 80);
			g2d.setStroke(new BasicStroke(1));
			
			g2d.drawImage(banner, 0,0, null);
		}
	}
}
