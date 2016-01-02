package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.shadow.components.controls.LogLine;
import com.hexotic.shadow.components.panels.LogPanel;
import com.hexotic.shadow.components.panels.LogPanelEvent;
import com.hexotic.shadow.components.panels.LogPanelListener;
import com.hexotic.shadow.components.panels.footer.FilterBoxListener;
import com.hexotic.shadow.components.panels.footer.FooterBar;
import com.hexotic.shadow.components.panels.footer.FooterMenuItemListener;
import com.hexotic.shadow.configurations.Flags;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;
import com.hexotic.shadow.logs.LogFactory;

public class ShadowFrame extends JInternalFrame{

	private LogPanel logPanel;
	private FooterBar footer;
	private MenuFrame menu;
	
	
	public ShadowFrame() {
		this.setLayout(new BorderLayout());
		buildFrame();
		
		this.setSize(Constants.WIDTH-Constants.X_OFFSET, Constants.HEIGHT-Constants.Y_OFFSET);
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
	}
	

	
	private void buildFrame() {
		JPanel panel = new JPanel(new BorderLayout());
		logPanel = new LogPanel();
		final JScrollPane scroller = new JScrollPane(logPanel);
		
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getHorizontalScrollBar().setUI(new SimpleScroller());
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.getVerticalScrollBar().setUnitIncrement(25);
		Dimension scrollSize = new Dimension(15,15);
		scroller.getVerticalScrollBar().setPreferredSize(scrollSize);
		scroller.getHorizontalScrollBar().setPreferredSize(scrollSize);
		scroller.setBackground(Theme.MAIN_BACKGROUND);
		scroller.setMinimumSize(logPanel.getPreferredSize());
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private boolean lock = false;
			private int previousMax = 0;
			private int lastLineCount = 0;
			
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	        	int extent = scroller.getVerticalScrollBar().getModel().getExtent();
	        	if( e.getValue()+extent >= previousMax){
	        		lock = true;
	        	} else {
	        		lock = false;
	        	}
	        	int currentLineCount = logPanel.getLineCount();

	        	if(lock){
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
	        		logPanel.setPauseState(LogPanel.NO_PAUSE);
	        	} else if(currentLineCount > lastLineCount){
	        		logPanel.setPauseState(LogPanel.GROW_BUT_DONT_REMOVE);
	        	}
	        	
	        	lastLineCount = currentLineCount;
	        	previousMax = e.getAdjustable().getMaximum();
	        }
	    });
		
		//menu.setShadow(true);
		
		footer = new FooterBar();
		
		footer.getFilterBox().addFilterBoxListener(new FilterBoxListener(){
			@Override
			public void filterChanged(String filter, boolean hasFocus, boolean canceled) {
				logPanel.filter(filter);
				
				// If the component gives up focus, refocus the main panel
				if(!hasFocus){
					logPanel.requestFocus();
				}
			}
		});
		
		
		logPanel.addLogPanelListener(new LogPanelListener() {
			@Override
			public void lineEvent(LogPanelEvent event) {
				switch(event.getEvent()){
				case LogPanelEvent.LINE_APPENDED:
					if(footer.getCounters().containsKey(((LogLine)event.getEventObj()).getFlag())){
						footer.getCounters().get(((LogLine)event.getEventObj()).getFlag()).increment();
					}
					break;
				case LogPanelEvent.LINE_REMOVED:
					if(footer.getCounters().containsKey(((LogLine)event.getEventObj()).getFlag())){
						footer.getCounters().get(((LogLine)event.getEventObj()).getFlag()).decrement();
					}
					if(((LogLine)event.getEventObj()).isBookmarked()){
						footer.getCounters().get(Flags.COUNTER_BOOKMARK).decrement();
					}
					break;
				case LogPanelEvent.LINE_BOOKMARKED:
					footer.getCounters().get(Flags.COUNTER_BOOKMARK).increment();
					break;
				case LogPanelEvent.LINE_UNBOOKMARKED:
					footer.getCounters().get(Flags.COUNTER_BOOKMARK).decrement();
					break;
				case LogPanelEvent.HOTKEY_FIND:
					footer.getFilterBox().getField().requestFocus();
					break;
				case LogPanelEvent.HOTKEY_CLOSE:
					closeLog();
					break;
				case LogPanelEvent.HOTKEY_SHADOW_MENU:
					footer.getPrimaryMenuItem().click();
					break;
				case LogPanelEvent.ACTIVATE_LOG:
					openLog(LogFactory.getLog((String)event.getEventObj()));
					break;
				}
			}
		});
		
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(footer, BorderLayout.SOUTH);
		this.add(panel, BorderLayout.CENTER);
		
	}
	
	public void closeLog()  {
		footer.reset();
		//logPanel.getLog().close();
		logPanel.reset();
		revalidate();
		repaint();
	}
	
	public void openLog(Log log){
		footer.reset();
		if(!log.isStarted()){
			menu.addItem(log);
		}
		logPanel.setLog(log);
		// Start the shadow process
		logPanel.getLog().startShadow();
	}
	
	public void openLog(File logFile) {
		openLog(LogFactory.getLog(logFile));
	}
	
	public void setMenu(MenuFrame menuFrame){
		this.menu = menuFrame;
		footer.getPrimaryMenuItem().addFooterMenuItemListener(new FooterMenuItemListener(){
			@Override
			public void itemActivated(boolean activated) {
				menu.showMenu(activated);
			}
		});
		
		this.menu.setShadowButton(footer.getPrimaryMenuItem());
	}
	
}
