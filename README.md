# ast-generator
AST-generator provides the abstract syntax trees for a GitHub project.

### Details

* Abstract Syntax Tree

Compilers, like java (javacc), often use an AST for the following bullets:
  - AST summarizes grammatical structures without including all the details of the derivation.
  - ASTs are kind of intermediate representation which helps the compilation procedure
  - Visualisation of Abstract Syntax Tree 

![buffer oberon ast](https://cloud.githubusercontent.com/assets/11991105/18111997/e36981bc-6f2c-11e6-9c24-a736bc6874b5.png)


**Ast-generator project** builds a basic AST for any GitHub project, where user pass GitHub project's path as argument using [javaparser project](http://javaparser.org/)

