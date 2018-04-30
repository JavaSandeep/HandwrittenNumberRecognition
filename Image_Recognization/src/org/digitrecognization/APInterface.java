/*
 * Takes requests from UI
 * Invokes Process Manager with request
 * Takes response and passes to UI
 */
package org.digitrecognization;

public class APInterface {
	protected ProcessManager pm = new ProcessManager();
	
	public String configure(String key, String value){
		String result = pm.CallConfigure(key, value);
		return result;
	}
	
	public String evalulate(){
		String result = pm.CallEvaluation("General");
		return result;
	}
	
	public String confusionMatrix(){
		String result = pm.CallEvaluation("Matrix");
		return result;
	}
	
	public String train(){
		String result = pm.CallTrain();
		return result;
	}
	
	public String test(String filePath){
		String result = pm.CallTest(filePath);
		return result;
	}
	
	public String generate(){
		String result = pm.MakeARFFFile();
		return result;
	}
	
	//RESETS PROCESS MANAGER
	public void reloadmanager(){
		pm = null;
		pm = new ProcessManager();
	}
}