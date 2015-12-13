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
import com.hexotic.shadow.components.panels.FooterBar;
import com.hexotic.shadow.components.panels.LogPanel;
import com.hexotic.shadow.components.panels.MenuBar;
import com.hexotic.shadow.components.panels.SidePanel;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;

public class ShadowFrame extends JInternalFrame{

	private LogPanel logPanel;
	private SidePanel sidePanel;
	
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
		logPanel.setLog(new Log(new File("C:\\Users\\Bradley\\Desktop\\Log.log")));
		final JScrollPane scroller = new JScrollPane(logPanel);
		
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getHorizontalScrollBar().setUI(new SimpleScroller());
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.getVerticalScrollBar().setUnitIncrement(25);
		Dimension scrollSize = new Dimension(5, 5);
		scroller.getVerticalScrollBar().setPreferredSize(scrollSize);
		scroller.getHorizontalScrollBar().setPreferredSize(scrollSize);
		scroller.setBackground(Theme.MAIN_BACKGROUND);
		scroller.setMinimumSize(logPanel.getPreferredSize());
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private boolean lock = false;
			private int previousMax = 0;
			private int lastLineCount = 0;
			private int previousValue = 0;
			
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	        	int extent = scroller.getVerticalScrollBar().getModel().getExtent();
	        	if( e.getValue()+extent >= previousMax){
	        		lock = true;
	        	} else {
	        		lock = false;
	        	}
	        	int currentLineCount = logPanel.getLineCount();
	        	int growth = 20*(currentLineCount - lastLineCount);
	        	
	        	if(lock){
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
	        	} else if(currentLineCount > lastLineCount && previousValue == e.getValue()){
	        		int value = e.getValue() - growth;
	        		e.getAdjustable().setValue(value);
	        		previousValue = value;
	        	} else {
	        		previousValue = e.getValue();
	        	}
	        	lastLineCount = currentLineCount;
	        	previousMax = e.getAdjustable().getMaximum();
	        }
	    });
		
		sidePanel = new SidePanel();
		
		MenuBar menu = new MenuBar();
		FooterBar footer = new FooterBar();
		
		panel.add(menu, BorderLayout.NORTH);
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(footer, BorderLayout.SOUTH);
	//	panel.add(sidePanel, BorderLayout.WEST);
		
		
		this.add(panel, BorderLayout.CENTER);

		
	}
}
