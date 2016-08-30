package com.elasticthree.ASTCreator.ASTCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.elasticthree.ASTCreator.ASTCreator.Helpers.RecursivelyProjectJavaFiles;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class ASTCreator {


	public ASTCreator(){
	}
	
	
	public void printCompilationUnit(String path_to_class){
		
		// creates an input stream for the file to be parsed
        FileInputStream in = null;
		try {
			in = new FileInputStream(path_to_class);
		} catch (FileNotFoundException e) {
			// TODO Add Logs
			e.printStackTrace();
		}

        CompilationUnit cu = null;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } catch (ParseException e) {
			// TODO You must add logger :(
			e.printStackTrace();
		} finally {
            try {
				in.close();
			} catch (IOException e) {
				// TODO You must add logger :(
				e.printStackTrace();
			}
        }

//        // prints the resulting compilation unit to default system output
//        System.out.println(cu.toString());
        
        // print the methods names
        getNamesClass(cu);
    }


	private void getNamesClass(CompilationUnit cu) {
		new MethodVisitor<Object>().visit(cu, null);
	}	
	
	public static void main( String[] args ){
		
		List<String> classes = RecursivelyProjectJavaFiles.getProjectJavaFiles(args[0]);
		classes.stream().forEach((string) -> {
		});
		
		ASTCreator ast = new ASTCreator();
		classes.forEach(file -> {
			ast.printCompilationUnit(file);	
		});
		
    }
	
}
