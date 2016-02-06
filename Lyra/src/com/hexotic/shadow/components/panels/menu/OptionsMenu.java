package com.hexotic.shadow.components.panels.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;

import com.hexotic.lib.switches.BasicSwitch;
import com.hexotic.lib.ui.input.textfield.ModernTextField;
import com.hexotic.shadow.constants.Theme;

public class OptionsMenu extends JPanel{

	private Font font;
	
	public OptionsMenu() {
		this.setBackground(Theme.FOOTER_BACKGROUND);
		
		font = Theme.FOOTER_FONT.deriveFont((float)12.0);
		
		this.setLayout(new FlowLayout());
		
		BasicSwitch horizontalScroll = new BasicSwitch("Never", "Always", 150, 20, 5);
		formatSwitch(horizontalScroll);
		
		ModernTextField lineLimit = new ModernTextField("2000", "# of lines to shadow");
		lineLimit.setPreferredSize(new Dimension(200, 20));
		lineLimit.setFont(font);
		
		this.add(horizontalScroll);
		this.add(lineLimit);
		
	}
	
	
	private void formatSwitch(BasicSwitch bs) {
		bs.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
		bs.setFontColor(Theme.FOOTER_FONT_COLOR);
		bs.setFont(font);
		bs.setForeground(Theme.FOOTER_FONT_COLOR);
	}
}
