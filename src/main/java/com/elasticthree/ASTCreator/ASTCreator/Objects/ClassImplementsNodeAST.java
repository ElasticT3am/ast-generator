package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class ClassImplementsNodeAST {

	private String name;

	public ClassImplementsNodeAST(String name) {
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
		String to_string = "[ \' ImplementsNodeAST - Name: " + name
				+ "\']";
		
		return to_string;
	}
}
