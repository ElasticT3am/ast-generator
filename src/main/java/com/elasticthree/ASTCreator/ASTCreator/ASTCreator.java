package com.elasticthree.ASTCreator.ASTCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.elasticthree.ASTCreator.ASTCreator.Helpers.RecursivelyProjectJavaFiles;
import com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver.Neo4JDriver;
import com.elasticthree.ASTCreator.ASTCreator.Objects.FileNodeAST;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class ASTCreator {

	static final Logger stdoutLog = Logger.getLogger(ASTCreator.class);
	static final Logger debugLog = Logger.getLogger("debugLogger");
	
	
	public ASTCreator() {
	}

	/**
	 * We use CompilationUnit to parse the File
	 * 
	 * @param path_to_class
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public CompilationUnit getClassCompilationUnit(String path_to_class){
		// creates an input stream for the file to be parsed
		FileInputStream in = null;
		CompilationUnit cu = null;
		try {
			in = new FileInputStream(path_to_class);
		} catch (FileNotFoundException e) {
			debugLog.debug("IO Error skip project " + path_to_class + " from AST - Graph procedure");
			return cu;
		}
		try {
			cu = JavaParser.parse(in);
		} catch (Exception e1) {
			debugLog.debug("Parsing Error skip project " + path_to_class + " from AST - Graph procedure");
		}
		try {
			in.close();
		} catch (IOException e) {
			debugLog.debug("IO Error skip project " + path_to_class + " from AST - Graph procedure");
		}
		return cu;
	}

	public FileNodeAST getASTStats(String path_to_class){
		
		FileNodeAST fileObject = null;
		CompilationUnit cu;
		cu = getClassCompilationUnit(path_to_class);
		if (cu != null){
			ClassMethodDeclarationAST ast = new ClassMethodDeclarationAST(cu,
					path_to_class);
			ast.getTypeDeclarationFile();
			String packageName = "";
			try {
				packageName = cu.getPackage().getName().toString();
			} catch (NullPointerException n_e) {
				packageName = "No_package";
			}
			fileObject = new FileNodeAST(path_to_class, packageName,
					ast.getClassVisitor().getNumberOfClasses(), ast
							.getClassVisitor().getNumberOfInterfaces());
			fileObject.setClasses(ast.getClassVisitor().getClasses());
			fileObject.setInterfaces(ast.getClassVisitor().getInterfaces());
		}
		return fileObject;
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("resources/log4j.properties");
		Map<String, Long> classes = RecursivelyProjectJavaFiles
				.getProjectJavaFiles(args[0]);
		ASTCreator ast = new ASTCreator();
		Neo4JDriver neo4j = new Neo4JDriver();
		for(String clazz: classes.keySet()) {
			stdoutLog.info("-> Java File: " + clazz );
			neo4j.insertNeo4JDB(ast.getASTStats(clazz));
		}
		neo4j.closeDriverSession();

	}

}
