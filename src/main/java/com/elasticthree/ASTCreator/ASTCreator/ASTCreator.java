package com.elasticthree.ASTCreator.ASTCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

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

	private String repoURL;
	private long javaLinesOfCode;
	private Neo4JDriver neo4j;

	public ASTCreator(String repoURL) {
		setRepoURL(repoURL);
		setJavaLinesOfCode(0);
		neo4j = new Neo4JDriver();
	}

	/**
	 * We use CompilationUnit (from Javaparser project) to parse the File
	 * 
	 * @param path_to_class
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public CompilationUnit getClassCompilationUnit(String path_to_class) {
		// creates an input stream for the file to be parsed
		FileInputStream in = null;
		CompilationUnit cu = null;
		try {
			in = new FileInputStream(path_to_class);
		} catch (FileNotFoundException e) {
			debugLog.debug("IO Error skip project " + path_to_class
					+ " from AST - Graph procedure");
			return cu;
		}
		try {
			cu = JavaParser.parse(in);
		} catch (Exception e1) {
			debugLog.debug("Parsing Error skip project " + path_to_class
					+ " from AST - Graph procedure");
		}
		try {
			in.close();
		} catch (IOException e) {
			debugLog.debug("IO Error skip project " + path_to_class
					+ " from AST - Graph procedure");
		}
		return cu;
	}

	/**
	 * This function creates AST (Abstract syntax tree) for a Java file
	 * 
	 * @param path_to_class
	 * @return
	 */
	public FileNodeAST getASTFileObject(String path_to_class) {

		FileNodeAST fileObject = null;
		CompilationUnit cu;
		cu = getClassCompilationUnit(path_to_class);
		if (cu != null) {
			ClassMethodDeclarationAST ast = new ClassMethodDeclarationAST(cu,
					getRepoURL(), path_to_class);
			ast.getTypeDeclarationFile();
			String packageName = "";
			try {
				packageName = cu.getPackage().getName().toString();
			} catch (NullPointerException n_e) {
				packageName = "No_package";
			}
			fileObject = new FileNodeAST(getRepoURL(), path_to_class,
					packageName, ast.getClassVisitor().getNumberOfClasses(),
					ast.getClassVisitor().getNumberOfInterfaces());
			fileObject.setClasses(ast.getClassVisitor().getClasses());
			fileObject.setInterfaces(ast.getClassVisitor().getInterfaces());
		}
		return fileObject;
	}

	/**
	 * This function runs the AST and inserts it in Neo4j instance for all Java
	 * files of a Java Project
	 * 
	 * @param classes
	 */
	public void repoASTProcedure(List<String> classes) {
		classes.forEach(file -> {
			stdoutLog.info("-> Java File: " + file);
			FileNodeAST fileNode = getASTFileObject(file);
			if (fileNode != null) {
				neo4j.insertNeo4JDB(fileNode);
				Stream<String> lines = null;
				try {
					lines = Files.lines(Paths.get(file));
					addJavaLinesOfCode(lines.count());
				} catch (Exception e) {
					stdoutLog.debug("Error", e);
					debugLog.debug("Error", e);
				}
				finally {
					if (lines != null)
						lines.close();
				}
			}
		});
		neo4j.insertRepoNodeNeo4JDB(getRepoURL(), getJavaLinesOfCode());
		neo4j.closeDriverSession();
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("resources/log4j.properties");
		// args[0] -> Path to Java Project
		List<String> classes = RecursivelyProjectJavaFiles
				.getProjectJavaFiles(args[0]);
		// args[1] -> URL of Java Project
		ASTCreator ast = new ASTCreator(args[1]);
		ast.repoASTProcedure(classes);
	}

	public Neo4JDriver getNeo4j() {
		return neo4j;
	}

	public void setNeo4j(Neo4JDriver neo4j) {
		this.neo4j = neo4j;
	}

	public String getRepoURL() {
		return repoURL;
	}

	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}

	public long getJavaLinesOfCode() {
		return javaLinesOfCode;
	}

	public void setJavaLinesOfCode(long javaLinesOfCode) {
		this.javaLinesOfCode = javaLinesOfCode;
	}

	public void addJavaLinesOfCode(long javaLinesOfCode) {
		this.javaLinesOfCode += javaLinesOfCode;
	}

}
