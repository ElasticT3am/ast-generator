#ast-generator
AST-generator project creates an Abstract Syntax Tree (AST) for each file of a Java project, translates them to graph models and inserts them to [Neo4J (Graph Distributed DB)](https://neo4j.com/) instance.

###Details

Compilers, like java (javacc), often use an Abstract Syntax Tree (AST) for the following bullets:
  - AST summarizes grammatical structures without including all the details of the derivation.
  - ASTs are kind of intermediate representation which helps the compilation procedure
  - Visualisation of Abstract Syntax Tree 

![buffer oberon ast](https://cloud.githubusercontent.com/assets/11991105/18111997/e36981bc-6f2c-11e6-9c24-a736bc6874b5.png)

**Ast-generator project** builds a basic AST for a Java project, using [javaparser project](http://javaparser.org/)


###Installation / Running

To run this project you have to create a file (config.properties) under the resource file (like the screenshot below) with the Neo4J instance credentials

![screen shot 2016-09-06 at 02 23 57](https://cloud.githubusercontent.com/assets/11991105/18258138/01f770f2-73d9-11e6-821d-3b47ec8535fb.png)

```
host = x.x.x.x
neo4j_username = username
neo4j_password = password
```

####Running with Maven

```
mvn clean install
mvn -Dexec.args="path_to_project URL_Of_project"  -Dexec.mainClass="com.elasticthree.ASTCreator.ASTCreator.ASTCreator" exec:java
```

Observation

URL_Of_project parameter is useful for Neo4j stats. If you don't have an unique URL, you can pass the same value as path_to_project
 
###Results

Using the following bullets you will see the graphs using Neo4j browser

* Go to Neo4j browser

* Run this command

```
START n=node(*) RETURN n;
```

* Visualization of results

![screen shot 2016-09-04 at 18 56 29](https://cloud.githubusercontent.com/assets/11991105/18232103/73a582a6-72d1-11e6-9011-f0a468595e3f.png)

*  Query for Total number of Java Code lines and total number of Repos in Neo4j instance

```
MATCH (nodes:Repo) RETURN SUM(nodes.linesOfJavaCode) as TotalJavaCode, count(nodes) as TotalNodes;
```

![screen shot 2016-09-08 at 03 25 07](https://cloud.githubusercontent.com/assets/11991105/18333027/f7d59cec-7573-11e6-898a-dcd78d650117.png)

###Results

We present some important and practical queries on AST datasets !

######Query1: Returns all the parameters of a specific method "name"

``` 
Match (m:Method) -[:HAS_PARAMETER]-> (p:Parameter)Where m.name = "name" return p.name as Name, p.type as Type;
```
######Query2: Returns all the parameters from a method, whch has annotation '@Override'

```
Match (m:Method) -[:HAS_PARAMETER]-> (p:Parameter) MATCH (m:Method) -[:HAS_ANNOTATION]-> (ann:Annotation) Where ann.name = "@Override" return p.name as Name, p.type as Type;
```

######Query3: Returns all the methods, whch has parameter type "type"

```
Match (m:Method) -[:HAS_PARAMETER]-> (p:Parameter) Where p.type = "type" return m.name as Name, m.package as Package;
```

###[Final Project Evaluation](https://github.com/ElasticThree/Neo4j_vs_Titan/tree/master/Evaluation)
