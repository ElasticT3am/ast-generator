package com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver;

import com.elasticthree.ASTCreator.ASTCreator.Helpers.StaticVariables;
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
	 * Neo4JDriver creates and inserts the query to Neo4j instance
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
	 * 
	 * @param query
	 */
	public void insertNeo4JDBLogFile(String query) {

		if (isNeo4jConnectionUp()) {

			try {
				// Insert query on Neo4j graph DB
				session.run(query);
				logger.info("Insertion Query: " + query);

			} catch (Exception e) {
				logger.debug("Excetion : ", e);
				debugLog.debug("Excetion : ", e);
				return;
			}
		} else {
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

		if (fileNodeAST == null) {
			logger.debug("AST File Object is null (Probably had parsing error)");
			debugLog.debug("AST File Object is null (Probably had parsing error)");
			return;
		}

		if (isNeo4jConnectionUp()) {
			try {
				// File Node of AST
				String fileNodeInsertQuery = "CREATE (";
				fileNodeInsertQuery += "f:" + StaticVariables.fileNodeName
						+ " {";
				// File node properties
				fileNodeInsertQuery += StaticVariables.URLRepoPropertyName
						+ ":\'" + fileNodeAST.getRepoURL() + "\',";
				fileNodeInsertQuery += StaticVariables.packagePropertyName
						+ ":\'" + fileNodeAST.getPackageName() + "\',";
				fileNodeInsertQuery += StaticVariables.namePropertyName + ":\'"
						+ fileNodeAST.getName() + "\',";
				fileNodeInsertQuery += StaticVariables.numberOfClassesPropertyName
						+ ":"
						+ String.valueOf(fileNodeAST.getNumberOfClasses())
						+ ",";
				fileNodeInsertQuery += StaticVariables.numberOfInterfacesPropertyName
						+ ":"
						+ String.valueOf(fileNodeAST.getNumberOfInterfaces())
						+ ""
						+ "})";

				// List of Classes
				if (fileNodeAST.getNumberOfClasses() > 0) {

					for (int i = 0; i < fileNodeAST.getClasses().size(); i++) {
						ClassNodeAST classNode = fileNodeAST.getClasses()
								.get(i);
						fileNodeInsertQuery += ",(";
						fileNodeInsertQuery += 
								"class" 
								+ classNode.getName()
								+ String.valueOf(i)
								+ ":" + StaticVariables.classNodeName + " {";
						// Class node properties
						if (classNode.isHasFinalModifier())
							fileNodeInsertQuery += "hasFinalModifier:\'"
									+ String.valueOf(classNode
											.isHasFinalModifier()) + "\',";
						if (classNode.isHasAbstractModifier())
							fileNodeInsertQuery += "hasAbstractModifier:\'"
									+ String.valueOf(classNode
											.isHasAbstractModifier()) + "\',";
						if (classNode.isHasPrivateModifier())
							fileNodeInsertQuery += "hasPrivateModifier:\'"
									+ String.valueOf(classNode
											.isHasPrivateModifier()) + "\',";
						if (classNode.isHasPublicModifier())
							fileNodeInsertQuery += "hasPublicModifier:\'"
									+ String.valueOf(classNode
											.isHasPublicModifier()) + "\',";
						if (classNode.isHasProtectedModifier())
							fileNodeInsertQuery += "hasProtectedModifier:\'"
									+ String.valueOf(classNode
											.isHasProtectedModifier()) + "\',";
						if (classNode.isHasStaticModifier())
							fileNodeInsertQuery += "hasStaticModifier:\'"
									+ String.valueOf(classNode
											.isHasStaticModifier()) + "\',";
						if (classNode.isHasSynchronizeModifier())
							fileNodeInsertQuery += "hasSynchronizeModifier:\'"
									+ String.valueOf(classNode
											.isHasSynchronizeModifier())
									+ "\',";
						if (!classNode.getExtendsClass().equalsIgnoreCase(
								"None")) {
							fileNodeInsertQuery += StaticVariables.extendsClassPropertyName
									+ ":\'"
									+ classNode.getExtendsClass()
									+ "\',";
						}
						fileNodeInsertQuery += StaticVariables.numberOfMethodsClassPropertyName
								+ ":"
								+ String.valueOf(classNode.getNumberOfMethods())
								+ ",";
						fileNodeInsertQuery += StaticVariables.URLRepoPropertyName
								+ ":\'" + classNode.getRepoURL() + "\',";
						fileNodeInsertQuery += StaticVariables.packageClassPropertyName
								+ ":\'" + classNode.getPackageName() + "\',";
						fileNodeInsertQuery += StaticVariables.nameClassPropertyName
								+ ":\'" + classNode.getName() + "\'";
						fileNodeInsertQuery += "})";

						// Annotation Node
						if (classNode.getAnnotatios().size() > 0) {
							for (int j = 0; j < classNode.getAnnotatios()
									.size(); j++) {
								AnnotationNodeAST annotationNode = classNode
										.getAnnotatios().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += 
										"class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ "ann"
										+ String.valueOf(j) + ":"
										+ StaticVariables.annotationNodeName
										+ " {";

								// Annotation node property
								fileNodeInsertQuery += StaticVariables.nameAnnotationPropertyName
										+ ":\'"
										+ annotationNode.getName().replace("\"", "|").replace("\'", "")
										+ "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP CLASS -> ANNOTATION
								fileNodeInsertQuery += ",(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i) 
										+ ")";

								fileNodeInsertQuery += "-[:"
										+ StaticVariables.has_annotationPropertyName
										+ "]->";
								fileNodeInsertQuery += "(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ "ann"
										+ String.valueOf(j) + ")";
							}
						}

						// Implements Interface Node
						if (classNode.getImpl().size() > 0) {
							for (int j = 0; j < classNode.getImpl().size(); j++) {
								ClassImplementsNodeAST implNode = classNode
										.getImpl().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += 
										"class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ "impl"
										+ String.valueOf(j)
										+ ":"
										+ StaticVariables.implementsInterfaceNodeName
										+ " {";
								// Implements Interface node property
								fileNodeInsertQuery += StaticVariables.implementsInterfacePropertyName
										+ ":\'" + implNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP CLASS -> IMPLEMENTS_INTERFACE
								fileNodeInsertQuery += ",(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ ")";

								fileNodeInsertQuery += "-[:"
										+ StaticVariables.implements_interfacePropertyName
										+ "]->";
								fileNodeInsertQuery += "(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i) 
										+ "impl"
										+ String.valueOf(j) + ")";
							}
						}

						// Method Node
						if (classNode.getMethod().size() > 0) {
							for (int j = 0; j < classNode.getMethod().size(); j++) {
								ClassHasMethodNodeAST methodNode = classNode
										.getMethod().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += 
										"class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ "method"
										+ String.valueOf(j) + ":"
										+ StaticVariables.methodNodeName + " {";

								if (methodNode.isHasFinalModifier())
									fileNodeInsertQuery += "hasFinalModifier:\'"
											+ String.valueOf(methodNode
													.isHasFinalModifier())
											+ "\',";
								if (methodNode.isHasAbstractModifier())
									fileNodeInsertQuery += "hasAbstractModifier:\'"
											+ String.valueOf(methodNode
													.isHasAbstractModifier())
											+ "\',";
								if (methodNode.isHasPrivateModifier())
									fileNodeInsertQuery += "hasPrivateModifier:\'"
											+ String.valueOf(methodNode
													.isHasPrivateModifier())
											+ "\',";
								if (methodNode.isHasPublicModifier())
									fileNodeInsertQuery += "hasPublicModifier:\'"
											+ String.valueOf(methodNode
													.isHasPublicModifier())
											+ "\',";
								if (methodNode.isHasProtectedModifier())
									fileNodeInsertQuery += "hasProtectedModifier:\'"
											+ String.valueOf(methodNode
													.isHasProtectedModifier())
											+ "\',";
								if (methodNode.isHasStaticModifier())
									fileNodeInsertQuery += "hasStaticModifier:\'"
											+ String.valueOf(methodNode
													.isHasStaticModifier())
											+ "\',";
								if (methodNode.isHasSynchronizeModifier())
									fileNodeInsertQuery += "hasSynchronizeModifier:\'"
											+ String.valueOf(methodNode
													.isHasSynchronizeModifier())
											+ "\',";

								fileNodeInsertQuery += StaticVariables.returningTypeMethodPropertyName
										+ ":\'"
										+ methodNode.getReturningType()
										+ "\',";
								fileNodeInsertQuery += StaticVariables.URLRepoPropertyName
										+ ":\'" + classNode.getRepoURL() + "\',";
								fileNodeInsertQuery += StaticVariables.packageMethodPropertyName
										+ ":\'"
										+ methodNode.getPackageName()
										+ "\',";
								fileNodeInsertQuery += StaticVariables.nameMethodPropertyName
										+ ":\'" + methodNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// Method's RelationShips

								// Annotation Node
								if (methodNode.getAnnotatios().size() > 0) {
									for (int k = 0; k < methodNode
											.getAnnotatios().size(); k++) {
										AnnotationNodeAST annotationNode = methodNode
												.getAnnotatios().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += 
												"class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j)
												+ "ann"
												+ String.valueOf(k)
												+ ":"
												+ StaticVariables.annotationNodeName
												+ " {";
										// Annotation node property
										fileNodeInsertQuery += StaticVariables.nameAnnotationPropertyName
												+ ":\'"
												+ annotationNode.getName().replace("\"", "|").replace("\'", "")
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> ANNOTATION
										fileNodeInsertQuery += ",(" 
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_annotationPropertyName
												+ "]->";
										fileNodeInsertQuery += "("
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "ann"
												+ String.valueOf(k) + ")";
									}
								}

								// Parameter Node
								if (methodNode.getParameters().size() > 0) {
									for (int k = 0; k < methodNode
											.getParameters().size(); k++) {
										ParameterMethodNodeAST paramNode = methodNode
												.getParameters().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery +=
												"class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j)
												+ "param"
												+ String.valueOf(k)
												+ ":"
												+ StaticVariables.parameterNodeName
												+ " {";
										// Parameter node property
										fileNodeInsertQuery += StaticVariables.nameParameterPropertyName
												+ ":\'"
												+ paramNode.getName()
												+ "\',";
										fileNodeInsertQuery += StaticVariables.typeParameterPropertyName
												+ ":\'"
												+ paramNode.getType()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> PARAMETER
										fileNodeInsertQuery += ",(" 
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_parameterPropertyName
												+ "]->";
										fileNodeInsertQuery += "(" 
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "param"
												+ String.valueOf(k)
												+ ")";
									}
								}

								// Throw Method Node
								if (methodNode.getThrowsMethod().size() > 0) {
									for (int k = 0; k < methodNode
											.getThrowsMethod().size(); k++) {
										ThrowMethodNodeAST throwNode = methodNode
												.getThrowsMethod().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += 
												"class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "throw"
												+ String.valueOf(k)
												+ ":"
												+ StaticVariables.throwNodeName
												+ " {";
										// Throw node property
										fileNodeInsertQuery += StaticVariables.nameThrowPropertyName
												+ ":\'"
												+ throwNode.getName()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> THROW
										fileNodeInsertQuery += ",(" 
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_throwPropertyName
												+ "]->";
										fileNodeInsertQuery += "(" 
												+ "class" 
												+ classNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "throw"
												+ String.valueOf(k)
												+ ")";
									}
								}
								// RELATION SHIP CLASS -> METHOD
								fileNodeInsertQuery += ",(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ ")";

								fileNodeInsertQuery += "-[:"
										+ StaticVariables.has_methodPropertyName
										+ "]->";
								fileNodeInsertQuery += "(" 
										+ "class" 
										+ classNode.getName()
										+ String.valueOf(i)
										+ "method"
										+ String.valueOf(j) 
										+ ")";
							}
						}
						// RELATION SHIP FILE -> CLASS
						fileNodeInsertQuery += ",(" + "f" + ")";

						fileNodeInsertQuery += "-[:"
								+ StaticVariables.has_classPropertyName + "]->";
						fileNodeInsertQuery += "(" + "class" 
								+ classNode.getName()
								+ String.valueOf(i) + ")";
					}
				}

				// //////////////
				// Interfaces //
				// //////////////

				// List of interfaces
				if (fileNodeAST.getNumberOfInterfaces() > 0) {
					for (int i = 0; i < fileNodeAST.getInterfaces().size(); i++) {
						InterfaceNodeAST interfaceNode = fileNodeAST
								.getInterfaces().get(i);
						fileNodeInsertQuery += ",(";
						fileNodeInsertQuery += 
								"interface"
								+ interfaceNode.getName()
								+ String.valueOf(i) + ":"
								+ StaticVariables.interfaceNodeName + " {";
						// Class node properties
						if (interfaceNode.isHasFinalModifier())
							fileNodeInsertQuery += "hasFinalModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasFinalModifier()) + "\',";
						if (interfaceNode.isHasAbstractModifier())
							fileNodeInsertQuery += "hasAbstractModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasAbstractModifier()) + "\',";
						if (interfaceNode.isHasPrivateModifier())
							fileNodeInsertQuery += "hasPrivateModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasPrivateModifier()) + "\',";
						if (interfaceNode.isHasPublicModifier())
							fileNodeInsertQuery += "hasPublicModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasPublicModifier()) + "\',";
						if (interfaceNode.isHasProtectedModifier())
							fileNodeInsertQuery += "hasProtectedModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasProtectedModifier()) + "\',";
						if (interfaceNode.isHasStaticModifier())
							fileNodeInsertQuery += "hasStaticModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasStaticModifier()) + "\',";
						if (interfaceNode.isHasSynchronizeModifier())
							fileNodeInsertQuery += "hasSynchronizeModifier:\'"
									+ String.valueOf(interfaceNode
											.isHasSynchronizeModifier())
									+ "\',";
						fileNodeInsertQuery += StaticVariables.URLRepoPropertyName
								+ ":\'" + interfaceNode.getRepoURL() + "\',";
						fileNodeInsertQuery += StaticVariables.packageInterfacePropertyName
								+ ":\'"
								+ interfaceNode.getPackageName()
								+ "\',";
						fileNodeInsertQuery += StaticVariables.nameInterfacePropertyName
								+ ":\'" + interfaceNode.getName() + "\'";
						fileNodeInsertQuery += "})";

						// Annotation Node
						if (interfaceNode.getAnnotatios().size() > 0) {
							for (int j = 0; j < interfaceNode.getAnnotatios()
									.size(); j++) {
								AnnotationNodeAST annotationNode = interfaceNode
										.getAnnotatios().get(j);
								fileNodeInsertQuery += ",(";
								fileNodeInsertQuery += 
										"interface"
										+ interfaceNode.getName()
										+ String.valueOf(i)
										+ "ann"
										+ String.valueOf(j) + ":"
										+ StaticVariables.annotationNodeName
										+ " {";
								// Annotation node property
								fileNodeInsertQuery += StaticVariables.nameAnnotationPropertyName
										+ ":\'"
										+ annotationNode.getName().replace("\"", "|").replace("\'", "")
										+ "\'";
								fileNodeInsertQuery += "})";

								// RELATION SHIP INTERFACE -> ANNOTATION
								fileNodeInsertQuery += ",(" 
										+ "interface"
										+ interfaceNode.getName()
										+ String.valueOf(i) 
										+ ")";

								fileNodeInsertQuery += "-[:"
										+ StaticVariables.has_annotationPropertyName
										+ "]->";
								fileNodeInsertQuery += "(" 
										+ "interface"
										+ interfaceNode.getName()
										+ String.valueOf(i)
										+ "ann"
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
								fileNodeInsertQuery += 
										"interface"
										+ interfaceNode.getName()
										+ String.valueOf(i) + "method"
										+ String.valueOf(j) + ":"
										+ StaticVariables.methodNodeName + " {";

								if (methodNode.isHasFinalModifier())
									fileNodeInsertQuery += "hasFinalModifier:\'"
											+ String.valueOf(methodNode
													.isHasFinalModifier())
											+ "\',";
								if (methodNode.isHasAbstractModifier())
									fileNodeInsertQuery += "hasAbstractModifier:\'"
											+ String.valueOf(methodNode
													.isHasAbstractModifier())
											+ "\',";
								if (methodNode.isHasPrivateModifier())
									fileNodeInsertQuery += "hasPrivateModifier:\'"
											+ String.valueOf(methodNode
													.isHasPrivateModifier())
											+ "\',";
								if (methodNode.isHasPublicModifier())
									fileNodeInsertQuery += "hasPublicModifier:\'"
											+ String.valueOf(methodNode
													.isHasPublicModifier())
											+ "\',";
								if (methodNode.isHasProtectedModifier())
									fileNodeInsertQuery += "hasProtectedModifier:\'"
											+ String.valueOf(methodNode
													.isHasProtectedModifier())
											+ "\',";
								if (methodNode.isHasStaticModifier())
									fileNodeInsertQuery += "hasStaticModifier:\'"
											+ String.valueOf(methodNode
													.isHasStaticModifier())
											+ "\',";
								if (methodNode.isHasSynchronizeModifier())
									fileNodeInsertQuery += "hasSynchronizeModifier:\'"
											+ String.valueOf(methodNode
													.isHasSynchronizeModifier())
											+ "\',";

								fileNodeInsertQuery += StaticVariables.returningTypeMethodPropertyName
										+ ":\'"
										+ methodNode.getReturningType()
										+ "\',";
								fileNodeInsertQuery += StaticVariables.URLRepoPropertyName
										+ ":\'" + interfaceNode.getRepoURL() + "\',";
								fileNodeInsertQuery += StaticVariables.packageMethodPropertyName
										+ ":\'"
										+ methodNode.getPackageName()
										+ "\',";
								fileNodeInsertQuery += StaticVariables.nameMethodPropertyName
										+ ":\'" + methodNode.getName() + "\'";
								fileNodeInsertQuery += "})";

								// Method's RelationShips

								// Annotation Node
								if (methodNode.getAnnotatios().size() > 0) {
									for (int k = 0; k < methodNode
											.getAnnotatios().size(); k++) {
										AnnotationNodeAST annotationNode = methodNode
												.getAnnotatios().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery += 
												"interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j)
												+ "ann"
												+ String.valueOf(k)
												+ ":"
												+ StaticVariables.annotationNodeName
												+ " {";
										// Annotation node property
										fileNodeInsertQuery += StaticVariables.nameAnnotationPropertyName
												+ ":\'"
												+ annotationNode.getName().replace("\"", "|").replace("\'", "")
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> ANNOTATION
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_annotationPropertyName
												+ "]->";
										fileNodeInsertQuery += "(" 
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "ann"
												+ String.valueOf(k) + ")";
									}
								}

								// Parameter Node
								if (methodNode.getParameters().size() > 0) {
									for (int k = 0; k < methodNode
											.getParameters().size(); k++) {
										ParameterMethodNodeAST paramNode = methodNode
												.getParameters().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery +=
												"interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j)
												+ "param"
												+ String.valueOf(k)
												+ ":"
												+ StaticVariables.parameterNodeName
												+ " {";
										// Parameter node property
										fileNodeInsertQuery += StaticVariables.nameParameterPropertyName
												+ ":\'"
												+ paramNode.getName()
												+ "\',";
										fileNodeInsertQuery += StaticVariables.typeParameterPropertyName
												+ ":\'"
												+ paramNode.getType()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> PARAMETER
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ interfaceNode.getName()
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_parameterPropertyName
												+ "]->";
										fileNodeInsertQuery += "(" 
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "param"
												+ String.valueOf(k) + ")";
									}
								}

								// Throw Method Node
								if (methodNode.getThrowsMethod().size() > 0) {
									for (int k = 0; k < methodNode
											.getThrowsMethod().size(); k++) {
										ThrowMethodNodeAST throwNode = methodNode
												.getThrowsMethod().get(k);
										fileNodeInsertQuery += ",(";
										fileNodeInsertQuery +=
												"interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "throw"
												+ String.valueOf(k) + ":"
												+ StaticVariables.throwNodeName
												+ " {";
										// Throw node property
										fileNodeInsertQuery += StaticVariables.nameThrowPropertyName
												+ ":\'"
												+ throwNode.getName()
												+ "\'";
										fileNodeInsertQuery += "})";

										// RELATION SHIP METHOD -> THROW
										fileNodeInsertQuery += ",("
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method" + String.valueOf(j)
												+ ")";

										fileNodeInsertQuery += "-[:"
												+ StaticVariables.has_throwPropertyName
												+ "]->";
										fileNodeInsertQuery += "(" 
												+ "interface"
												+ interfaceNode.getName()
												+ String.valueOf(i)
												+ "method"
												+ methodNode.getName()
												+ String.valueOf(j) + "throw"
												+ String.valueOf(k) + ")";
									}
								}
								// RELATION SHIP INTERFACE -> METHOD
								fileNodeInsertQuery += ",(" + "interface"
										+ interfaceNode.getName()
										+ String.valueOf(i) + ")";

								fileNodeInsertQuery += "-[:"
										+ StaticVariables.has_methodPropertyName
										+ "]->";
								fileNodeInsertQuery += "(" + "interface"
										+ interfaceNode.getName()
										+ String.valueOf(i) + "method"
										+ String.valueOf(j) + ")";
							}
						}
						// RELATION SHIP FILE -> CLASS
						fileNodeInsertQuery += ",(" + "f" + ")";

						fileNodeInsertQuery += "-[:"
								+ StaticVariables.has_interfacePropertyName
								+ "]->";
						fileNodeInsertQuery += "(" + "interface"
								+ interfaceNode.getName()
								+ String.valueOf(i) + ")";
					}
				}

				fileNodeInsertQuery += ";";

				// Insert query on Neo4j graph DB
				session.run(fileNodeInsertQuery);

				logger.info("Insertion Query: " + fileNodeInsertQuery);
				resultLog.info(fileNodeInsertQuery);

			} catch (Exception e) {
				logger.debug("Excetion : ", e);
				debugLog.debug("Excetion : ", e);
				return;
			}
		} else {
			logger.debug("Driver or Session is down, check the configuration");
			debugLog.debug("Driver or Session is down, check the configuration");
		}
	}

	public void insertRepoNodeNeo4JDB(String repoURL, long linesOfJavaCode) {

		if (isNeo4jConnectionUp()) {
			// File Node of AST
			String nodeInsertQuery = "CREATE (";
			nodeInsertQuery += "r:" + StaticVariables.repoNodeName + " {";
			// File node properties
			nodeInsertQuery += StaticVariables.URLRepoPropertyName + ":\'"
					+ repoURL + "\',";
			nodeInsertQuery += StaticVariables.linesOfJavaCodeRepoPropertyName
					+ ":" + String.valueOf(linesOfJavaCode) + "";
			nodeInsertQuery += "})";

			nodeInsertQuery += ";";
			logger.info("Insertion Query: " + nodeInsertQuery);
			resultLog.info(nodeInsertQuery);

			// Insert query on Neo4j graph DB
			session.run(nodeInsertQuery);

		} else {
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

	public String escapingCharacters(String query) {

		return query;
	}

	/*
	 * Check Neo4j Connection
	 */
	public boolean isNeo4jConnectionUp() {
		return session.isOpen();
	}

}
