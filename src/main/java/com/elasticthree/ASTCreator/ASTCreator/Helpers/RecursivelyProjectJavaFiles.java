package com.elasticthree.ASTCreator.ASTCreator.Helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RecursivelyProjectJavaFiles {
	
	final static Logger logger = Logger.getLogger(RecursivelyProjectJavaFiles.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	
	public static List<String> getProjectJavaFiles(String path){
		
		List<String> allFiles = new ArrayList<String>();
		
		// Java8 lambda expression manner getting recursively all files
		try {
			Files.walk(new File(path).toPath())
			// With this filter, we avoid the Test Folder / classes
			.filter(file -> {
				 if(Files.isRegularFile(file)) {
					 if (file.getParent().toString().contains("test")){
		                return false;
					 }
		             else
		            	return true;
		                	
				 }
				 else
					 return true;
			 })
			.filter(p -> p.toFile().getName().endsWith(".java"))
			.forEach(tmpnam -> {
				allFiles.add(tmpnam.toString() /*, Files.lines(tmpnam).count()*/);
				// I don't know if the following command is necessary
				// I comment out
				//Files.lines(tmpnam);
			});
			
		} catch (IOException e) {
			logger.error("IO Exception",e);
			debugLog.error("IO Exception",e);
		}
		return allFiles;
	}
	
	
	
}
