/*
 * IMAGE TO PIXEL MATRIX
 * IF RBG VALUE is greater than 340 then A[i][j] = 0
 * ELSE 1
 */
package org.digitrecognization;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ImageFeaturesExtractor {
	// VARIABLES
	
	//RBG value divided by 2.25 
	int THRESHOLD = (int)((255+255+255)/2.25);
	
    protected final static Logger logger = LogManager.getLogger(ImageFeaturesExtractor.class);
    
    //TODO: WRITE CLEAN CODE
    // CONSTRUCTOR
    public ImageFeaturesExtractor(){}

	// load and convert the image into BINARY format
	public int[][] getBinaryImage(Image currentImage) throws Exception {
        logger.info("Enter function getBinaryImage");
		int width = currentImage.getWidth();
		int height = currentImage.getHeight();
		int[][] binaryImage = new int[width][height];
		BufferedImage colorImage = null;
		
		logger.debug("Image: "+currentImage.getFilePathName());
		colorImage = ImageIO.read(new File(currentImage.getFilePathName()));
		
        try{
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    // The colors are inverted
                    Color c = new Color( colorImage.getRGB(x, y));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int resultColor = r + g + b;
                    int monoValue = resultColor < THRESHOLD ? 1 : 0;
                    
                    binaryImage[x][y] = monoValue;                    
               }
            }
        } catch (Exception e){
            logger.error("Could not Binary of file %s. Error %s", currentImage.getFilePathName(), e);
            e.printStackTrace();
        }    
        logger.info("Exit function getBinaryImage");    
        return binaryImage;
	  }
}
