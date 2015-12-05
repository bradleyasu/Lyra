package com.hexotic.lyra;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.hexotic.lib.util.WinOps;
import com.hexotic.lyra.components.frames.LyraFrame;
import com.hexotic.lyra.constants.Constants;

public class Lyra extends JFrame{

	private JDesktopPane rootPane;
	private JInternalFrame mainFrame;
	
	public Lyra() {
		this.setTitle(Constants.APP_NAME+" "+Constants.VERSION+" - "+Constants.APP_COMPANY);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		
		rootPane = new JDesktopPane();
		this.setContentPane(rootPane);
		
		/* Build main view */
		mainFrame = new LyraFrame();
		rootPane.add(mainFrame);
		mainFrame.setVisible(true);
		
		pack();
		this.setVisible(true);
		WinOps.centreWindow(this);
	}
	
	
	
	public static void main(String[] args) {
		new Lyra();
	}
}
