/*
 * Stores Pixel Matrix of Binary Image
 * Converts Pixel Matrix to Feature Vector
 * Using Horizontal Cell Projection Method
 * Cells is NUMBER OF HORIZONTAL SECTION
 * Image is divided into
 */
package org.digitrecognization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CelledProjector {
    private final int cells;
    private int[][] pixelMatrix;
    private double[] featureVector;
    private int THRESHOLD = 1;
    
    protected final static Logger logger = LogManager.getLogger(CelledProjector.class);

    public CelledProjector(int cells) {
    	// K value
        this.cells = cells;
    }
    
    public void SetPixelMatrix(int[][] pixelMatrix) {
        // TODO: Write cleaner code
        logger.debug("Enter function setPixelMatrix");
        try{
	        this.pixelMatrix = pixelMatrix;
	        int addition = pixelMatrix.length % cells == 0 ? 0 : pixelMatrix.length;
	        int length = cells * pixelMatrix.length + addition;
	        featureVector = new double[length];
        } catch(Exception e){
        	logger.error("Could not Set Matrix "+e);
        }
        logger.debug("Exit function setPixelMatrix");
    }
    
    public void Compute() {
        logger.debug("Enter function compute");
        int m = pixelMatrix.length;
        int n = pixelMatrix[0].length;
        double q = (double)n / (double)cells;
        
        try{
	        for(int i = 0; i < m; ++i) {
	            for(int j = 0; j < n; ++j) {
	                if(pixelMatrix[i][j] == THRESHOLD) {
	                    int index = (int)(i + m * Math.floor(j / q));
	                    try{
	                        featureVector[index] = 1.0;
	                    } catch(Exception e) {System.out.println(i + " " + j);}
	                }
	            }
	        }
        } catch(Exception e){
        	logger.error("Could not calculate featureVector "+e);
        }
        logger.debug("Exit function compute");
    }
    
    public double[] getFeatureVector() {        
        return featureVector;
    }
}