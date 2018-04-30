/*
 * CLASS CLASSIFIER
 * Has train, test, evaluate
 * write, load methods
 */
package org.digitrecognization;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;


public class DigitClassifier {
    private Classifier classifier;
    private Instances dataset;
    
    protected String arff = ".\\data\\imagedata.arff";
    protected final static Logger logger = LogManager.getLogger(DigitClassifier.class);
    
    public DigitClassifier(){
    	this.classifier = new IBk();
    }
    
    // TRAIN
    public String train() throws Exception {
	    logger.info("Enter classifier train function");
	    if(!LoadDataSet()){
    		logger.error("Untimely Exit");
	    	return "Dataset Not found. Please Generate Dataset first";
	    }
	    // our class is the last attribute
	    classifier.buildClassifier(dataset);
	    String result = "TRAINING CLASSIFIER.\nSUCCESS.\n";
	    logger.info("Exit classifier train function");
	    WriteClassifier();
	    return result;
    }

    // TEST
    public String test(double[] attributeArray) throws Exception {
    	logger.debug("Enter classifier test function");
    	logger.debug("Length of Attribute vector: "+attributeArray.length);
    	if(!LoadDataSet()){
    		logger.error("Untimely Exit");
	    	return "Dataset Not found. Please Generate Dataset first";
	    }
	    Instance instance = new DenseInstance(dataset.numAttributes());
	    instance.setDataset(dataset);
	    for(int i = 0; i < attributeArray.length; i++){
	    	instance.setValue(dataset.attribute(i), attributeArray[i]);
	    }
	    LoadClassifier();
	    // ARFF GENERATOR {}
	    double[] dist = null;
	    try{
	    	dist = classifier.distributionForInstance(instance);
	    } catch (Exception e){
	    	logger.error("Couldnot classify"+e);
	    	return "TEST FAILED";
	    }
	    double max = 0;
	    int index = 0;
	    for (int counter = 0; counter < dist.length; counter++)
	    {
	    	logger.debug("Number: "+counter+" Dist: "+dist[counter]);
	    	if (dist[counter] >= max)
	    	{
	    		max = dist[counter];
	    		index = counter;
	    	}
	    }
	    return Integer.toString(index);
    }
    
    //EVALUATE
    public String Evaluate(String evalType) throws Exception{
    	logger.debug("Enter classifier evaluation function");
    	if(!LoadDataSet()){
    		logger.error("Untimely Exit");
    		return "Dataset Not found. Please Generate Dataset first";
	    }
    	Evaluation eval = new Evaluation(dataset);
    	eval.crossValidateModel(classifier, dataset, 10, new Random(1));
    	String result = "";
    	if(evalType == "General"){
    		result = eval.toSummaryString("\n10-Fold Validation\n======\n", false);
    		logger.info("10 fold Validation: "+eval.toSummaryString("\nResults\n======\n", false));
    		
    	} else if(evalType == "Matrix"){
    		result = eval.toMatrixString("\nConfusionMatrix\n");
    		logger.info("Confusion Matrix: "+eval.toSummaryString("\nResults\n======\n", false));
    	} else{
    		result = "Evaluation Type Not Defined";
    	}
		logger.info("Exit classifier evaluation function");
    	return result;
    }
    
    public boolean LoadDataSet(){
    	try {
			this.dataset = new Instances(
			  new BufferedReader(new FileReader(arff)));
		    dataset.setClassIndex(dataset.numAttributes() - 1);
		} catch (FileNotFoundException e) {
			logger.error("Dataset not found: "+e);
			return false;
			// TODO Auto-generated catch block
		} catch (IOException e) {
			logger.error("Could not read dataset: "+e);
			return false;
			// TODO Auto-generated catch block
		}
    	return true;
    }
    
    public void WriteClassifier() throws Exception{
    	SerializationHelper.write(".//model//KNN.model", classifier);
    }
    
    public void LoadClassifier(){
    	ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
			        new FileInputStream(".//model//KNN.model"));
		} catch (FileNotFoundException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			classifier  = (Classifier) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
		try {
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
    }
}
