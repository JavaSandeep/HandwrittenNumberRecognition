package org.digitrecognization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ProtectedProperties;
import weka.core.converters.ArffLoader.ArffReader;

@SuppressWarnings("deprecation")
public class ARFFGenerator{
	
	private final static String arrfFile = ".\\data\\imagedata.arff";
	protected final static Logger logger = LogManager.getLogger(CSVFileHandler.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void createTemplateARFF(int featureLength){
		logger.info("Enter create ARFF function");
		// Nominal Attributes with it's value
		FastVector att = new FastVector();
		Attribute vectorComponent = null;
		Attribute classAttribute = null;

		for(int i=0; i <  featureLength; i++){
			ProtectedProperties real = null;
			vectorComponent = new Attribute("V_comp_"+i, real);
			logger.debug("Vector components as Attribute: V_comp"+i);
			att.add(vectorComponent);
		}

		//Create The Class
		ArrayList digits = new ArrayList(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));
		logger.debug("Class used : 1,2,3,4,5,6,7,8,9");
		
		classAttribute = new Attribute("Digits", digits);
		att.add(classAttribute);
		
		//Create Object Instances
		Instances data = new Instances("Image Instances", att, 0);
		try{
			PrintWriter writer = new PrintWriter(arrfFile, "UTF-8");
			writer.println(data);
			writer.close();
		} catch (IOException e) {
			logger.error("Could not write to file "+arrfFile + " Exception. "+e);
		}	
		logger.info("Exit create ARFF function");
	}
	
	public Instances createARFFInstance(int dataCount) throws IOException
	{
		Instances data = null;
		if(!FileExists(arrfFile)){
			return data;
		}
		BufferedReader reader = new BufferedReader(new FileReader(arrfFile));
		ArffReader arff = new ArffReader(reader, dataCount);
		data = arff.getData();
		return data;
	}

	public Instance getInstanceData(double[] attributeVector, String instanceClass, Instances data){
		Instance instance = new DenseInstance(attributeVector.length+1);
	     
		instance.setDataset(data);
		
		for(int i =0; i < attributeVector.length; i++)
		{
			instance.setValue(i, attributeVector[i]);
		}
		instance.setValue(attributeVector.length, instanceClass);
			
		return instance;
	}
	
	public void writeInstanceDataToARFF(Instances data)
	{
		try{
			PrintWriter instanceWriter = new PrintWriter(arrfFile, "UTF-8");
			instanceWriter.println(data);
			instanceWriter.close();
		} catch (IOException e) {
			logger.error("Could not write data Instance. Exception "+e);
		}
	}
	
	public boolean FileExists(String pathFile){
		File f = new File(arrfFile);
        if(!f.exists() && f.isDirectory()) { 
        	logger.error("File " +arrfFile+ " Doesn't Exists");
        	return false;
        } else {
        	logger.debug("File Exists");
        }
        return true;
	}
}