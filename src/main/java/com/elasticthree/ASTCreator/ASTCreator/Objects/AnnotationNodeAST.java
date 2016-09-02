package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class AnnotationNodeAST {

	private String name;
	
	public AnnotationNodeAST(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
