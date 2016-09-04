# ast-generator
AST-generator provides the abstract syntax trees for a GitHub project.

### Details

* Abstract Syntax Tree

Compilers, like java (javacc), often use an AST for the following bullets:
  - AST summarizes grammatical structures without including all the details of the derivation.
  - ASTs are kind of intermediate representation which helps the compilation procedure
  - Visualisation of Abstract Syntax Tree 

![buffer oberon ast](https://cloud.githubusercontent.com/assets/11991105/18111997/e36981bc-6f2c-11e6-9c24-a736bc6874b5.png)

**Ast-generator project** builds a basic AST for any GitHub project, using [javaparser project](http://javaparser.org/)

User just passes GitHub project path (for AST) as argument and after runs the ast-generator java project (Maven project) 

### Installation / Running

To run this project you have to create a file (config.properties) under the resource file with the Neo4J instance credentials

> host = x.x.x.x

> neo4j_username = username

> neo4j_password = password

* Running with Maven

> mvn clean install

> mvn -Dexec.args="path_to_project"  -Dexec.mainClass="com.elasticthree.ASTCreator.ASTCreator.ASTCreator" exec:java

### Results

* Go to Neo4j browser

* Run this command

> START n=node(*) RETURN n;

* Virtualization of results

![screen shot 2016-09-04 at 18 56 29](https://cloud.githubusercontent.com/assets/11991105/18232103/73a582a6-72d1-11e6-9011-f0a468595e3f.png)
