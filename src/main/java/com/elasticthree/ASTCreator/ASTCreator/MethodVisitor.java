package com.elasticthree.ASTCreator.ASTCreator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@SuppressWarnings("hiding")
public class MethodVisitor<Object> extends VoidVisitorAdapter<Object>{
	 @Override
     public void visit(MethodDeclaration n, Object arg) {
         // here you can access the attributes of the method.
         // this method will be called for all methods in this 
         // CompilationUnit, including inner class methods
         System.out.println(n.getName());
         super.visit(n, arg);
     }
}
