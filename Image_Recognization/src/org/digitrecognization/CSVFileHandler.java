/*
 * HANDLES CSV FILE
 */
package org.digitrecognization;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileHandler extends GenericHanlder{
	protected static String[][] csvAsArray;
    protected static String csvFilePath;
    protected static int rowCount;
    protected static boolean validation;

	protected final static Logger logger = LogManager.getLogger(CSVFileHandler.class);
    
    private Config config;

    public CSVFileHandler(){
        this.config = new Config();

        String CSV_FILENAME = config.getProperty("CSV_FILE_PATH");
        String FilePath = CSV_FILENAME;
        CSVFileHandler.csvFilePath = FilePath;
        try{
            CSVFileHandler.validation = ValidateCSV(csvFilePath);
            if(validation){
                csvAsArray = new String[rowCount][2];
            } else {
                logger.error("Could not validate");
            }
        }catch(Exception e){
            logger.error("Could not get key. Exception "+ e);
        }
    }

    public boolean ValidateCSV(String csvFilePath) throws Exception{
        logger.info("VALIDATION");
        boolean isValid = true;
        int numberOfRows = 0;

        CheckFile(csvFilePath);
        try (
	            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
	            CSVReader csvReader = new CSVReader(reader);
            ) {
                String[] nextRecord;
                while ((nextRecord = csvReader.readNext()) != null) {
                    numberOfRows++;
                }
        } catch(Exception e){
        	logger.error("Could not open CSV for validation");
        	CSVFileHandler.rowCount = 0;
        	isValid = false;
        }
        logger.debug("Number of rows in CSV "+numberOfRows);
        CSVFileHandler.rowCount = numberOfRows;
        logger.info("RETURN");
        return isValid;
    }

    public void LoadCSV () throws Exception {
        logger.info("Enter function LoadCSV file");
        int pointer = 0;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
                CSVReader csvReader = new CSVReader(reader);
            ) {
            String[] nextRecord;
            logger.info("Read CSV file");
            while ((nextRecord = csvReader.readNext()) != null) {
            	csvAsArray[pointer][0] =  nextRecord[0];
            	csvAsArray[pointer][1] =  nextRecord[1];
            	pointer++;
            }
        }
        logger.info("Exit function LoadCSV file");
    }
    
    public String[][] getCSVasArray(){
        return csvAsArray;
    }

    public int getRowCount(){
        return rowCount;
    }
    
    public static boolean isValidation() {
		return validation;
	}
    
    // For Testing Purpose
    public void printArray(){
    	for(int i = 0; i < rowCount; i++){
    		System.out.print(csvAsArray[i][0]);
    		System.out.print(csvAsArray[i][1]);
    		System.out.print("\n");
    	}
    }
}
