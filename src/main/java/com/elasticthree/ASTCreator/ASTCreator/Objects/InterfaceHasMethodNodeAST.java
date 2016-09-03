package com.elasticthree.ASTCreator.ASTCreator.Objects;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class InterfaceHasMethodNodeAST {
	private String id;
	private String name;
	private String packageName;
	private String returningType;
	private boolean hasFinalModifier;
	private boolean hasAbstractModifier;
	private boolean hasPrivateModifier;
	private boolean hasPublicModifier;
	private boolean hasProtectedModifier;
	private boolean hasStaticModifier;
	private boolean hasSynchronizeModifier;
	private List<AnnotationNodeAST> annotatios;
	private List<CommentsNodeAST> comments;
	private List<ParameterMethodNodeAST> parameters;
	private List<ThrowMethodNodeAST> throwsMethod;

	
	public InterfaceHasMethodNodeAST(String name, String packageName) {
		this.setId(packageName + "-MethodNodeFromInterface-" + name);
		this.name = name;
		this.packageName = packageName;
		this.returningType = "";
		hasFinalModifier = false;
		hasAbstractModifier = false;
		hasPrivateModifier = false;
		hasPublicModifier = false;
		hasProtectedModifier = false;
		hasStaticModifier = false;
		hasSynchronizeModifier = false;
		setAnnotatios(new ArrayList<AnnotationNodeAST>());
		setComments(new ArrayList<CommentsNodeAST>());
		setParameters(new ArrayList<ParameterMethodNodeAST>());
		setThrowsMethod(new ArrayList<ThrowMethodNodeAST>());
	}

	public void setAllModifiers(int mod){
		if (Modifier.isFinal(mod)) {
			hasFinalModifier = true;
		}
		if (Modifier.isAbstract(mod)){
			hasAbstractModifier = true;
		}
		if (Modifier.isPrivate(mod)){
			hasPrivateModifier = true;
		}
		if (Modifier.isPublic(mod)){
			hasPublicModifier = true;
		}
		if (Modifier.isProtected(mod)){
			hasProtectedModifier = true;
		}
		if (Modifier.isStatic(mod)){
			hasStaticModifier = true;
		}
		if (Modifier.isSynchronized(mod)){
			hasStaticModifier = true;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isHasFinalModifier() {
		return hasFinalModifier;
	}

	public void setHasFinalModifier(boolean hasFinalModifier) {
		this.hasFinalModifier = hasFinalModifier;
	}

	public boolean isHasAbstractModifier() {
		return hasAbstractModifier;
	}

	public void setHasAbstractModifier(boolean hasAbstractModifier) {
		this.hasAbstractModifier = hasAbstractModifier;
	}

	public boolean isHasPrivateModifier() {
		return hasPrivateModifier;
	}

	public void setHasPrivateModifier(boolean hasPrivateModifier) {
		this.hasPrivateModifier = hasPrivateModifier;
	}

	public boolean isHasPublicModifier() {
		return hasPublicModifier;
	}

	public void setHasPublicModifier(boolean hasPublicModifier) {
		this.hasPublicModifier = hasPublicModifier;
	}

	public boolean isHasProtectedModifier() {
		return hasProtectedModifier;
	}

	public void setHasProtectedModifier(boolean hasProtectedModifier) {
		this.hasProtectedModifier = hasProtectedModifier;
	}

	public boolean isHasStaticModifier() {
		return hasStaticModifier;
	}

	public void setHasStaticModifier(boolean hasStaticModifier) {
		this.hasStaticModifier = hasStaticModifier;
	}

	public boolean isHasSynchronizeModifier() {
		return hasSynchronizeModifier;
	}

	public void setHasSynchronizeModifier(boolean hasSynchronizeModifier) {
		this.hasSynchronizeModifier = hasSynchronizeModifier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReturningType() {
		return returningType;
	}

	public void setReturningType(String returningType) {
		this.returningType = returningType;
	}

	public List<AnnotationNodeAST> getAnnotatios() {
		return annotatios;
	}

	public void setAnnotatios(List<AnnotationNodeAST> annotatios) {
		this.annotatios = annotatios;
	}

	public List<CommentsNodeAST> getComments() {
		return comments;
	}

	public void setComments(List<CommentsNodeAST> comments) {
		this.comments = comments;
	}

	public List<ParameterMethodNodeAST> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterMethodNodeAST> parameters) {
		this.parameters = parameters;
	}

	public List<ThrowMethodNodeAST> getThrowsMethod() {
		return throwsMethod;
	}

	public void setThrowsMethod(List<ThrowMethodNodeAST> throwsMethod) {
		this.throwsMethod = throwsMethod;
	}
	
	
	@Override
	public String toString(){
		String to_string = " METHOD_INTERFACE -> [ \'ID: " + id + "\', \'Package : " 
				+ packageName + "\', \'Name: " + name  
				+ "\', \'ReturningType: " + returningType
				+ "\', \'HasFinalModifier : " + hasFinalModifier
				+ "\', \'HasAbstractModifier : " + hasAbstractModifier
				+ "\', \'HasPrivateModifier : " + hasPrivateModifier
				+ "\', \'HasPublicModifier : " + hasPublicModifier
				+ "\', \'HasProtectedModifier : " + hasProtectedModifier
				+ "\', \'HasStaticModifier : " + hasStaticModifier
				+ "\', \'HasSynchronizeModifier : " + hasSynchronizeModifier
				+ "\']";
		if (annotatios.size() != 0 )
			for(int i=0; i<annotatios.size(); i++)
				to_string += "\n" + annotatios.get(i).toString();
		
		if (comments.size() != 0 )
			for(int i=0; i<comments.size(); i++)
				to_string += "\n" + comments.get(i).toString();
		
		if (parameters.size() != 0 )
			for(int i=0; i<parameters.size(); i++)
				to_string += "\n" + parameters.get(i).toString();
		
		if (throwsMethod.size() != 0 )
			for(int i=0; i<throwsMethod.size(); i++)
				to_string += "\n" + throwsMethod.get(i).toString();
		
		return to_string;
	}
}
