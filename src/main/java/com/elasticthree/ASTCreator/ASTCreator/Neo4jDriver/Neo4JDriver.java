package com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver;

import com.elasticthree.ASTCreator.ASTCreator.Objects.*;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Neo4JDriver {

	final static Logger logger = Logger.getLogger(Neo4JDriver.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	final static Logger resultLog = Logger.getLogger("reportsLogger");
	
	private String host;
	private String usern;
	private String password;
	private Driver driver;
	private Session session;

	/**
	 * Neo4JDriver creates and inserts
	 * the query to Neo4j instance
	 */
	public Neo4JDriver() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("resources/config.properties");
			prop.load(input);
			this.host = prop.getProperty("host");
			this.usern = prop.getProperty("neo4j_username");
			this.password = prop.getProperty("neo4j_password");
		} catch (IOException ex) {
			logger.debug("IOException: ", ex);
			debugLog.debug("IOException: ", ex);
			host = null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			driver = GraphDatabase.driver("bolt://" + this.host,
					AuthTokens.basic(this.usern, this.password));
			session = driver.session();
		} catch (Exception e) {
			driver = null;
			session = null;
		}

	}
	
	/**
	 * Inserting AST to Neo4J instance through log file
	 * @param query
	 */
	public void insertNeo4JDBLogFile(String query){
		
		if (isNeo4jConnectionUp() ) {
			
			try{
				// Insert query on Neo4j graph DB
				session.run(query);
				logger.info("Insertion Query: " + query);

			} catch (Exception e) {
				logger.debug("Excetion : ", e);
				debugLog.debug("Excetion : ", e);
				return;
			}
		} else{
			logger.debug("Driver or Session is down, check the configuration");
			debugLog.debug("Driver or Session is down, check the configuration");
		}
		
	}

	/**
	 * Inserting AST to Neo4J instance
	 * 
	 * @param fileNodeAST
	 *            (AST root node)
	 */
	public void insertNeo4JDB(FileNodeAST fileNodeAST) {

		if (fileNodeAST == null){
			logger.debug("AST File Object is null (Probably had parsing error)");
			debugLog.debug("AST File Object is null (Probably had parsing error)");
			return;
		}
		
		
		if (isNeo4jConnectionUp() ) {
			try {
				// File Node of AST
				String fileNodeInsertQuery = "CREATE (";
				fileNodeInsertQuery += "f:File {";
				// File node properties
				fileNodeInsertQuery += "package:\'"
						+ fileNodeAST.getPackageName() + "\',";
				fileNodeInsertQuery += "name:\'" + fileNodeAST.getName()
						+ "\',";
				fileNodeInsertQuery += "NumberOfClasses:\'"
						+ String.valueOf(fileNodeAST.getNumberOfClasses())
						+ "\',";
				fileNodeInsertQuery += "NumberOfInterfaces:\'"
						+ String.valueOf(fileNodeAST.getNumberOfInterfaces())
						+ "\'";
				fileNodeInsertQuery += "})";

				// List of Classes
				if (fileNodeAST.getNumberOfClasses() > 0) {
					for (int i = 0; i < fileNodeAST.getClasses().size(); i++) {
						ClassNodeAST classNode = fileNodeAST.getClasses()
								.get(i);
						fileNodeInsertQuery += ",(";
						fileNodeInsertQuery += "class" + classNode.getName()
								+ ":Class {";
						// Class node properties
						if (classNode.isHasFinalModifier())
							fileNodeInsertQuery += "HasFinalModifier:\'"
									+ String.valueOf(classNode
											.isHasFinalModifier()) + "\',";
						if (classNode.isHasAbstractModifier())
							fileNodeInsertQuery += "HasAbstractModifier:\'"
									+ String.valueOf(classNode
											.isHasAbstractModifier()) + "\',";
						if (classNode.isHasPrivateModifier())
							fileNodeInsertQuery += "HasPrivateModifier:\'"
									+ String.valueOf(classNode
											.isHasPrivateModifier()) + "\',";
						if (classNode.isHasPublicModifier())
							fileNodeInsertQuery += "HasPublicModifier:\'"
									+ String.valueOf(classNode
											.isHasPublicModifier()) + "\',";
						if (classNode.isHasProtectedModifier())
							fileNodeInsertQuery += "HasProtectedModifier:\'"
									+ String.valueOf(classNode
											.isHasProtectedModifier()) + "\',";
						if (classNode.isHasStaticModifier())
							fileNodeInsertQuery += "HasStaticModifier:\'"
									+ String.valueOf(classNode
											.isHasStaticModifier()) + "\',";
						if (classNode.isHasSynchronizeModifier())
							fileNodeInsertQuery += "HasSynchronizeModifier:\'"
									+ String.valueOf(classNode
											.isHasSynchronizeModifier())
									+ "\',";
						if (!classNode.getExtendsClass().equalsIgnoreCase(
								"None")) {
							fileNodeInsertQuery += "extends:\'"
									+ classNode.getExtendsClass() + "\',";
						}
						fileNodeInsertQuery += "NumberOfMethods:\'"
								+ String.valueOf(classNode.getNumberOfMethods())
								+ "\',";
						fileNodeInsertQuery += "package:\'"
								+ classNode.getPackageName() + "\',";
						fileNodeInsertQuery += "name:\'" + classNode.getName()
								+ "\'";
						fileNodeInsertQuery += "})";

						// Annotation Node
						if (classNode.getAnnotatios().size() > 0) {
							for (int j = 0; j < classNode.getAnnotatios()
									.size(); j++) {
								AnnotationNodeAST annotationNode = classNode
										.getAnnotatios().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += "class"
										+ classNode.getName() + "ann"
										+ String.valueOf(j) + ":Annotation {";
								// Annotation node property
								fileNodeInsertQuery += "name:\'"
										+ annotationNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP CLASS -> ANNOTATION
								fileNodeInsertQuery += ",(" + "class"
										+ classNode.getName() + ")";

								fileNodeInsertQuery += "-[:HAS_ANNOTATION]->";
								fileNodeInsertQuery += "(" + "class"
										+ classNode.getName() + "ann"
										+ String.valueOf(j) + ")";
							}
						}

						// Implements Interface Node
						if (classNode.getImpl().size() > 0) {
							for (int j = 0; j < classNode.getImpl().size(); j++) {
								ClassImplementsNodeAST implNode = classNode
										.getImpl().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += "class"
										+ classNode.getName() + "impl"
										+ String.valueOf(j)
										+ ":ImplementsInterface {";
								// Implements Interface node property
								fileNodeInsertQuery += "name:\'"
										+ implNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP CLASS -> IMPLEMENTS_INTERFACE
								fileNodeInsertQuery += ",(" + "class"
										+ classNode.getName() + ")";

								fileNodeInsertQuery += "-[:IMPLEMENTS_INTERFACE]->";
								fileNodeInsertQuery += "(" + "class"
										+ classNode.getName() + "impl"
										+ String.valueOf(j) + ")";
							}
						}

						// Method Node
						if (classNode.getMethod().size() > 0) {
							for (int j = 0; j < classNode.getMethod().size(); j++) {
								ClassHasMethodNodeAST methodNode = classNode
										.getMethod().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += "class"
										+ classNode.getName() + "method"
										+ String.valueOf(j) + ":Method {";

								if (methodNode.isHasFinalModifier())
									fileNodeInsertQuery += "HasFinalModifier:\'"
											+ String.valueOf(methodNode
													.isHasFinalModifier())
											+ "\',";
								if (methodNode.isHasAbstractModifier())
									fileNodeInsertQuery += "HasAbstractModifier:\'"
											+ String.valueOf(methodNode
													.isHasAbstractModifier())
											+ "\',";
								if (methodNode.isHasPrivateModifier())
									fileNodeInsertQuery += "HasPrivateModifier:\'"
											+ String.valueOf(methodNode
													.isHasPrivateModifier())
											+ "\',";
								if (methodNode.isHasPublicModifier())
									fileNodeInsertQuery += "HasPublicModifier:\'"
											+ String.valueOf(methodNode
													.isHasPublicModifier())
											+ "\',";
								if (methodNode.isHasProtectedModifier())
									fileNodeInsertQuery += "HasProtectedModifier:\'"
											+ String.valueOf(methodNode
													.isHasProtectedModifier())
											+ "\',";
								if (methodNode.isHasStaticModifier())
									fileNodeInsertQuery += "HasStaticModifier:\'"
											+ String.valueOf(methodNode
													.isHasStaticModifier())
											+ "\',";
								if (methodNode.isHasSynchronizeModifier())
									fileNodeInsertQuery += "HasSynchronizeModifier:\'"
											+ String.valueOf(methodNode
													.isHasSynchronizeModifier())
											+ "\',";

								fileNodeInsertQuery += "ReturningType:\'"
										+ methodNode.getReturningType() + "\',";
								fileNodeInsertQuery += "package:\'"
										+ methodNode.getPackageName() + "\',";
								fileNodeInsertQuery += "name:\'"
										+ methodNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// Method's RelationShips

								// Annotation Node
								if (methodNode.getAnnotatios().size() > 0) {
									for (int k = 0; k < methodNode
											.getAnnotatios().size(); k++) {
										AnnotationNodeAST annotationNode = methodNode
												.getAnnotatios().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "ann"
												+ String.valueOf(k)
												+ annotationNode.getName()
														.replace("@", "")
												+ ":Annotation {";
										// Annotation node property
										fileNodeInsertQuery += "name:\'"
												+ annotationNode.getName()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> ANNOTATION
										fileNodeInsertQuery += ",(" + "class"
												+ classNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_ANNOTATION]->";
										fileNodeInsertQuery += "("
												+ "method"
												+ methodNode.getName()
												+ "ann"
												+ String.valueOf(k)
												+ annotationNode.getName()
														.replace("@", "") + ")";
									}
								}


								// Parameter Node
								if (methodNode.getParameters().size() > 0) {
									for (int k = 0; k < methodNode
											.getParameters().size(); k++) {
										ParameterMethodNodeAST paramNode = methodNode
												.getParameters().get(k);
										String typeParam = paramNode.getType()
												.replace("[", "");
										typeParam = typeParam.replace("]", "");
										typeParam = typeParam.replace("(", "");
										typeParam = typeParam.replace(")", "");
										typeParam = typeParam.replace("<", "");
										typeParam = typeParam.replace(">", "");
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "param" + String.valueOf(k)
												+ paramNode.getName()
												+ typeParam + ":Parameter {";
										// Parameter node property
										fileNodeInsertQuery += "name:\'"
												+ paramNode.getName() + "\',";
										fileNodeInsertQuery += "type:\'"
												+ paramNode.getType() + "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> PARAMETER
										fileNodeInsertQuery += ",(" + "class"
												+ classNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_PARAMETER]->";
										fileNodeInsertQuery += "(" + "method"
												+ methodNode.getName()
												+ "param" + String.valueOf(k)
												+ paramNode.getName()
												+ typeParam + ")";
									}
								}

								// Throw Method Node
								if (methodNode.getThrowsMethod().size() > 0) {
									for (int k = 0; k < methodNode
											.getThrowsMethod().size(); k++) {
										ThrowMethodNodeAST throwNode = methodNode
												.getThrowsMethod().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "throw" + String.valueOf(k)
												+ throwNode.getName()
												+ ":Throw {";
										// Throw node property
										fileNodeInsertQuery += "name:\'"
												+ throwNode.getName() + "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> THROW
										fileNodeInsertQuery += ",(" + "class"
												+ classNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_THROW]->";
										fileNodeInsertQuery += "(" + "method"
												+ methodNode.getName()
												+ "throw" + String.valueOf(k)
												+ throwNode.getName() + ")";
									}
								}
								// RELATION SHIP CLASS -> METHOD
								fileNodeInsertQuery += ",(" + "class"
										+ classNode.getName() + ")";

								fileNodeInsertQuery += "-[:HAS_METHOD]->";
								fileNodeInsertQuery += "(" + "class"
										+ classNode.getName() + "method"
										+ String.valueOf(j) + ")";
							}
						}
						// RELATION SHIP FILE -> CLASS
						fileNodeInsertQuery += ",(" + "f" + ")";

						fileNodeInsertQuery += "-[:HAS_CLASS]->";
						fileNodeInsertQuery += "(" + "class"
								+ classNode.getName() + ")";
					}
				}

				// List of interfaces
				if (fileNodeAST.getNumberOfInterfaces() > 0) {
					for (int i = 0; i < fileNodeAST.getInterfaces().size(); i++) {
						InterfaceNodeAST interfaceNode = fileNodeAST
								.getInterfaces().get(i);
						fileNodeInsertQuery += ",(";
						fileNodeInsertQuery += "interface"
								+ interfaceNode.getName() + ":Interface {";
						// Class node properties
						if (interfaceNode.isHasFinalModifier())
							fileNodeInsertQuery += "HasFinalModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasFinalModifier()) + "\',";
						if (interfaceNode.isHasAbstractModifier())
							fileNodeInsertQuery += "HasAbstractModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasAbstractModifier()) + "\',";
						if (interfaceNode.isHasPrivateModifier())
							fileNodeInsertQuery += "HasPrivateModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasPrivateModifier()) + "\',";
						if (interfaceNode.isHasPublicModifier())
							fileNodeInsertQuery += "HasPublicModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasPublicModifier()) + "\',";
						if (interfaceNode.isHasProtectedModifier())
							fileNodeInsertQuery += "HasProtectedModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasProtectedModifier()) + "\',";
						if (interfaceNode.isHasStaticModifier())
							fileNodeInsertQuery += "HasStaticModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasStaticModifier()) + "\',";
						if (interfaceNode.isHasSynchronizeModifier())
							fileNodeInsertQuery += "HasSynchronizeModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasSynchronizeModifier())
									+ "\',";
						fileNodeInsertQuery += "package:\'"
								+ interfaceNode.getPackageName() + "\',";
						fileNodeInsertQuery += "name:\'"
								+ interfaceNode.getName() + "\'";
						fileNodeInsertQuery += "})";

						// Annotation Node
						if (interfaceNode.getAnnotatios().size() > 0) {
							for (int j = 0; j < interfaceNode.getAnnotatios()
									.size(); j++) {
								AnnotationNodeAST annotationNode = interfaceNode
										.getAnnotatios().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += "interface"
										+ interfaceNode.getName() + "ann"
										+ String.valueOf(j) + ":Annotation {";
								// Annotation node property
								fileNodeInsertQuery += "name:\'"
										+ annotationNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP CLASS -> ANNOTATION
								fileNodeInsertQuery += ",(" + "interface"
										+ interfaceNode.getName() + ")";

								fileNodeInsertQuery += "-[:HAS_ANNOTATION]->";
								fileNodeInsertQuery += "(" + "interface"
										+ interfaceNode.getName() + "ann"
										+ String.valueOf(j) + ")";
							}
						}

						// Method Node
						if (interfaceNode.getMethod().size() > 0) {
							for (int j = 0; j < interfaceNode.getMethod()
									.size(); j++) {
								InterfaceHasMethodNodeAST methodNode = interfaceNode
										.getMethod().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += "interface"
										+ interfaceNode.getName() + "method"
										+ String.valueOf(j) + ":Method {";

								if (methodNode.isHasFinalModifier())
									fileNodeInsertQuery += "HasFinalModifier:\'"
											+ String.valueOf(methodNode
													.isHasFinalModifier())
											+ "\',";
								if (methodNode.isHasAbstractModifier())
									fileNodeInsertQuery += "HasAbstractModifier:\'"
											+ String.valueOf(methodNode
													.isHasAbstractModifier())
											+ "\',";
								if (methodNode.isHasPrivateModifier())
									fileNodeInsertQuery += "HasPrivateModifier:\'"
											+ String.valueOf(methodNode
													.isHasPrivateModifier())
											+ "\',";
								if (methodNode.isHasPublicModifier())
									fileNodeInsertQuery += "HasPublicModifier:\'"
											+ String.valueOf(methodNode
													.isHasPublicModifier())
											+ "\',";
								if (methodNode.isHasProtectedModifier())
									fileNodeInsertQuery += "HasProtectedModifier:\'"
											+ String.valueOf(methodNode
													.isHasProtectedModifier())
											+ "\',";
								if (methodNode.isHasStaticModifier())
									fileNodeInsertQuery += "HasStaticModifier:\'"
											+ String.valueOf(methodNode
													.isHasStaticModifier())
											+ "\',";
								if (methodNode.isHasSynchronizeModifier())
									fileNodeInsertQuery += "HasSynchronizeModifier:\'"
											+ String.valueOf(methodNode
													.isHasSynchronizeModifier())
											+ "\',";

								fileNodeInsertQuery += "ReturningType:\'"
										+ methodNode.getReturningType() + "\',";
								fileNodeInsertQuery += "package:\'"
										+ methodNode.getPackageName() + "\',";
								fileNodeInsertQuery += "name:\'"
										+ methodNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// Method's RelationShips

								// Annotation Node
								if (methodNode.getAnnotatios().size() > 0) {
									for (int k = 0; k < methodNode
											.getAnnotatios().size(); k++) {
										AnnotationNodeAST annotationNode = methodNode
												.getAnnotatios().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "ann"
												+ String.valueOf(k)
												+ annotationNode.getName()
														.replace("@", "")
												+ ":Annotation {";
										// Annotation node property
										fileNodeInsertQuery += "name:\'"
												+ annotationNode.getName()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> ANNOTATION
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_ANNOTATION]->";
										fileNodeInsertQuery += "("
												+ "method"
												+ methodNode.getName()
												+ "ann"
												+ String.valueOf(k)
												+ annotationNode.getName()
														.replace("@", "") + ")";
									}
								}


								// Parameter Node
								if (methodNode.getParameters().size() > 0) {
									for (int k = 0; k < methodNode
											.getParameters().size(); k++) {
										ParameterMethodNodeAST paramNode = methodNode
												.getParameters().get(k);
										String typeParam = paramNode.getType()
												.replace("[", "");
										typeParam = typeParam.replace("]", "");
										typeParam = typeParam.replace("(", "");
										typeParam = typeParam.replace(">", "");
										typeParam = typeParam.replace("<", "");
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "param" + String.valueOf(k)
												+ paramNode.getName()
												+ typeParam + ":Parameter {";
										// Parameter node property
										fileNodeInsertQuery += "name:\'"
												+ paramNode.getName() + "\',";
										fileNodeInsertQuery += "type:\'"
												+ paramNode.getType() + "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> PARAMETER
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_PARAMETER]->";
										fileNodeInsertQuery += "(" + "method"
												+ methodNode.getName()
												+ "param" + String.valueOf(k)
												+ paramNode.getName()
												+ typeParam + ")";
									}
								}

								// Throw Method Node
								if (methodNode.getThrowsMethod().size() > 0) {
									for (int k = 0; k < methodNode
											.getThrowsMethod().size(); k++) {
										ThrowMethodNodeAST throwNode = methodNode
												.getThrowsMethod().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += "method"
												+ methodNode.getName()
												+ "throw" + String.valueOf(k)
												+ throwNode.getName()
												+ ":Throw {";
										// Throw node property
										fileNodeInsertQuery += "name:\'"
												+ throwNode.getName() + "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> THROW
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:HAS_THROW]->";
										fileNodeInsertQuery += "(" + "method"
												+ methodNode.getName()
												+ "throw" + String.valueOf(k)
												+ throwNode.getName() + ")";
									}
								}
								// RELATION SHIP CLASS -> METHOD
								fileNodeInsertQuery += ",(" + "interface"
										+ interfaceNode.getName() + ")";

								fileNodeInsertQuery += "-[:HAS_METHOD]->";
								fileNodeInsertQuery += "(" + "interface"
										+ interfaceNode.getName() + "method"
										+ String.valueOf(j) + ")";
							}
						}
						// RELATION SHIP FILE -> CLASS
						fileNodeInsertQuery += ",(" + "f" + ")";

						fileNodeInsertQuery += "-[:HAS_INTERFACE]->";
						fileNodeInsertQuery += "(" + "interface"
								+ interfaceNode.getName() + ")";
					}
				}

				fileNodeInsertQuery += ";";
				logger.info("Insertion Query: " + fileNodeInsertQuery);
				resultLog.info(fileNodeInsertQuery);

				// Insert query on Neo4j graph DB
				session.run(fileNodeInsertQuery);

			} catch (Exception e) {
				logger.debug("Excetion : ", e);
				debugLog.debug("Excetion : ", e);
				return;
			}
		} else{
			logger.debug("Driver or Session is down, check the configuration");
			debugLog.debug("Driver or Session is down, check the configuration");
		}
	}

	/*
	 * Close Neo4j Connection
	 */
	public void closeDriverSession() {
		if (session != null)
			session.close();
		if (driver != null)
			driver.close();
	}

	/*
	 * Check Neo4j Connection
	 */
	public boolean isNeo4jConnectionUp() {
		return session.isOpen();
	}

}
