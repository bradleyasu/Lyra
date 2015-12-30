package com.hexotic.shadow;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.util.WinOps;
import com.hexotic.shadow.components.frames.MenuFrame;
import com.hexotic.shadow.components.frames.ShadowFrame;
import com.hexotic.shadow.constants.Constants;

public class Shadow extends JFrame{

	private JDesktopPane rootPane;
	private ShadowFrame mainFrame;
	
	private MenuFrame menuFrame;
	
	public Shadow() {
		this.setTitle(Constants.APP_NAME+" "+Constants.VERSION+" - "+Constants.APP_COMPANY);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		
		rootPane = new JDesktopPane();
		this.setContentPane(rootPane);
		
		/* Build main view */
		mainFrame = new ShadowFrame();
		menuFrame = new MenuFrame();
		rootPane.add(mainFrame); 
		rootPane.add(menuFrame);
		
		
		mainFrame.setVisible(true);
		menuFrame.setVisible(false);
		
		// Register the menu with the main frame so that it will be visible 
		mainFrame.setMenu(menuFrame);
		
		List<Image> icons;
		try {
			icons = getIcons();
			this.setIconImages(icons);
		} catch (ResourceException e1) { /* Do Nothing */}
		
		
		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				
				int targetWidth = e.getComponent().getWidth() - Constants.X_OFFSET;
				int targetHeight = e.getComponent().getHeight() - Constants.Y_OFFSET;
				mainFrame.setSize(targetWidth, targetHeight);
				mainFrame.setLocation(0,0);
				
				menuFrame.setSize(Constants.SIDEBAR_WIDTH, targetHeight-Constants.FOOTER_SIZE-(Constants.FOOTER_SIZE/2));
				menuFrame.setLocation(0,Constants.FOOTER_SIZE/2);
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
		});
		
		pack();
		this.setVisible(true);
		WinOps.centreWindow(this);
	}
	
	
	private List<Image> getIcons() throws ResourceException {
		List<Image> list = new ArrayList<Image>();
		list.add(Resources.getInstance().getImage("icon_sm.png"));
		list.add(Resources.getInstance().getImage("icon_med.png"));
		return list;
	}
	
	public static void main(String[] args) {
		 java.awt.EventQueue.invokeLater(new Runnable() {
	          public void run() {
	               new Shadow();
	          }
	    });
	}
}
