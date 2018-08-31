package com.cjit.vms.stock.exe;

import java.awt.Color;
import java.awt.FileDialog; 
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu; 
import java.awt.MenuBar; 
import java.awt.MenuItem; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon; 
import javax.swing.ImageIcon; 
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) {
		  final Frame f = new Frame();// 标题 
		  final JLabel lbl = new JLabel(); 
		  f.setSize(1000, 600);// 对话框大小 
		  f.setLocation(100, 100);// 对话框初始位置 
		  f.addWindowListener(new WindowAdapter() { 
		   public void windowClosing(WindowEvent e) { 
		    System.exit(0); 
		   } 
		  }); 
		  
		  
		  
		  MenuBar mb = new MenuBar(); 
 		  Menu m1 = new Menu("photolist"); 
//		  Menu m2 = new Menu("video栏"); 
 		  MenuItem mi1 = new MenuItem("") ;
 		 
 		  
 		  mi1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//FileDialog flDialog  =new file
 			      Icon icon = new ImageIcon("E:\\workspacechongqing\\uploadFile\\fileName\\12.jpeg");
 			      lbl.setIcon(icon);
 			      
			}
		});
 		  
 		  //		  MenuItem mi2 = new MenuItem("open"); 
//		  mi2.addActionListener(new ActionListener() { // 打开文件夹 
//		 
//		     public void actionPerformed(ActionEvent e) { 
//		      FileDialog fd = new FileDialog(f, "photo open", 
//		        FileDialog.LOAD); 
//		      fd.show(); 
//		       
//		      String fileName = fd.getDirectory() + fd.getFile(); 
//		      Icon icon = new ImageIcon(fileName,""); 
//		      lbl.setIcon(icon); 
//		     } 
//		    }); 
//		  MenuItem mi3 = new MenuItem("photo3"); 
//		  MenuItem mi4 = new MenuItem("photo4"); 
//		  MenuItem mi5 = new MenuItem("video1"); 
//		  MenuItem mi6 = new MenuItem("video2"); 
 		  m1.add(mi1); 
//		  m1.add(mi2); 
//		  m1.add(mi3); 
//		  m1.add(mi4); 
//		  m2.add(mi5); 
//		  m2.add(mi6); 
 		  mb.add(m1); 
//		  mb.add(m2); 
		  f.setMenuBar(mb); 
		  f.add(lbl); 
		  f.show(); 
	}
}
class text444{
	public static void main(String[] args) {
		ImageIcon imgIcon = new ImageIcon("E:\\workspacechongqing\\uploadFile\\fileName\\12.jpeg");
		Image theImg = imgIcon.getImage();
		
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bimage.createGraphics();
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		
		FileOutputStream out;
		try {
			out = new FileOutputStream("E:\\workspacechongqing\\uploadFile\\fileName\\12.jpeg");
			ImageIO.write(bimage, "png", new File("E:\\workspacechongqing\\uploadFile\\fileName\\12.jpeg"));
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


