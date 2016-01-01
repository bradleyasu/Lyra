package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.shadow.components.controls.LogLine;
import com.hexotic.shadow.components.panels.FilterBoxListener;
import com.hexotic.shadow.components.panels.FooterBar;
import com.hexotic.shadow.components.panels.FooterMenuItemListener;
import com.hexotic.shadow.components.panels.LogPanel;
import com.hexotic.shadow.components.panels.LogPanelListener;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;

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
			public void filterChanged(String filter, boolean hasFocus) {
				logPanel.filter(filter);
				
				// If the component gives up focus, refocus the main panel
				if(!hasFocus){
					logPanel.requestFocus();
				}
			}
		});
		
		
		logPanel.addLogPanelListener(new LogPanelListener() {
			@Override
			public void lineEvent(int event, LogLine line) {
				switch(event){
				case LogPanelListener.LINE_APPENDED:
					if(footer.getCounters().containsKey(line.getFlag())){
						footer.getCounters().get(line.getFlag()).increment();
					}
					break;
				case LogPanelListener.LINE_REMOVED:
					if(footer.getCounters().containsKey(line.getFlag())){
						footer.getCounters().get(line.getFlag()).decrement();
					}
					if(line.isBookmarked()){
						footer.getCounters().get(FooterBar.COUNTER_BOOKMARK).decrement();
					}
					break;
				case LogPanelListener.LINE_BOOKMARKED:
					footer.getCounters().get(FooterBar.COUNTER_BOOKMARK).increment();
					break;
				case LogPanelListener.LINE_UNBOOKMARKED:
					footer.getCounters().get(FooterBar.COUNTER_BOOKMARK).decrement();
					break;
				}
			}
		});
		
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(footer, BorderLayout.SOUTH);
		this.add(panel, BorderLayout.CENTER);
		
	}
	
	public void openLog(File logFile) {
		footer.reset();
		logPanel.setLog(new Log(logFile));
		logPanel.getLog().startShadow();
	}
	
	public void setMenu(MenuFrame menuFrame){
		this.menu = menuFrame;
		footer.getPrimaryMenuItem().addFooterMenuItemListener(new FooterMenuItemListener(){
			@Override
			public void itemActivated(boolean activated) {
				menu.setVisible(activated);
			}
		});
	}
	
}
