package com.hexotic.shadow.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FooterBar extends JPanel{


	private FooterMenuItem primaryMenuItem;
	private FilterBox filter;
	
	public FooterBar() {
		this.setPreferredSize(new Dimension(Constants.FOOTER_SIZE, Constants.FOOTER_SIZE));
		this.setBackground(Theme.FOOTER_BACKGROUND);
		//this.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
		this.setLayout(new BorderLayout());
		try {
			primaryMenuItem = new FooterMenuItem(Resources.getInstance().getImage("icon_sm.png"));
			this.add(primaryMenuItem, BorderLayout.WEST);
		} catch (ResourceException e1) {
			e1.printStackTrace();
		}
		
		setupCounters();
		
		filter = new FilterBox();
		this.add(filter, BorderLayout.EAST);

	}
	
	
	public FilterBox getFilterBox() {
		return filter;
	}
	
	private void setupCounters() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
		panel.setBackground(Theme.FOOTER_BACKGROUND);
		panel.add(new FooterCounter(Theme.SUCCESS_COLOR, 0));
		panel.add(new FooterCounter(Theme.INFO_COLOR, 30));
		panel.add(new FooterCounter(Theme.WARNING_COLOR, 3));
		panel.add(new FooterCounter(Theme.ERROR_COLOR, 6));
		panel.add(new FooterCounter(Theme.LINE_BOOKMARK_COLOR, 4));
		this.add(panel, BorderLayout.CENTER);
	}
	
	public FooterMenuItem getPrimaryMenuItem() {
		return primaryMenuItem;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
}
