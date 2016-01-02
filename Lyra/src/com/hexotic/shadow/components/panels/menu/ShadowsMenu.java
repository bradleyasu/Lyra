package com.hexotic.shadow.components.panels.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.shadow.components.panels.footer.FilterBox;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;

public class ShadowsMenu extends JPanel{
	
	private ShadowLogs menuItems;
	public ShadowsMenu(){
		this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.setLayout(new BorderLayout());
		
		menuItems = new ShadowLogs();
		
		JScrollPane scroller = new JScrollPane(menuItems);
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getHorizontalScrollBar().setUI(new SimpleScroller());
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.getVerticalScrollBar().setUnitIncrement(25);
		Dimension scrollSize = new Dimension(5,5);
		scroller.getVerticalScrollBar().setPreferredSize(scrollSize);
		scroller.getHorizontalScrollBar().setPreferredSize(scrollSize);
		scroller.setBackground(Theme.MAIN_BACKGROUND);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		this.add(scroller, BorderLayout.CENTER);
		this.add(new FilterBox(), BorderLayout.SOUTH);
	}
	
	public ShadowLogs getItems() {
		return menuItems;
	}
	
	public class ShadowLogs extends JPanel {
		
		private Map<String, ShadowMenuItem> items;
		
		public ShadowLogs() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBackground(Theme.FOOTER_BACKGROUND);
			items = new HashMap<String, ShadowMenuItem>();
		}
		
		public void addItem(ShadowMenuItem item){
			if(!items.containsKey(item.getLog().getLogId())){
				items.put(item.getLog().getLogId(), item);
				this.add(item);
			}
		}
		
		public void removeItem(ShadowMenuItem item){
			if(items.containsKey(item.getLog().getLogId())){
				this.remove(items.get(item.getLog().getLogId()));
			}
		}
	}
}
