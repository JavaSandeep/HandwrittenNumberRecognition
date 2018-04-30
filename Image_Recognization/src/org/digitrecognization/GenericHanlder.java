package org.digitrecognization;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenericHanlder {
	protected final static Logger logger = LogManager.getLogger(ImageProcessor.class);
	
	public boolean CheckFile(String filePath){
		File f = new File(filePath);
        if(!f.exists() && !f.isDirectory()) { 
        	logger.error("File Doesn't Exists "+filePath);
            return false;
        }
        return true;
	}
	
	public boolean CheckFolder(String filePath){
		File f = new File(filePath);
        if(!f.exists() && f.isDirectory()) { 
        	logger.error("Folder Doesn't Exists "+filePath);
            return false;
        }
        return true;
	}
}
