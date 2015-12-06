package com.hexotic.lyra.components.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.lyra.components.panels.LogPanel;
import com.hexotic.lyra.components.panels.SidePanel;
import com.hexotic.lyra.constants.Constants;
import com.hexotic.lyra.constants.Theme;
import com.hexotic.lyra.logs.Log;

public class LyraFrame extends JInternalFrame{

	private LogPanel logPanel;
	private SidePanel sidePanel;
	
	public LyraFrame() {
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
		
		scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private boolean lock = false;
			private int previousMax = 0;
			
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	        	int extent = scroller.getVerticalScrollBar().getModel().getExtent();
	        	if(e.getAdjustable().getMaximum() != previousMax && e.getValue()+extent >= previousMax){
	        		lock = true;
	        	} else {
	        		lock = false;
	        	}
	        	if(lock){
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        	}
	        	previousMax = e.getAdjustable().getMaximum();
	        }
	    });
		
		sidePanel = new SidePanel();
		
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(sidePanel, BorderLayout.WEST);
		
		
		this.add(panel, BorderLayout.CENTER);

		
	}
}
