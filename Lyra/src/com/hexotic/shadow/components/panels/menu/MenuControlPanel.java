package com.hexotic.shadow.components.panels.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class MenuControlPanel extends JPanel{

	private List<MenuButton> buttons;
	private List<MenuControlPanelListener> listeners;
	
	public MenuControlPanel() {
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH, 30));
		this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		
		listeners = new ArrayList<MenuControlPanelListener>();
		buttons = new ArrayList<MenuButton>();
	}
	
	public void addMenuControlPanelListener(MenuControlPanelListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners(String id){
		for(MenuControlPanelListener listener : listeners){
			listener.menuItemSelected(id);
		}
	}
	
	public void setActive(String id) {
		for(MenuButton button : buttons){
			button.setActive((id.equals(button.getId())));
		}
		notifyListeners(id);
		revalidate();
		repaint();
	}
	
	public void addButton(String buttonId, String buttonText){
		MenuButton button = new MenuButton(buttonId, buttonText);
		this.add(button);
		buttons.add(button);
	}
	
	public void bindListeners() {
		for(MenuButton button : buttons){
			button.setupMouseListener(this);
		}
	}
}
