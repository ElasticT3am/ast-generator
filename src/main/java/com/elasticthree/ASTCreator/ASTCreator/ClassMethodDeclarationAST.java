package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger; 
import com.github.javaparser.ast.CompilationUnit;

public class ClassMethodDeclarationAST {
	
	final static Logger logger = Logger.getLogger(ClassMethodDeclarationAST.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	
	private CompilationUnit cu;
	private ClassVisitor classVisitor;

	public ClassMethodDeclarationAST(CompilationUnit cu, String repoURL, String pathClass){
		setCu(cu);
		String packageName = "";
		try{
			packageName = cu.getPackage().getName().toString();
		}
		catch(NullPointerException n_e){
			packageName = "No_package";
		}
		finally{
			setClassVisitor(new ClassVisitor(repoURL,packageName));
		}
	}
	
	public void getTypeDeclarationFile(){
		getClassOrInterface();
	}
	
	private void getClassOrInterface() {
		try{
			classVisitor.visit(getCu(), null);
		}catch (Exception e){
			logger.debug("Error: ",e);
			debugLog.debug("Error: ",e);	
		}
	}
	
	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}


	public ClassVisitor getClassVisitor() {
		return classVisitor;
	}

	public void setClassVisitor(ClassVisitor classVisitor) {
		this.classVisitor = classVisitor;
	}

}
