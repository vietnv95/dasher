package com.phamkhanh.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;



public class ImageLoader {
	private static Map maps = new HashMap<String,BufferedImage>();
	
	private static GraphicsConfiguration gc = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	public static BufferedImage loadImage(String imageName) {
		try {
			BufferedImage im = ImageIO.read(new File("resources/images/"+imageName));
			int transparency = im.getColorModel().getTransparency();
			BufferedImage copy = gc.createCompatibleImage(im.getWidth(),
					im.getHeight(), transparency);
			Graphics2D g2d = copy.createGraphics();

			// copy image
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();

			return copy;
		} catch (Exception e) {
			System.out.println("Error load image");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void loadImage(){
		File dirImage = new File("resources/images");
		if(dirImage.isDirectory()){
			for(File f : dirImage.listFiles()){
				try{
					BufferedImage im = ImageIO.read(f);
					int transparency = im.getColorModel().getTransparency();
					BufferedImage copy = gc.createCompatibleImage(im.getWidth(),
							im.getHeight(), transparency);
					Graphics2D g2d = copy.createGraphics();
	
					// copy image
					g2d.drawImage(im, 0, 0, null);
					g2d.dispose();
					
					// add image to maps to use latter
					maps.put(f.getName(), copy);
				}catch(Exception e){
					System.out.println("Error load Images");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static BufferedImage getImage(String imageName){
		return (BufferedImage) maps.get(imageName);
	}
	
	public static void main(String[] args){
		loadImage();
		System.out.println(maps);
	}
}
