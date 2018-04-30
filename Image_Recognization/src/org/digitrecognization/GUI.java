/*
 * MAIN UI
 */
package org.digitrecognization;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;

public class GUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static int HEIGHT = 480;
	protected static int WIDTH = 400;
	protected JButton configureButton, trainButton, testButton, evalButton, genButton;
	protected JButton matrixButton;
	protected JTextArea textArea;
	
	public GUI(){
		setLayout(new FlowLayout());
		
		String msgAuthor = "CONFIGURE VARIABLES FIRST...\n"
				+ "IN IMAGE PATH, PROVIDE PATH TO FOLDER \n"
				+ "THAT CONTAINS TRAINING IMAGES.\n"
				+ "IN CSV PATH, PROVIDE OF CSV FILE.\n"
				+ "IN MAX HEIGHT, PROVIDE HEIGHT OF IMAGE.\n"
				+ "IN MAX WIDTH, PROVIDE WIDTH OF IMAGE.\n"
				+ "PROVIDED HEIGHT AND WIDTH WILL BE CONVERTED.\n"
				+ "TO MULTIPLE OF 5...\n"
				+ "FOR FURTHER DETAIL REFER DOCUMENTATION.\n";
		
		textArea = new JTextArea(msgAuthor,22, 30);
		add(textArea);
		
		configureButton = new JButton("CONFIGURE");
		add(configureButton);
		
		trainButton = new JButton("TRAIN");
		add(trainButton);
		
		testButton = new JButton("TEST");
		add(testButton);
		
		evalButton = new JButton("EVALUATE");
		add(evalButton);
		
		genButton = new JButton("GENERATE DATASET");
		add(genButton);
		
		matrixButton = new JButton("CONFUSION MATRIX");
		add(matrixButton);
		
		configureButton.addActionListener(new ButtonListener());
		trainButton.addActionListener(new ButtonListener());
		testButton.addActionListener(new ButtonListener());
		evalButton.addActionListener(new ButtonListener());
		matrixButton.addActionListener(new ButtonListener());
		genButton.addActionListener(new ButtonListener());
	}
	
	public class ButtonListener implements ActionListener{
		protected APInterface api = new APInterface();
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == configureButton){
				//TODO
				String result = "";
				ConfigVariablesGUI cvg = new ConfigVariablesGUI(GUI.this);
				cvg.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				cvg.setSize(400, 300);
				cvg.setVisible(true);
				cvg.setResizable(false);
				cvg.setTitle("Configuration");
				JSONObject jobj = cvg.getJsonObj();
				for(Object key : jobj.keySet()){
					String keys = (String)key;
					String values = (String) jobj.get(keys);
					result = api.configure(keys, values);
				}
				api.reloadmanager();
				if(result==""){
					result = "Configuration was not set";
				}
				textArea.setText(result);
			}
			else if(e.getSource() == trainButton){
				textArea.setText(api.train());
				//TODO
			}
			else if(e.getSource() == testButton){
				String fileName = "";
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Image file", "jpg", "jpeg", "png", "bmp", "tif");
				fc.setFileFilter(filter);
				int response = fc.showOpenDialog(null);
				if(response == JFileChooser.APPROVE_OPTION){
					fileName = fc.getSelectedFile().toString();
					textArea.setText(api.test(fileName));
				} else{
					textArea.setText("No files selected");
				}
				//TODO
			}
			else if(e.getSource() == evalButton){
				//TODO
				textArea.setText(api.evalulate());
			}
			else if(e.getSource() == matrixButton){
				//TODO
				textArea.setText(api.confusionMatrix());
			}
			else if(e.getSource()== genButton){
				textArea.setText(api.generate());
			}
			else{
				//TODO
			}
		}
	}
	
	public static void main(String[] args){
		GUI gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(WIDTH, HEIGHT);
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setTitle("MAIN UI");
		gui.setLocation(500, 100);
	}
}
