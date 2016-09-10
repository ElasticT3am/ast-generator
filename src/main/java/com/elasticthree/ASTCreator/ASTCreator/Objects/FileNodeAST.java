package com.elasticthree.ASTCreator.ASTCreator.Objects;

import java.util.ArrayList;
import java.util.List;

public class FileNodeAST {

	private String packageName;
	private String name;
	private String repoURL;
	private long numberOfClasses;
	private long numberOfInterfaces;
	private List<ClassNodeAST> classes;
	private List<InterfaceNodeAST> interfaces;

	public FileNodeAST(String repoURL, String absPathToFile,
			String packageName, long numberOfClasses, long numberOfInterfaces) {
		setRepoURL(repoURL);
		setName(absPathToFile.substring(absPathToFile.lastIndexOf("/") + 1));
		setPackageName(packageName);
		setNumberOfClasses(numberOfClasses);
		setNumberOfInterfaces(numberOfInterfaces);
		setClasses(new ArrayList<ClassNodeAST>());
		setInterfaces(new ArrayList<InterfaceNodeAST>());
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(long numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	public long getNumberOfInterfaces() {
		return numberOfInterfaces;
	}

	public void setNumberOfInterfaces(long numberOfInterfaces) {
		this.numberOfInterfaces = numberOfInterfaces;
	}

	public List<ClassNodeAST> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassNodeAST> classes) {
		this.classes = classes;
	}

	public List<InterfaceNodeAST> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceNodeAST> interfaces) {
		this.interfaces = interfaces;
	}

	@Override
	public String toString() {
		String to_string = " - root level - [ \'URL: " + repoURL + "\', \'Package : "
				+ packageName + "\', \'Name: " + name
				+ "\', \'NumberOfClasses: " + numberOfClasses
				+ "\', \'NumberOfInterfaces : " + numberOfInterfaces + "\']";
		if (classes.size() == 0)
			to_string += "\nNo classes on this File";

		else {
			for (int i = 0; i < classes.size(); i++)
				to_string += "\n CLASS -> " + classes.get(i).toString();
		}

		if (interfaces.size() == 0)
			to_string += "\nNo interfaces on this File";

		else {
			for (int i = 0; i < interfaces.size(); i++)
				to_string += "\n INTERFACE -> " + interfaces.get(i).toString();
		}

		return to_string;
	}

	public String getRepoURL() {
		return repoURL;
	}

	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}
}
