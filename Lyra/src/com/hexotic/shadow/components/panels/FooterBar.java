package com.hexotic.shadow.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FooterBar extends JPanel{

	// Static counter names
	public static final String COUNTER_SUCCESS = "0_SUCCESS";
	public static final String COUNTER_INFO = "1_INFO";
	public static final String COUNTER_WARNING = "2_WARNING";
	public static final String COUNTER_ERROR = "3_ERROR";
	public static final String COUNTER_BOOKMARK = "4_BOOKMARK";
	
	
	private FooterMenuItem primaryMenuItem;
	private FilterBox filter;
	
	// Line counters - Key being the counter name and value being the counter control
	private Map<String, FooterCounter> counters;
	
	public FooterBar() {
		this.setPreferredSize(new Dimension(Constants.FOOTER_SIZE, Constants.FOOTER_SIZE));
		this.setBackground(Theme.FOOTER_BACKGROUND);
		counters = new TreeMap<String, FooterCounter>();
		
		this.setLayout(new BorderLayout());
		try {
			primaryMenuItem = new FooterMenuItem(Resources.getInstance().getImage("icon_med.png"));
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
		counters.put(COUNTER_SUCCESS, new FooterCounter(Theme.SUCCESS_COLOR, 0));
		counters.put(COUNTER_INFO, new FooterCounter(Theme.INFO_COLOR, 0));
		counters.put(COUNTER_WARNING, new FooterCounter(Theme.WARNING_COLOR, 0));
		counters.put(COUNTER_ERROR, new FooterCounter(Theme.ERROR_COLOR, 0));
		counters.put(COUNTER_BOOKMARK, new FooterCounter(Theme.LINE_BOOKMARK_COLOR, 0));
		
		for(FooterCounter counter : counters.values()){
			panel.add(counter);
		}
		
		this.add(panel, BorderLayout.CENTER);
	}
	
	public Map<String, FooterCounter> getCounters() {
		return counters;
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
