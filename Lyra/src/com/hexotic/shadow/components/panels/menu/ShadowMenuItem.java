package com.hexotic.shadow.components.panels.menu;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.util.StringOps;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogEvent;
import com.hexotic.shadow.logs.LogListener;

public class ShadowMenuItem extends JPanel implements Comparable<ShadowMenuItem>{

	private int itemId = 0;
	
	private Log log;
	private int lineCount = 0;
	private Font lgFont;
	private Font mdFont;
	private Font smFont;
	private boolean isHovering = false;
	private boolean isActive = false;
	private boolean isRemote = false;

	private Image icon;
	private Image errorIcon;
	private Image remoteIcon;
	
	public ShadowMenuItem(int id, Log l) {
		itemId = id;
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH-5, 100));
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		this.log = l;
		
		if(log.getFile().getName().endsWith(".sshadow")){
			isRemote = true;
		}
		
		log.addLogListener(new LogListener(){

			@Override
			public void lineAppended(LogEvent event) {
				lineCount++;
				refresh();
			}

			@Override
			public void logClosed(String logId) {
				remove();
				refresh();
			}

			@Override
			public void logOpened(String logId) {
				lineCount = 0;
				refresh();
			}

			@Override
			public void logActivated(String logId) {
				isActive = true;
				refresh();
			}

			@Override
			public void logNotFound(String logId) {
				lineCount = -1;
				refresh();
			}

			@Override
			public void logDeactivated(String logId) {
				isActive = false;
				
			}
		});
		
		smFont = Theme.FOOTER_FONT.deriveFont((float)8.0);
		mdFont = Theme.FOOTER_FONT.deriveFont((float)12.0);
		lgFont = Theme.FOOTER_FONT.deriveFont((float)16.0).deriveFont(Font.BOLD);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bindMouseListener();
		getIcons();
	}
	
	private void getIcons() {
		String iconName = "default.png";
		if(log.getFile().getName().toLowerCase().endsWith(".csv")){
			iconName = "csv.png";
		} else if(log.getFile().getName().toLowerCase().endsWith(".txt")) {
			iconName = "text.png";
		} else if(log.getFile().getName().toLowerCase().endsWith(".png") || 
				log.getFile().getName().toLowerCase().endsWith(".gif") ||
				log.getFile().getName().toLowerCase().endsWith(".bmp") ||
				log.getFile().getName().toLowerCase().endsWith(".jpg")) {
			iconName = "image.png";
		} 
		try {
			icon = Resources.getInstance().getImage("fileTypes/"+iconName);
			errorIcon = Resources.getInstance().getImage("fileTypes/fileError.png");
			remoteIcon = Resources.getInstance().getImage("flat_remote.png");
		} catch (ResourceException e) { }
	}

	private void bindMouseListener() {
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				isHovering = true;
				refresh();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				isHovering = false;
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				log.activate();
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	
	public int getId() {
		return itemId;
	}
	
	public void remove() {
		
	}
	
	public String getFileName() {
		return log.getFile().getName();
	}
	
	private void refresh(){
		revalidate();
		repaint();
	}
	
	public Log getLog() {
		return log;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int imgWidth = 85;
		if(isHovering){
			g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		} else{
			g2d.setColor(Theme.FOOTER_BACKGROUND);
		}
		// Draw background color
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		// Draw header line
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawLine(0,0,getWidth(), 0);
		
		// Draw footer line
		g2d.setColor(Theme.FOOTER_BACKGROUND.darker());
		g2d.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		
		// Draw icon
		
		// draw labels
		g2d.setColor(Theme.FOOTER_FONT_COLOR);
		g2d.setFont(lgFont);
		g2d.drawString(log.getFile().getName().replace(".sshadow", ""), imgWidth, 40);
		
		g2d.setFont(mdFont);
		
		if(lineCount == 0){
			g2d.drawImage(icon, 10, 10,null);
			g2d.drawString("Connecting...", imgWidth, 60);
			if(log.isActivated()){
				g2d.fillRect(0,0,5,getHeight()-2);
			}
		}else if(lineCount > 0){
			g2d.drawImage(icon, 10, 10,null);
			g2d.drawString("Lines: "+lineCount, imgWidth, 60);
			if(log.isActivated()){
				g2d.fillRect(0,0,5,getHeight()-2);
			}
		} else {
			g2d.drawImage(errorIcon, 10, 10,null);
			g2d.setColor(Theme.ERROR_COLOR);
			if(isRemote){
				g2d.drawString("Remote Connection Failure", imgWidth, 60);
			} else {
				g2d.drawString("File Moved, Missing, or Not Readable", imgWidth, 60);
			}
			
			if(log.isActivated()){
				g2d.fillRect(0,0,5,getHeight()-2);
			}
		}
		if(isRemote) {
			g2d.drawImage(remoteIcon, getWidth()-remoteIcon.getWidth(null)-5, 55, null);
		}
		
		g2d.setFont(smFont);
		Dimension size = StringOps.getStringBounds(g2d, smFont, log.getLogId());
		g2d.setColor(Theme.FOOTER_BACKGROUND.brighter());
		g2d.drawString(log.getLogId(), getWidth()-size.width, getHeight()-5);
		
	}

	@Override
	public int compareTo(ShadowMenuItem item) {
		return item.getId() - getId();
	}
}
