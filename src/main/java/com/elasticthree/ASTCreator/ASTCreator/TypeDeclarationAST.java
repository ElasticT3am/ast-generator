package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger; 
import com.github.javaparser.ast.CompilationUnit;

public class TypeDeclarationAST {
	final static Logger logger = Logger.getLogger(TypeDeclarationAST.class);
	private CompilationUnit cu = null;

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}
	
	public TypeDeclarationAST(){}
	
	
	public TypeDeclarationAST(CompilationUnit cu){
		this.cu = cu;
	}
	
	public void getTypeDeclarationFile(){
		getClassOrInterface(cu);
		getFileMethods(cu);
	}
	
	private void getClassOrInterface(CompilationUnit cu) {
		try{
			logger.info("Package name : " + cu.getPackage().getName().toString());
			new ClassVisitor().visit(cu, null);
		}catch (Exception e){
       	 logger.error("Error: ",e);
		}
	}
	
	private void getFileMethods(CompilationUnit cu) {
		try{
			new MethodVisitor().visit(cu, null);
		}catch (Exception e){
			logger.error("Error: ",e);
		}
	}
}
