package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class ParameterMethodNodeAST {

	private String type;
	private String name;
	
	public ParameterMethodNodeAST(String type, String name){
		this.setType(type);
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
	
	@Override
	public String toString(){
		String to_string = "[ \'ParameterMethodNodeAST - Type: " + type.toString()
				+ ", Name: " + name.toString() + "\']";
		
		return to_string;
	}
}
