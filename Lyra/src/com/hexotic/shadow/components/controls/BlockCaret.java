package com.hexotic.shadow.components.controls;


//CornerCaret.java
//A custom caret class.
//

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class BlockCaret extends DefaultCaret {

	public BlockCaret() {
		setBlinkRate(500); // half a second
	}

	protected synchronized void damage(Rectangle r) {
		if (r == null)
			return;
		// give values to x,y,width,height (inherited from java.awt.Rectangle)
		x = r.x;
		y = r.y;
		width = 2;
		height = r.height;
		repaint(); // calls getComponent().repaint(x, y, width, height)
	}

	public void paint(Graphics g) {
		JTextComponent comp = getComponent();
		if (comp == null)
			return;

		int dot = getDot();
		Rectangle r = null;
		try {
			r = comp.modelToView(dot);
		} catch (BadLocationException e) {
			return;
		}
		if (r == null)
			return;

		if (isVisible()) {
			g.setColor(comp.getCaretColor());
			g.fillRect(r.x,  y, width, r.height);
		}
	}
}