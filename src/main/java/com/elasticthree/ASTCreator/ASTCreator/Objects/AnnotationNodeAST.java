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
	
	@Override
	public String toString(){
		String to_string = "[ \'AnnotationNodeAST - Name: " + name
				+ "\']";
		
		return to_string;
	}
	
}
