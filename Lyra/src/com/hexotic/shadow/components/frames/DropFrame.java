package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.util.StringOps;
import com.hexotic.shadow.constants.Constants;
import com.hexotic.shadow.constants.Theme;

public class DropFrame extends JInternalFrame{

	private JPanel dropPanel;
	private int xLocation = 0;
	private int yLocation = 0;
	private List<File> hoveringFiles;
	private Font font;
	
	public DropFrame() {
		font = Theme.FOOTER_FONT.deriveFont((float)10.0).deriveFont(Font.BOLD);
		this.setSize(Constants.WIDTH-Constants.X_OFFSET, Constants.HEIGHT-Constants.Y_OFFSET);
		this.setLocation(0,0);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		

		dropPanel = new DropPanel();
		this.add(dropPanel, BorderLayout.CENTER);
	}
	
	public void setHoverLocation(int x, int y){
		xLocation = x;
		yLocation = y;
		refresh();
	}
	
	public void setHoveringFiles(List<File> files){
		this.hoveringFiles = files;
		refresh();
	}
	
	public void refresh() {
		dropPanel.revalidate();
		dropPanel.repaint();
		revalidate();
		repaint();
	}

	
	class DropPanel extends JPanel {
		public DropPanel() {
			this.setBackground(Theme.FOOTER_BACKGROUND_DARKER);
			this.setLayout(new FlowLayout());
		}

		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			Point2D center = new Point2D.Float(xLocation, yLocation);
		    float radius = getWidth()/2;
		    Point2D focus = new Point2D.Float(getWidth()/2, getHeight()/2);
		    float[] dist = {0.0f, 0.5f};
		    Color[] colors = {Theme.FOOTER_BACKGROUND_DARKER, Theme.FOOTER_BACKGROUND};
		    RadialGradientPaint p = new RadialGradientPaint(center, radius, focus, dist, colors, CycleMethod.NO_CYCLE);
			
			g2d.setPaint(p);
			g2d.fillRect(0,0, getWidth(), getHeight());
			
			try {
				Image img = Resources.getInstance().getImage("icon_lg.png");
				g2d.drawImage(img, getWidth()/2-img.getWidth(null)/2, getHeight()/2-img.getHeight(null)/2, null);
			} catch (ResourceException e) {
				e.printStackTrace();
			}
			
			if(hoveringFiles != null){
				g2d.setFont(font);
				g2d.setColor(Theme.FOOTER_FONT_COLOR);
				int fileLabelY = 0;
				int fileLabelX = 20;
				String header = "Drop to Shadow:";
				Dimension fsize = StringOps.getStringBounds(g2d, font, header);
				
				fileLabelY += (int)(fsize.getHeight()+2);
				
				g2d.drawString(header, fileLabelX-10, fileLabelY);
				int opacity = 255;
				for(File file : hoveringFiles){
					String info = file.getName();
					fsize = StringOps.getStringBounds(g2d, font, info);
					fileLabelY += (int)(fsize.getHeight()+2);
					g2d.drawString(info, fileLabelX, fileLabelY);
					
					// fade out
					opacity -= 25;
					if(opacity < 0){
						opacity = 0;
					}
					g2d.setColor(new Color(g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), opacity));
				}
				
			}

		}
	}
	
	
}
