package com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.neo4j.driver.v1.*;

import com.elasticthree.ASTCreator.ASTCreator.Objects.FileNodeAST;

public class Neo4JDriver {

	final static Logger logger = Logger.getLogger(Neo4JDriver.class);
	private String host;
	private String usern;
	private String password;

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

	public void insertNeo4JDB(FileNodeAST fileObject) {

		if (fileObject != null && getHost() != null) {
			Driver driver = GraphDatabase.driver("bolt://" + getHost(),
					AuthTokens.basic(getUsern(), getPassword()));
			Session session = driver.session();
			// First of All the File Node of AST
			// For testing we just printing the output
			logger.debug("// For testing we just printing the output");
					
			// session.run("CREATE (a:Person {name:'Arthur', title:'King'})");
			
			session.close();
			driver.close();
		}
	}

	public String getHost() {
		return host;
	}

	public String getUsern() {
		return usern;
	}

	public String getPassword() {
		return password;
	}

}
