package com.elasticthree.ASTCreator.ASTCreator;

import java.lang.reflect.Modifier;
import org.apache.log4j.Logger;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;

public class MethodParser{
	
	final static Logger logger = Logger.getLogger(ASTCreator.class);
	private MethodDeclaration method;
	
	public MethodParser(MethodDeclaration method){
		this.method = method;
	}
	
	
	public void methodObjectParser(){
//		logger.info("Method name: " + method.getName());
//		logger.info("Method returning type : " + method.getType());
		for (TypeParameter parameter: method.getTypeParameters()){
//			logger.info("Method parameter names: " + parameter.getName());
//			logger.info("Method parameter type: " + parameter.getClass());
		}
		for (Comment comment: method.getAllContainedComments())
//			logger.info("Method comment: " + comment.getContent());
		for (AnnotationExpr ann:  method.getAnnotations()){
//			logger.info("Method Annotations: " + ann.toString());
		}
//		printAllModifiers(method.getModifiers());
		
	}
	
	public static void printAllModifiers(int mod){
		if (Modifier.isFinal(mod)) {
			logger.info("Method has final modifier");
		}
		if (Modifier.isAbstract(mod)){
			logger.info("Method has abstract modifier");
		}
		if (Modifier.isInterface(mod)){
			logger.info("Is an interface");
		}
		if (Modifier.isPrivate(mod)){
			logger.info("Method has private modifier");
		}
		if (Modifier.isPublic(mod)){
			logger.info("Method has public modifier");
		}
		if (Modifier.isProtected(mod)){
			logger.info("Method has protected modifier");
		}
		if (Modifier.isStatic(mod)){
			logger.info("Method has static modifier");
		}
		if (Modifier.isSynchronized(mod)){
			logger.info("Method has synchronized modifier");
		}
	}
	
	public MethodDeclaration getMethod() {
		return method;
	}

	public void setMethod(MethodDeclaration method) {
		this.method = method;
	}
	
	

}
