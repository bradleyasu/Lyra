package com.hexotic.shadow.components.panels.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.buttons.SoftButton;
import com.hexotic.lib.ui.panels.SimpleScroller;
import com.hexotic.shadow.components.panels.footer.FilterBox;
import com.hexotic.shadow.components.panels.footer.FilterBoxListener;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;

public class ShadowsMenu extends JPanel{
	
	private ShadowLogs menuItems;
	private List<MenuListener> listeners;
	private FilterBox filterBox;
	private Image notFoundIcon;
	private Font font;
	private SoftButton openLocal;
	private SoftButton openSsh;
	
	public ShadowsMenu(){
		this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.setLayout(new BorderLayout());
		listeners = new ArrayList<MenuListener>();
		loadResources();
		
		menuItems = new ShadowLogs();

		// Create a button panel for opening logs
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setPreferredSize(new Dimension(35,35));
		buttonPanel.setBackground(Theme.FOOTER_BACKGROUND);
		openSsh = new SoftButton("Open SSH");
		openSsh.setBackgroundColor(Theme.FOOTER_BACKGROUND);
		openSsh.setPreferredSize(new Dimension(70,22));
		openSsh.setForegroundColor(Theme.FOOTER_FONT_COLOR);
		openSsh.setArc(2);
		openSsh.setFont(font);
		buttonPanel.add(openSsh);
		
		openLocal = new SoftButton("Open");
		openLocal.setBackgroundColor(Theme.FOOTER_BACKGROUND);
		openLocal.setPreferredSize(new Dimension(70,22));
		openLocal.setForegroundColor(Theme.FOOTER_FONT_COLOR);
		openLocal.setArc(2);
		openLocal.setFont(font);
		buttonPanel.add(openLocal);
		
		
		this.add(buttonPanel, BorderLayout.NORTH);
		
		// Create a scroll area where open logs will be listed
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
		
		
		
		// Create filter box for filtering out open logs
		this.add(scroller, BorderLayout.CENTER);
		filterBox = new FilterBox();
		filterBox.addFilterBoxListener(new FilterBoxListener() {
			@Override
			public void filterChanged(String filter, boolean hasFocus, boolean canceled) {
				if(canceled){
					notifyListeners(MenuEvent.CLOSE_MENU, null);
				} else if(!hasFocus) {
					ShadowMenuItem item = menuItems.selectFirstVisibleItem();
					notifyListeners(MenuEvent.OPEN_SELECTED_LOG, item);
					notifyListeners(MenuEvent.CLOSE_MENU, null);
				} else {
					menuItems.filterItems(filter);
				}
			}
		});
		this.add(filterBox, BorderLayout.SOUTH);
	}
	
	public void setOpenAction(ActionListener listener){
		for(ActionListener l : openLocal.getActionListeners()){
			openLocal.removeActionListener(l);
		}
		openLocal.addActionListener(listener);
	}
	
	public void setOpenSshAction(ActionListener listener){
		for(ActionListener l : openLocal.getActionListeners()){
			openSsh.removeActionListener(l);
		}
		openSsh.addActionListener(listener);
	}
	
	private void loadResources() {
		try {
			notFoundIcon = Resources.getInstance().getImage("fileTypes/question.png");
		} catch (ResourceException e) {
			e.printStackTrace();
		}
		font = Theme.FOOTER_FONT.deriveFont((float)11.0).deriveFont(Font.BOLD);
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
	
	public void focusFilterBox() {
		if(filterBox != null){
			filterBox.getField().setText("");
			filterBox.getField().requestFocus();
		}
	}
	
	public class ShadowLogs extends JPanel {
		
		private Map<String, ShadowMenuItem> items;
		private int visibleCount = 0;
		
		public ShadowLogs() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			//this.setLayout(new AnimatedGridLayout(0,0,true));
			this.setBackground(Theme.FOOTER_BACKGROUND);
			items = new HashMap<String, ShadowMenuItem>();
		}
		
		public void addItem(Log log){
			ShadowMenuItem item = new ShadowMenuItem(items.size(), log);
			if(!items.containsKey(log.getLogId())){
				items.put(log.getLogId(), item);
				this.add(item, 0);
				visibleCount++;
			}
		}
		
		public void removeItem(ShadowMenuItem item){
			if(items.containsKey(item.getLog().getLogId())){
				if(item.isVisible()){
					visibleCount--;
				}
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
			visibleCount = 0;
			for(ShadowMenuItem item : items.values()){
				item.setVisible(item.getFileName().toLowerCase().contains(filter.toLowerCase()));
				if(item.isVisible()){
					visibleCount++;
				}
			}
			revalidate();
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2d.setColor(Theme.FOOTER_FONT_COLOR);
			g2d.setFont(font);
			if(items.isEmpty()){
				g2d.drawString("Welcome!",  20,40);
				g2d.drawString("To get started, open a file that you would like",  20,80);
				g2d.drawString("to shadow.  The contents will be listed out and", 20, 100);
				g2d.drawString("updated live as the file updates on disk.", 20, 120);
			} else if(!items.isEmpty() && visibleCount == 0){
				g2d.drawImage(notFoundIcon, getWidth()/2-notFoundIcon.getWidth(null)/2, 100, null);
				g2d.drawString("Nothing open matches your filter", 65, 200);
				
			}
		}
	}
}
