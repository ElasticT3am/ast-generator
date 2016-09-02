package com.elasticthree.ASTCreator.ASTCreator.Objects;

public class FileObject {

	private String id;
	private String packageName;
	private String name;
	private long numberOfClasses;
	private long numberOfInterfaces;
	private long numberOfMethods;

	public FileObject(String absPathToFile, String packageName,
			long numberOfClasses, long numberOfInterfaces, long numberOfMethods) {
		this.id = absPathToFile;
		this.name = absPathToFile.substring(absPathToFile.lastIndexOf("/") + 1);
		this.packageName = packageName;
		setNumberOfClasses(numberOfClasses);
		setNumberOfInterfaces(numberOfInterfaces);
		setNumberOfMethods(numberOfMethods);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public long getNumberOfMethods() {
		return numberOfMethods;
	}

	public void setNumberOfMethods(long numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}
	
	@Override
	public String toString(){
		return "[ \'ID: " + id + "\', \'Package : " + packageName + "\', \'Name: " + name 
				+ "\', \'NumberOfClasses: " + numberOfClasses + "\', \'NumberOfInterfaces : " 
				+ numberOfInterfaces + "\', \'NUmberOfMethods: " + numberOfMethods + "\']";
	}
}
