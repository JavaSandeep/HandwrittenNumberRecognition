/*
 * Load Bearer, API requests and receive response
 * Handles every requests with possible errors
 * 
 */
package org.digitrecognization;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weka.core.Instances;

public class ProcessManager extends GenericHanlder{
	
	private Config config;
	private DigitClassifier DC;
	protected static int numberOfHorizontalProjections;
	protected static int fileWidth;
	
	protected String preProcessedFilePath = ".\\tmp";
	protected final static Logger logger = LogManager.getLogger(ProcessManager.class);
	
	protected static int numberOfRows;
	protected static String[][] dataArray;
	
	public ProcessManager(){
		this.config = new Config();
		ProcessManager.fileWidth = Integer.parseInt(config.getProperty("WIDTH_TO_BE"));
		ProcessManager.numberOfHorizontalProjections = Integer.
				parseInt(config.getProperty("PROJECTION_LINES"));
		DC = new DigitClassifier();
	}
	
	public boolean TestCSVLoader(){
		try{
			logger.info("Getting an CSV Handler");
			CSVFileHandler csvHandler = new CSVFileHandler();
			logger.debug("Loading CSV");
			csvHandler.LoadCSV();
			logger.debug("Reading values from CSV Handler");
			numberOfRows = csvHandler.getRowCount();
			dataArray = new String[numberOfRows][2];
			dataArray = csvHandler.getCSVasArray();
			logger.debug("Reading CSV. SUCCESS");
		} catch(Exception e){
			logger.error("Could Instantiate CSV Handler. Excpetion "+e);
			logger.debug("Reading CSV. FAILED");
			return false;
		}
		return true;
	}
	
	public boolean TestPreprocess(String pathToImage){
		try{
			logger.debug("Preprocessing File");
			ImageProcessor imgProcessor = new ImageProcessor();
			if(!CheckFile(pathToImage)){
				logger.debug("FAILED.");
				return false;
			}
			imgProcessor.PreprocessImage(pathToImage);
			logger.debug("SUCCESS.");
		} catch(Exception e){
			logger.error("Could not complete preprocessing image files "+e);
			return false;
		}
		return true;
	}
	
	public double[] GetFeaturesVector(Image image){
		logger.debug("Enter GetFeaturesVector function");
		ImageFeaturesExtractor ife = new ImageFeaturesExtractor();
		int length = numberOfHorizontalProjections * fileWidth;
		double[] featureVector = new double[length];
		CelledProjector cellproject = new CelledProjector(numberOfHorizontalProjections);
		try {
			cellproject.SetPixelMatrix(ife.getBinaryImage(image));
			cellproject.Compute();
			featureVector = cellproject.getFeatureVector();
		} catch (Exception e) {
			logger.error("Cannot get Feature Vector "+e);
			e.printStackTrace();
		}
		logger.debug("Exit GetFeaturesVector function");
		return featureVector;
	}
	
	public void CleanUp(){
		logger.debug("Clearing tmp Folder");
		File dir = new File(preProcessedFilePath);
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory()) 
		        file.delete();
	}
	
	//Generate Dataset
	public String MakeARFFFile(){
		logger.info("Enter Make arff file function");
		logger.debug("Sanity Test");
		String error = SanityTest();
		if(error != ""){
			logger.error("Sanity Test. FAILED");
			return error;
		}
		logger.debug("Sanity Test. PASSED");
		ARFFGenerator arffhandler = null;
		Instances instance = null;
		if(!TestCSVLoader()){
			logger.info("Exit Make arff file function");
			return "ERROR csv could not be loaded";
		}
		try{
			logger.info("Creating Template ARFF");
			arffhandler = new ARFFGenerator();
			// HERE IT IS 25 * 5
			arffhandler.createTemplateARFF(numberOfHorizontalProjections * fileWidth);
			instance = arffhandler.createARFFInstance(numberOfRows);
		} catch(Exception e){
			logger.error("Could not create Template ARFF");
		}
		boolean[] doesFileExists = new boolean[numberOfRows];
		for(int i = 0; i < numberOfRows; i++){
			String pathImage = config.getProperty("IMAGES_FOLDER") +"\\"+ dataArray[i][0];
			doesFileExists[i] = TestPreprocess(pathImage);
		}
		
		for(int j = 0; j < numberOfRows; j++){
			if(doesFileExists[j]){
				String filePath = preProcessedFilePath +"\\"+ dataArray[j][0];
				Image img = new Image(filePath);
				logger.debug("Value: "+dataArray[j][1]);
				img.setClassType(dataArray[j][1]);
				//HERE IT IS 25 * 5 + 1
				int length = numberOfHorizontalProjections * fileWidth;
				double[] ftr = new double[length];
				ftr = GetFeaturesVector(img);
				if(ftr.length > (length+1)){
					return "ARFF file not created.";
				}
				logger.debug("Size of feature vector: "+ftr.length);
				instance.add(arffhandler.getInstanceData(ftr, img.getClassType(), instance));
			}
		}
		CleanUp();
		arffhandler.writeInstanceDataToARFF(instance);
		return "ARFF file created. SUCCESSFUL";
	}
	
	public String CallEvaluation(String Type){
		logger.info("Invoking Classifier Evaluation");
		String result = "";
		if(CheckFile(".//data//imagedata.arff")){
			try {
				result = DC.Evaluate(Type);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Could not evalulate classifier "+e);
				result = "Could not evalulate classifier "+e;
				e.printStackTrace();
			}
		} else {
			result = "Dataset not found.\nCreate Dataset from images first\n";
		}
		logger.info("Exiting... Classifier Evaluation");
		return result;
	}
	
	public String CallTrain(){
		logger.info("Invoking Classifier Train");
		String result = "";
		String ARFFPath = ".//data//imagedata.arff";
		if(CheckFile(ARFFPath)){
			try {
				result = DC.train();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Could not train classifier "+e);
				result = "Could not train classifier "+e;
				e.printStackTrace();
			}
		} else {
			result = "Dataset not found.\nCreate Dataset from images first\n";
		}
		return result;
	}
	
	public String CallConfigure(String key, String value){
		if(key != "WIDTH_TO_BE" && key != "HEIGHT_TO_BE"){
			if(CheckFile(value)){
				config.setProperty(key, value);
			} else{
				return "Invalid Path File doesn't exists";
			}
		} else{
			config.setProperty(key, value);
		}
		return "SUCCESS";
	}
	
	public String CallTest(String filePath){
		logger.debug("Enter CallTest Function");
		logger.debug("Image under process: "+filePath);
		String result = "";
		logger.debug("Preprocessing image file. Moving to tmp Folder");
		TestPreprocess(filePath);
		File tmpFile = new File(filePath);
		Image img = new Image(".\\tmp\\"+tmpFile.getName());
		double[] ftr = GetFeaturesVector(img);
		logger.debug("Feature vector of length: "+ftr.length);
		try {
			String resultNum = DC.test(ftr);
			result =  "IT IS IMAGE OF "+resultNum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Could not run test on Classifier: Error "+e);
			e.printStackTrace();
		}
		CleanUp();
		logger.debug("Exit CallTest function");
		return result;
	}
	
	//SANITY TEST
	//Configuration file has valid information or not.
	public String SanityTest(){
		String errorList = "";
		String csvPath = config.getProperty("CSV_FILE_PATH");
		String imageFolder = config.getProperty("IMAGES_FOLDER");
		int width = Integer.parseInt(config.getProperty("WIDTH_TO_BE"));
		int height = Integer.parseInt(config.getProperty("HEIGHT_TO_BE"));

		if(csvPath.length() != 0){
			if(!CheckFile(csvPath)){
				logger.error(csvPath+ " File doesn't exists");
				errorList +=(csvPath+ " File doesn't exists\n");
			}
		} else{
			logger.error("File Path cannot be empty");
			errorList += "File Path cannot be empty\n";
		}
		if(imageFolder.length() != 0){
			if(!CheckFolder(imageFolder)){
				logger.error(imageFolder+ " Folder doesn't exists");
				errorList +=(imageFolder+ " Folder doesn't exists\n");
			}
		} else{
			logger.error("File Path cannot be empty");
			errorList += "File Path cannot be empty\n";
		}
		if(width==0){
			logger.error("Image width cannot be zero");
			errorList += "Image width cannot be zero\n";
		}
		if(height==0){
			logger.error("Image height cannot be zero");			
			errorList += "Image height cannot be zero\n";
		}
		return errorList;
	}
}
