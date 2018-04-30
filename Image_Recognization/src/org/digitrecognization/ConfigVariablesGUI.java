package org.digitrecognization;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;

public class ConfigVariablesGUI extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel HEIGHT, BREADTH, GAP;
	private JButton csvButton, imageButton, saveButton;
	private JTextField csvField, imageField;
	private JTextField heightField, weightField;
	
	protected JSONObject jsonObj = new JSONObject();
	
	protected APInterface apt = new APInterface();
	
	public JSONObject getJsonObj() {
		return jsonObj;
	}

	//TODO
	public ConfigVariablesGUI(JFrame frame){
		super(frame, "Configuration", true);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		csvButton = new JButton("CSV PATH");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(csvButton, c);

		csvField = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		add(csvField, c);
		
		GAP = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		add(GAP, c);
		
		imageButton = new JButton("IMAGE PATH");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		add(imageButton, c);
		
		imageField = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		add(imageField, c);

		GAP = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		add(GAP, c);

		
		// START HERE
		HEIGHT = new JLabel("MAX HEIGHT");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		add(HEIGHT, c);
		
		heightField = new JTextField(10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		add(heightField, c);
		
		GAP = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		add(GAP, c);

		
		BREADTH = new JLabel("MAX WIDTH");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		add(BREADTH, c);
		
		weightField = new JTextField(10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		add(weightField, c);
		
		GAP = new JLabel(" ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		add(GAP, c);

		
		saveButton = new JButton("SAVE");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 8;
		add(saveButton, c);
		
		csvButton.addActionListener(new EventListen());
		imageButton.addActionListener(new EventListen());
		saveButton.addActionListener(new EventListen());
	}
	
	public class EventListen implements ActionListener{
		protected APInterface api = new APInterface();
		
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == csvButton){
				String fileName = "";
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
				fc.setFileFilter(filter);
				int response = fc.showOpenDialog(null);
				if(response == JFileChooser.APPROVE_OPTION){
					fileName = fc.getSelectedFile().toString();
					csvField.setText(fileName);
				} else{
					fileName = "No files selected";
					csvField.setText(fileName);
				}
			}
			else if(e.getSource() == imageButton){
				String fileName = "";
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int response = fc.showOpenDialog(null);
				if(response == JFileChooser.APPROVE_OPTION){
					fileName = fc.getSelectedFile().toString();
					imageField.setText(fileName);
				} else{
					fileName = "No Folder/s selected";
					imageField.setText(fileName);
				}
			}
			else if(e.getSource() == saveButton){
				String csvPath = " ";
				String imageFolderPath = " ";
				int maxHeight = 0;
				int maxWidth = 0;
				try{
					csvPath = csvField.getText();
					imageFolderPath = imageField.getText();
				} catch(Exception ex){
					csvField.setText("Invalid Field value");
					imageField.setText("Invalid Field value");
				}
				try{
					int Height = Integer.parseInt(heightField.getText());
					int Width = Integer.parseInt(weightField.getText());
					maxWidth = (int)(Width/5) * 5;
					maxHeight = (int)(Height/5) * 5;
					heightField.setText(Integer.toString(maxHeight));
					weightField.setText(Integer.toString(maxWidth));
				} catch(NumberFormatException ex){
					heightField.setText("Invalid Field value");
					weightField.setText("Invalid Field value");
				}
				if(csvPath!="" && imageFolderPath!="" && maxHeight!=0 && maxWidth!=0){
					jsonObj.put("CSV_FILE_PATH", csvPath);
					jsonObj.put("IMAGES_FOLDER", imageFolderPath);
					jsonObj.put("HEIGHT_TO_BE", Integer.toString(maxHeight));
					jsonObj.put("WIDTH_TO_BE", Integer.toString(maxWidth));
				}
				dispose();
				//TODO
			}
			else{
				//TODO
			}
		}
	}
}
