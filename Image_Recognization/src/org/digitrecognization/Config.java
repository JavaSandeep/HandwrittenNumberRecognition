package org.digitrecognization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    Properties configFile;
    
    public Config()
    {
    configFile = new java.util.Properties();
    try {
      configFile.load(this.getClass().getClassLoader().
      getResourceAsStream("config.cfg"));
    }catch(Exception eta){
        eta.printStackTrace();
    }
    }

    public String getProperty(String key)
    {
	    String value = this.configFile.getProperty(key);
	    return value;
    }
    
    public void setProperty(String key, String value){
    	Properties prop = new Properties();
    	try{
    		prop.load(new FileInputStream(".\\configurations\\config.cfg"));
    		prop.setProperty(key, value);
    		prop.store(new FileOutputStream(".\\configurations\\config.cfg"), null);
    	} catch(IOException e) {
    		e.printStackTrace();
        }
    }
}
