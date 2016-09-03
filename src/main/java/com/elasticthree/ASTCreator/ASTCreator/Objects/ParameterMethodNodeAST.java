package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class ParameterMethodNodeAST {

	private String type;
	private String name;
	
	public ParameterMethodNodeAST(String type, String name){
		this.setType(name);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
