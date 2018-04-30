package org.digitrecognization;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	protected int height;
	protected int width;
	protected String filePath;
	protected String classType;
	
	// Constructor
	public Image(String filePath){
		String pathToImage =  filePath;
		this.filePath = pathToImage;
		try {
			getImageAttributes(pathToImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Getting Properties Of Image file
	public void getImageAttributes(String filePath) throws IOException{
		File infile = new File(filePath);
		BufferedImage input = ImageIO.read(infile);
	    this.width = input.getWidth();
	    this.height = input.getHeight();
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getFilePathName() {
		return filePath;
	}
	
	public String getClassType() {
		return classType;
	}
	
	public void setClassType(String classType) {
		this.classType = classType;
	}
}
