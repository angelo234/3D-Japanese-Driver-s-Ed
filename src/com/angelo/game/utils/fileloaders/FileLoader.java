package com.angelo.game.utils.fileloaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FileLoader {

	public static List<String> loadFile(String fileDir, String fileTitle){
		boolean isTheFile = false;
		
		List<String> file = new ArrayList<String>();

		FileReader fr = null;

		try {
			fr = new FileReader(new File(fileDir));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(fr);
			String line = null;
			boolean firstLine = true;
			
			while ((line = reader.readLine()) != null) {
				//Checks the first line of the file once, to check if the file has the right file header	
				if(firstLine){
					if(fileTitle == null){
						isTheFile = true;
					}
					else if (line.equals(fileTitle)) {				
						isTheFile = true;
					}
					else{
						System.err.println("File header "+fileTitle +" is not correct in file "+fileDir);
						break;
					}
					firstLine = false;
				}
							
				if (isTheFile) {
					file.add(line);
				}
				
			}
			reader.close();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	
		return file;
	}	
}
