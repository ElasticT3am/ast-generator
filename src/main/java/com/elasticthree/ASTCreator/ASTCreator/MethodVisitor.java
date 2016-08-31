package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Object>{
	
	final static Logger logger = Logger.getLogger(ASTCreator.class);
	
	 @Override
     public void visit(MethodDeclaration n, Object arg) {
			logger.info("Method name: " + n.getName());
         super.visit(n, arg);
     }
}
