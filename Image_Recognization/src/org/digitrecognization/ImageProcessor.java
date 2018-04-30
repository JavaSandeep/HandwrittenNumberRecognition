/*
 * PREPROCESSING IMAGE FILES
 * DOWNSIZING
 * MOVING TO TMP FOLDER
 */
package org.digitrecognization;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageProcessor extends GenericHanlder{
	protected static int WIDTH;
	protected static int HEIGHT;
    protected static String imageType;
	private Config config;
	
	protected final static Logger logger = LogManager.getLogger(ImageProcessor.class);
	
	public ImageProcessor(){
        this.config = new Config();
		ImageProcessor.WIDTH = Integer.parseInt(config.getProperty("WIDTH_TO_BE"));
		ImageProcessor.HEIGHT = Integer.parseInt(config.getProperty("HEIGHT_TO_BE"));
		ImageProcessor.imageType = config.getProperty("IMAGE_TYPE");
	}
	
	public void copyImage(File src, File dest) throws IOException{
		try{
			Files.copy(src.toPath(), dest.toPath());
		} catch(Exception e){
			logger.error("Could not Copy Image");
		}
	}
	
	public void copyScaleImage(String fileName, File dest){
		 try
         {
             ImageIcon ii = new ImageIcon(fileName);
             BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
             Graphics2D g2d = (Graphics2D)bi.createGraphics();
             g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                     RenderingHints.VALUE_RENDER_QUALITY));
             boolean b = g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
             System.out.println(b);
             ImageIO.write(bi, imageType, dest);
         }
         catch (Exception e)
         {
        	logger.error("Couldnot Copy image "+fileName);
            e.printStackTrace();
         }
	}
	
	public void PreprocessImage(String filePath) throws IOException{
		logger.debug("Preprocessing "+filePath);
		String pathToImage = filePath;
		//String pathToImage = config.getProperty("IMAGES_FOLDER") +"\\"+ fileName;
		if(CheckFile(pathToImage)){
			Image srcImage = new Image(pathToImage);
			
			File imageFile = new File(pathToImage);
			String fileName = imageFile.getName();
			String pathToDestImage = ".\\tmp\\" + fileName;
			File destImageFile = new File(pathToDestImage);
			if(srcImage.getWidth() == WIDTH && srcImage.getHeight() == HEIGHT){
				copyImage(imageFile, destImageFile);
			} else {
				copyScaleImage(pathToImage, destImageFile);
			}
		}
	}
}
