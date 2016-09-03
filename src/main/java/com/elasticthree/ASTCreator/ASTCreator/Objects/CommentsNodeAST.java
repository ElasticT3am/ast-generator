package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class CommentsNodeAST {

	private String name;

	public CommentsNodeAST(String name) {
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
		String to_string = "[ \'CommentsNodeAST - Name: " + name
				+ "\']";
		
		return to_string;
	}
}
