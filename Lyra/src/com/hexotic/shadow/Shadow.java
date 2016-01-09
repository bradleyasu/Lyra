package com.hexotic.shadow;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.util.WinOps;
import com.hexotic.shadow.components.frames.BlurFrame;
import com.hexotic.shadow.components.frames.DialogEvent;
import com.hexotic.shadow.components.frames.DialogListener;
import com.hexotic.shadow.components.frames.DropFrame;
import com.hexotic.shadow.components.frames.MenuFrame;
import com.hexotic.shadow.components.frames.ShadowFrame;
import com.hexotic.shadow.components.frames.SshFrame;
import com.hexotic.shadow.constants.Constants;

public class Shadow extends JFrame{

	private JDesktopPane rootPane;
	private ShadowFrame mainFrame;
	private DropFrame dropFrame;
	private BlurFrame blurFrame;
	private SshFrame sshFrame;
	
	private MenuFrame menuFrame;
	
	public Shadow() {
		
		this.setTitle(Constants.APP_NAME+" "+Constants.VERSION+" - "+Constants.APP_COMPANY);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		
		rootPane = new JDesktopPane();
		this.setContentPane(rootPane);
		
		/* Build main view */
		mainFrame = new ShadowFrame();
		
		/* Build Main Menu */
		menuFrame = new MenuFrame();
		
		/* Build Dialog for Creating SSH Connections */
		sshFrame = new SshFrame();
		
		/* Build a "Blur Panel" that will be displayed when prompt boxes are open */
		blurFrame = new BlurFrame();
		
		// The menu has buttons that allow logs to be opened, here's where those actions get set
		// This is happening here so that we can call the openLogs methods directly
		menuFrame.setOpenActions(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				blurFrame.createBlur(getThis().getContentPane());
				blurFrame.setVisible(true);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Open a File to Shadow");
				if (fileChooser.showOpenDialog(getThis()) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  openFile(file);
				  menuFrame.closeMenu();
				}
				blurFrame.setVisible(false);
			}
			
		}, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				blurFrame.createBlur(getThis().getContentPane());
				blurFrame.setVisible(true);
				sshFrame.reset();
				sshFrame.setVisible(true);
			}
		});
		
		/* Build DropFrame used to display to the user the item is being dropped */
		dropFrame = new DropFrame();
		
		rootPane.add(mainFrame, new Integer(5)); 
		rootPane.add(menuFrame, new Integer(10));
		rootPane.add(dropFrame, new Integer(15));
		
		// anything that is a prompt should be displayed at 35 or higher
		rootPane.add(blurFrame, new Integer(30));
		rootPane.add(sshFrame, new Integer(35));
		
		
		mainFrame.setVisible(true);
		menuFrame.setVisible(false);
		dropFrame.setVisible(false);
		blurFrame.setVisible(false);
		sshFrame.setVisible(false);
		
		
		// Register the menu with the main frame so that it will be visible 
		mainFrame.setMenu(menuFrame);
		
		List<Image> icons;
		try {
			icons = getIcons();
			this.setIconImages(icons);
		} catch (ResourceException e1) { /* Do Nothing */}
		
		
		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				
				int targetWidth = e.getComponent().getWidth() - Constants.X_OFFSET;
				int targetHeight = e.getComponent().getHeight() - Constants.Y_OFFSET;
				mainFrame.setSize(targetWidth, targetHeight);
				mainFrame.setLocation(0,0);
				
				blurFrame.setSize(targetWidth, targetHeight);
				blurFrame.setLocation(0,0);
				
				dropFrame.setSize(targetWidth, targetHeight);
				dropFrame.setLocation(0,0);
				
				menuFrame.setSize(Constants.SIDEBAR_WIDTH, targetHeight-Constants.FOOTER_SIZE-(Constants.FOOTER_SIZE/2));
				menuFrame.setLocation(0,Constants.FOOTER_SIZE/2);
				
				sshFrame.setLocation(targetWidth/2 - sshFrame.getWidth()/2, targetHeight/2 - sshFrame.getHeight()/2);
				
				if(blurFrame.isVisible()){
					blurFrame.setVisible(false);
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
		});
		
		sshFrame.addDialogListener(new DialogListener(){
			@Override
			public void eventPerformed(DialogEvent event) {
				if(event.getEvent() == DialogEvent.CANCELLED){
					sshFrame.setVisible(false);
					blurFrame.setVisible(false);
				} else if(event.getEvent() == DialogEvent.FILE_SUBMIT){
					openFile(event.getFile());
					sshFrame.setVisible(false);
					blurFrame.setVisible(false);
					menuFrame.closeMenu();
				}
			}
		});
		
		pack();
		this.setVisible(true);
		WinOps.centreWindow(this);
		

		try {
			setupDragAndDrop();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
	
	private Shadow getThis(){
		return this;
	}
	
	private void setupDragAndDrop() throws TooManyListenersException {
		DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
		dropTarget.addDropTargetListener(new DropTargetListener(){
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				dropFrame.setVisible(true);
				Transferable transferable = dtde.getTransferable();
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        List<File> transferData = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (transferData != null && transferData.size() > 0) {
                       		dropFrame.setHoveringFiles(transferData);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } 
				
			}
			@Override
			public void dragExit(DropTargetEvent arg0) {
				dropFrame.setVisible(false);
			}
			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				dropFrame.setHoverLocation(dtde.getLocation().x, dtde.getLocation().y);
			}
			@Override
			public void drop(DropTargetDropEvent dtde) {
				Transferable transferable = dtde.getTransferable();
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(dtde.getDropAction());
                    try {
                        List<File> transferData = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (transferData != null && transferData.size() > 0) {
                        	for(File file : transferData){
                        		openFile(file);
                        	}
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    dtde.rejectDrop();
                }
				
				dropFrame.setVisible(false);
			}
			@Override
			public void dropActionChanged(DropTargetDragEvent arg0) {
			}
		});
	}
	
	
	private void openFile(File file){
		mainFrame.openLog(file);
	}
	
	private List<Image> getIcons() throws ResourceException {
		List<Image> list = new ArrayList<Image>();
		list.add(Resources.getInstance().getImage("icon_sm.png"));
		list.add(Resources.getInstance().getImage("icon_med.png"));
		return list;
	}
	
	public static void main(String[] args) {
		 try {
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
		
		
		 java.awt.EventQueue.invokeLater(new Runnable() {
	          public void run() {
	               new Shadow();
	          }
	    });
	}
}
