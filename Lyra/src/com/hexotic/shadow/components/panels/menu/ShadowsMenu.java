package com.hexotic.shadow.components.panels.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.shadow.components.panels.footer.FilterBox;
import com.hexotic.shadow.components.panels.footer.FilterBoxListener;
import com.hexotic.shadow.constants.Theme;

public class ShadowsMenu extends JPanel{
	
	private ShadowLogs menuItems;
	private List<MenuListener> listeners;
	
	public ShadowsMenu(){
		this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.setLayout(new BorderLayout());
		listeners = new ArrayList<MenuListener>();
		
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
		FilterBox filterBox = new FilterBox();
		filterBox.addFilterBoxListener(new FilterBoxListener() {
			@Override
			public void filterChanged(String filter, boolean hasFocus, boolean canceled) {
				if(canceled){
					System.out.println("Close Menu");
				} else if(!hasFocus) {
					ShadowMenuItem item = menuItems.selectFirstVisibleItem();
					notifyListeners(MenuEvent.OPEN_SELECTED_LOG, item);
				} else {
					menuItems.filterItems(filter);
				}
			}
		});
		this.add(filterBox, BorderLayout.SOUTH);
	}
	
	public void addMenuListener(MenuListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners(int event, Object o){
		for(MenuListener listener : listeners){
			listener.menuActionPerformed(new MenuEvent(event, o));
		}
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
				items.remove(item.getLog().getLogId());
			}
		}
		
		public ShadowMenuItem selectFirstVisibleItem() {
			ShadowMenuItem item = null;
			for(Component c : this.getComponents()){
				if(c.isVisible()){
					item = (ShadowMenuItem)c;
					break;
				}
			}
			return item;
		}
		
		private void filterItems(String filter){
			for(ShadowMenuItem item : items.values()){
				item.setVisible(item.getFileName().contains(filter));
			}
		}
	}
}
