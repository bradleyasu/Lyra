package com.hexotic.shadow.components.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.shadow.constants.Constants;

public class BlurFrame extends JInternalFrame{

	private JPanel blurPanel;
	private BufferedImage background;
	
	public BlurFrame() {
		this.setLayout(new BorderLayout());
		blurPanel = new BlurPanel();
		
		this.setSize(Constants.WIDTH-Constants.X_OFFSET, Constants.HEIGHT-Constants.Y_OFFSET);
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		
		this.add(blurPanel, BorderLayout.CENTER);
	}
	
	public void createBlur(Container fromToBlur) {
		BufferedImage sourceImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage destImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = sourceImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		fromToBlur.print(g);
		background = sourceImage;
		revalidate();
		repaint();
		
		int radius = 4;
		float[] matrix = makeKernel(radius, radius);
		
		BufferedImageOp op = new ConvolveOp( new Kernel(radius, radius, matrix) );
		background = op.filter(sourceImage, destImage);
		revalidate();
		repaint();
	}
	
	 private float[] makeKernel(int radius, float sigma) {
         float[] kernel = new float[radius * radius];
         float sum = 0;
         for (int y = 0; y < radius; y++) {
                 for (int x = 0; x < radius; x++) {
                         int off = y * radius + x;
                         int xx = x - radius / 2;
                         int yy = y - radius / 2;
                         kernel[off] = (float) Math.pow(Math.E, -(xx * xx + yy * yy)
                                         / (2 * (sigma * sigma)));
                         sum += kernel[off];
                 }
         }
         for (int i = 0; i < kernel.length; i++)
                 kernel[i] /= sum;
         return kernel;
 }
		
	class BlurPanel extends JPanel {
		public BlurPanel(){
			
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
			g2d.drawImage(background, -1, -1, getWidth()+2, getHeight()+2, null);
			
			g2d.setColor(new Color(10,10,10,100));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
