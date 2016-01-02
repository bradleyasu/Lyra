package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.shadow.components.panels.menu.AboutMenu;
import com.hexotic.shadow.components.panels.menu.MenuControlPanel;
import com.hexotic.shadow.components.panels.menu.MenuControlPanelListener;
import com.hexotic.shadow.components.panels.menu.MenuEvent;
import com.hexotic.shadow.components.panels.menu.MenuListener;
import com.hexotic.shadow.components.panels.menu.OptionsMenu;
import com.hexotic.shadow.components.panels.menu.ShadowMenuItem;
import com.hexotic.shadow.components.panels.menu.ShadowsMenu;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;
import com.hexotic.shadow.logs.Log;

public class MenuFrame extends JInternalFrame{
	
	private MenuPanel innerPanel;
	public MenuFrame(){
		this.setSize(Constants.SIDEBAR_WIDTH, Constants.SIDEBAR_HEIGHT-Constants.Y_OFFSET-20-10);
		this.setLocation(0,Constants.FOOTER_SIZE/2);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		innerPanel = new MenuPanel();
		this.add(innerPanel, BorderLayout.CENTER);
	}
	
	public void addItem(Log log){
		innerPanel.openLog(log);
	}
	
	class MenuPanel extends JPanel {
		
		private static final String MENU_SHADOW = "SHADOWS";
		private static final String MENU_OPTIONS = "OPTIONS";
		private static final String MENU_ABOUT = "ABOUT";
		
		private JPanel menuCards; 
		private CardLayout cardLayout;
		
		private MenuControlPanel controlPanel;
		private ShadowsMenu shadowsMenu;
		
		
		public MenuPanel() {
			this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
			this.setLayout(new BorderLayout());
			
			controlPanel = new MenuControlPanel();
			
			controlPanel.addButton(MENU_SHADOW, "Shadows");
			controlPanel.addButton(MENU_OPTIONS, "Options");
			controlPanel.addButton(MENU_ABOUT, "About");
			
			// Set the default option to the shadows menu
			controlPanel.setActive(MENU_SHADOW);
			
			
			// Once the control panel is built, bind the listeners to the buttons
			controlPanel.bindListeners();
			
			controlPanel.addMenuControlPanelListener(new MenuControlPanelListener(){
				@Override
				public void menuItemSelected(String itemId) {
					cardLayout.show(menuCards, itemId);
				}
			});
			
			this.add(controlPanel, BorderLayout.NORTH);
			
			setupCards();
		}
		
		private void setupCards() {
			cardLayout = new CardLayout();
			menuCards = new JPanel(cardLayout);
			shadowsMenu = new ShadowsMenu();
			shadowsMenu.addMenuListener(new MenuListener(){
				@Override
				public void menuActionPerformed(MenuEvent event) {
					if(event.getMenuEventType() == MenuEvent.OPEN_SELECTED_LOG){
						ShadowMenuItem item = (ShadowMenuItem)event.getMenuObject();
						item.getLog().activate();
					}
				}
			});
			
			menuCards.add(shadowsMenu, MENU_SHADOW);
			menuCards.add(new OptionsMenu(), MENU_OPTIONS);
			menuCards.add(new AboutMenu(), MENU_ABOUT);
			
			cardLayout.show(menuCards, MENU_SHADOW);
			
			this.add(menuCards, BorderLayout.CENTER);
		}
		
		public void openLog(Log log) {
			shadowsMenu.getItems().addItem(new ShadowMenuItem(log));
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
	}

}
