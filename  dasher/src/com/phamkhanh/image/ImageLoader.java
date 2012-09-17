package com.phamkhanh.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
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
}
