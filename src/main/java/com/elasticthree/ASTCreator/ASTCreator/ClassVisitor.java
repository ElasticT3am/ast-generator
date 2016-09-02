package com.elasticthree.ASTCreator.ASTCreator;

import org.apache.log4j.Logger;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVisitor extends VoidVisitorAdapter<Object> {
	final static Logger logger = Logger.getLogger(ASTCreator.class);

	public ClassVisitor() {
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		super.visit(n, arg);
		parsing(n);
	}
	
	private void parsing(ClassOrInterfaceDeclaration n){
		// It's a Class
				if (!n.isInterface()) {
					logger.info("Class name : " + n.getName());
					for (ClassOrInterfaceType ext : n.getExtends()) {
						logger.info("Extends class: " + ext.getName());
					}
					for (AnnotationExpr ann : n.getAnnotations()) {
						logger.info("Annotation class: " + ann.toString());
					}
					for (Comment comment : n.getAllContainedComments()) {
						logger.info("Comment class:" + comment.toString());
					}
					logger.info("Class line: " + n.getBegin().line + " , column: "
							+ n.getBegin().column);
					logger.info("Class begin range: " + n.getRange().begin
							+ " , end range: " + n.getRange().end);

					// Method parser
					for (BodyDeclaration member : n.getMembers()) {
						if (member instanceof MethodDeclaration) {
							MethodDeclaration method = (MethodDeclaration) member;
							MethodParser mparser = new MethodParser(method);
							mparser.methodObjectParser();
						}
					}
				}
				// It's an Interface
				else {
					logger.info("Interface name : " + n.getName());
					for (ClassOrInterfaceType ext : n.getExtends()) {
						logger.info("Extends class: " + ext.getName());
					}
					for (AnnotationExpr ann : n.getAnnotations()) {
						logger.info("Annotation class: " + ann.toString());
					}
					for (Comment comment : n.getAllContainedComments()) {
						logger.info("Comment class:" + comment.toString());
					}
					logger.info("Class line: " + n.getBegin().line + " , column: "
							+ n.getBegin().column);
					logger.info("Class begin range: " + n.getRange().begin
							+ " , end range: " + n.getRange().end);

					// Method parser
					for (BodyDeclaration member : n.getMembers()) {
						if (member instanceof MethodDeclaration) {
							MethodDeclaration method = (MethodDeclaration) member;
							MethodParser mparser = new MethodParser(method);
							mparser.methodObjectParser();
						}
					}
				}
	}

}
