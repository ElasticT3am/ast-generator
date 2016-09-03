package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class ThrowMethodNodeAST {
	
	private String name;
	
	public ThrowMethodNodeAST(String name){
		
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
		String to_string = "[ \'ThrowMethodNodeAST - Name: " + name
				+ "\']";
		
		return to_string;
	}
}
