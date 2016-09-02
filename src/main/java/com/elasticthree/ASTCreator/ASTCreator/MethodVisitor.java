package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Object> {

	final static Logger logger = Logger.getLogger(ASTCreator.class);

	@Override
	public void visit(MethodDeclaration n, Object arg) {

		super.visit(n, arg);
		logger.info("--------Method Name: " + n.getName() + "--------");
		parsing(n);
	}

	private void parsing(MethodDeclaration method) {
		for (Parameter param : method.getParameters()) {
			logger.info("Method Type parameter: " + param.getType());
			logger.info("Method Name parameter: " + param.getName());
		}
		for (AnnotationExpr ann : method.getAnnotations()) {
			logger.info("Method Annotation: " + ann.toString());
		}
		for (Comment comment : method.getAllContainedComments()){
			logger.info("Method Comment: " + comment.toString());
		}
		
		logger.info("Method Declaration: " + method.getDeclarationAsString());
		for (ReferenceType reftype : method.getThrows()){
			logger.info("Method Comment: " + reftype.toString());
		}
		
		logger.info("Method Array Count: " + method.getArrayCount());
		
		if (method.getJavaDoc() != null)
		logger.info("Method javadoc: " + method.getJavaDoc().toString());
	
		logger.info("Is deafult ? : " + method.isDefault());
		logger.info("Returning type : " + method.getType());
		
		MethodParser.printAllModifiers(method.getModifiers());
		
		if (method.getBody() != null)
		for (Statement stmt : method.getBody().getStmts()){
			logger.info("Stmt toString : " + stmt.toString());
		}
		
	}
}
