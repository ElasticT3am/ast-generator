package com.elasticthree.ASTCreator.ASTCreator.Helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.elasticthree.ASTCreator.ASTCreator.Neo4jDriver.Neo4JDriver;

/**
 * This class parses parsing_info file and inserts 
 * queries into Neo4j instance DB
 * 
 * @author nikolas
 *
 */
public class LogParser {
	
	final static Logger logger = Logger.getLogger(LogParser.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");
	final static Logger resultLog = Logger.getLogger("reportsLogger");
	
	private String logFile;
	private Stream<String> lines;
	private Neo4JDriver neo4j;
	
	
	public LogParser (Neo4JDriver neo4j){
		setLogFile("parsing_info.log");
		setNeo4j(neo4j);
	}
	
	public LogParser (String file, Neo4JDriver neo4j){
		setLogFile(file);
		setNeo4j(neo4j);
	}
	
	/**
	 * Method parses log file and inserts 
	 * queries into Neo4j instance DB
	 */
	public void parsingLogFile(){
		
		try {
			logger.info("logFile " + logFile );
			lines = Files.lines(Paths.get(logFile));
			lines
			.filter(line -> line.contains("CREATE"))
	        .forEach(query -> {
	        	neo4j.insertNeo4JDBLogFile(query);
			});
		} catch (IOException e) {
			logger.debug("IO Error: ", e);
			debugLog.debug("IO Error: ", e);
		}
		
	}

	/**
	 * Testing main
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("resources/log4j.properties");
		Neo4JDriver neo4j = new Neo4JDriver();
		new LogParser(neo4j).parsingLogFile();

	}
	
	
	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public Neo4JDriver getNeo4j() {
		return neo4j;
	}

	public void setNeo4j(Neo4JDriver neo4j) {
		this.neo4j = neo4j;
	}
	
}
