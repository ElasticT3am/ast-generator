package com.elasticthree.ASTCreator.ASTCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.elasticthree.ASTCreator.ASTCreator.Helpers.RecursivelyProjectJavaFiles;
import com.elasticthree.ASTCreator.ASTCreator.Objects.FileObject;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class ASTCreator {

	final static Logger logger = Logger.getLogger(ASTCreator.class);

	public ASTCreator() {
	}

	/**
	 * We use CompilationUnit to parse the File
	 * 
	 * @param path_to_class
	 * @return
	 */
	public CompilationUnit getClassCompilationUnit(String path_to_class) {
		// creates an input stream for the file to be parsed
		FileInputStream in = null;
		try {
			in = new FileInputStream(path_to_class);
		} catch (FileNotFoundException e) {
			logger.error("File not found : ", e);
		}

		CompilationUnit cu = null;
		try {
			// parse the file
			cu = JavaParser.parse(in);
		} catch (ParseException e) {
			logger.error("Parse Exception error : ", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("IOException : ", e);
			}
		}

		return cu;
	}

	public void getASTStats(String path_to_class) {

		CompilationUnit cu = this.getClassCompilationUnit(path_to_class);
		// Print Type Declarations
		ClassMethodDeclarationAST ast = new ClassMethodDeclarationAST(cu,
				path_to_class);
		ast.getTypeDeclarationFile();
		FileObject fileObbject = new FileObject(path_to_class, cu.getPackage()
				.getName().toString(), ast.getClassVisitor()
				.getNumberOfClasses(), ast.getClassVisitor()
				.getNumberOfInterfaces(), ast.getMethodVisitor()
				.getNumberOfMethods());
		logger.info(fileObbject.toString());

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/main/java/log4j.properties");
		List<String> classes = RecursivelyProjectJavaFiles
				.getProjectJavaFiles(args[0]);
		ASTCreator ast = new ASTCreator();
		classes.forEach(file -> {
			logger.info("##################### Java file: " + file
					+ " #####################");
			ast.getASTStats(file);
		});

	}

}
