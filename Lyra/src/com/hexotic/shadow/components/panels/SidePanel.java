package com.hexotic.shadow.components.panels;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class SidePanel extends JPanel {

	public SidePanel() {
		this.setBackground(Theme.SIDEBAR_BACKGROUND);
		this.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH, Constants.SIDEBAR_HEIGHT));
	}
}
