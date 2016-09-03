package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger; 
import com.github.javaparser.ast.CompilationUnit;

public class ClassMethodDeclarationAST {
	final static Logger logger = Logger.getLogger(ClassMethodDeclarationAST.class);
	private CompilationUnit cu;
	private String pathFile;
	private ClassVisitor classVisitor;

	public ClassMethodDeclarationAST(CompilationUnit cu, String pathClass){
		this.cu = cu;
		this.setPathFile(pathClass);
		String packageName = "";
		try{
			packageName = cu.getPackage().getName().toString();
		}
		catch(NullPointerException n_e){
			logger.debug("Propably no package");
			packageName = "No_package";
		}
		finally{
			this.classVisitor = new ClassVisitor(packageName);
		}
	}
	
	public void getTypeDeclarationFile(){
		getClassOrInterface();
	}
	
	private void getClassOrInterface() {
		try{
			classVisitor.visit(getCu(), null);
		}catch (Exception e){
       	 logger.error("Error: ",e);
		}
	}
	
	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public String getPathClass() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	
	public ClassVisitor getClassVisitor() {
		return classVisitor;
	}


}
