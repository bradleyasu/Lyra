package com.hexotic.shadow.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class FilterBox extends JPanel{

	private JTextField field;
	private List<FilterBoxListener> listeners;
	
	public FilterBox() {
		field = new JTextField();
		field.setFont(Theme.FOOTER_FONT.deriveFont((float)14.0).deriveFont(Font.BOLD));
		listeners = new ArrayList<FilterBoxListener>();
		field.setSelectionColor(Theme.LINE_SELECT_COLOR);
		//field.showIcons(false);
		field.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		field.setForeground(Theme.FOOTER_FONT_COLOR);
		field.setBorder(BorderFactory.createEmptyBorder());
		field.setCaretColor(Theme.FOOTER_FONT_COLOR);
		this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		this.setLayout(new BorderLayout(4,4));
		this.setPreferredSize(new Dimension(250,Constants.FOOTER_SIZE));
		
		// Bind on change event to notify listeners
		field.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// whenever a key is pressed, notify listeners that the state has changed
				notifyListeners();
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		try {
			Image ico = Resources.getInstance().getImage("flat_search.png");
			JLabel searchLabel = new JLabel(new ImageIcon(ico));
			this.add(searchLabel, BorderLayout.EAST);
		} catch (ResourceException e) {
			e.printStackTrace();
		}
		
		this.add(field, BorderLayout.CENTER);
		
	}
	
	private void notifyListeners(){
		for(FilterBoxListener listener : listeners){
			listener.filterChanged(field.getText());
		}
	}
	
	public void addFilterBoxListener(FilterBoxListener listener){
		listeners.add(listener);
	}
	
	public JTextField getField() {
		return field;
	}
	
}
