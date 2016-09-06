package com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.neo4j.driver.v1.*;


public class TestDriver {

	final static Logger stdoutLog = Logger.getLogger(TestDriver.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	final static Logger resultLog = Logger.getLogger("reportsLogger");
	private String host;
	private String usern;
	private String password;

	public TestDriver() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("resources/config.properties");
			prop.load(input);
			this.host = prop.getProperty("host");
			this.usern = prop.getProperty("neo4j_username");
			this.password = prop.getProperty("neo4j_password");
		} catch (IOException ex) {
			ex.printStackTrace();
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
	}
	
	public void printAllNodes(){
		Driver driver = GraphDatabase.driver( "bolt://" + host, AuthTokens.basic( usern, password ) );
		Session session = driver.session();
		StatementResult result = session.run( "MATCH (n)-[:HAS_CLASS]-(x) RETURN x  Limit 5;" );
		stdoutLog.info("Nodes: " + result.list().size());
		session.close();
		driver.close();
	}
	
	
	
	
	
	public static void main (String[] ar){
		
		
		new TestDriver().printAllNodes();
		
		
	}

}
