package com.elasticthree.ASTCreator.ASTCreator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.elasticthree.ASTCreator.ASTCreator.Objects.AnnotationNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassHasMethodNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassImplementsNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ClassNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.CommentsNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.InterfaceHasMethodNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.InterfaceNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ParameterMethodNodeAST;
import com.elasticthree.ASTCreator.ASTCreator.Objects.ThrowMethodNodeAST;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVisitor extends VoidVisitorAdapter<Object> {

	final static Logger logger = Logger.getLogger(ASTCreator.class);
	final static Logger debugLog = Logger.getLogger("debugLogger");

	private String repoURL;
	private String packageFile;
	private long numberOfClasses;
	private long numberOfInterfaces;
	private List<ClassNodeAST> classes;
	private List<InterfaceNodeAST> interfaces;

	public ClassVisitor(String repoURL, String pacName) {
		setRepoURL(repoURL);
		setPackageFile(pacName);
		setNumberOfClasses(0);
		setNumberOfInterfaces(0);
		setClasses(new ArrayList<ClassNodeAST>());
		setInterfaces(new ArrayList<InterfaceNodeAST>());
	}

	public void setPackageFile(String packageFile) {
		this.packageFile = packageFile;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		super.visit(n, arg);
		parsing(n);
	}

	private void parsing(ClassOrInterfaceDeclaration n) {
		// It's a Class
		if (!n.isInterface()) {
			parsingClass(n);
		}
		// It's an Interface
		else {
			parsingInterface(n);
		}
	}

	private void parsingClass(ClassOrInterfaceDeclaration n) {
		numberOfClasses++;

		ClassNodeAST classNode = new ClassNodeAST(getRepoURL(), n.getName(),
				getPackageFile());
		classNode.setAllModifiers(n.getModifiers());
		if (n.getExtends() != null) {
			List<ClassOrInterfaceType> ext = n.getExtends();
			if (ext.size() > 0)
				classNode.setExtendsClass(ext.get(0).getName());
		}

		if (n.getImplements().size() != 0) {
			List<ClassImplementsNodeAST> classImpl = new ArrayList<ClassImplementsNodeAST>();
			for (ClassOrInterfaceType impl : n.getImplements()) {
				classImpl.add(new ClassImplementsNodeAST(impl.getName()));
				// logger.info("Implements interfaces: " + impl.getName());
			}
			classNode.setImpl(classImpl);
		}

		if (n.getAnnotations().size() != 0) {
			List<AnnotationNodeAST> classAnn = new ArrayList<AnnotationNodeAST>();
			for (AnnotationExpr ann : n.getAnnotations()) {
				classAnn.add(new AnnotationNodeAST(ann.toString()));
				// logger.info("Implements interfaces: " + impl.getName());
			}
			classNode.setAnnotatios(classAnn);
		}

		if (n.getAllContainedComments().size() != 0) {
			List<CommentsNodeAST> classComment = new ArrayList<CommentsNodeAST>();
			for (Comment comment : n.getAllContainedComments()) {
				classComment.add(new CommentsNodeAST(comment.toString()));
				// logger.info("Implements interfaces: " + impl.getName());
			}
			classNode.setComments(classComment);
		}

		// Method parser
		if (n.getMembers().size() != 0) {
			long numberOfMethodsPerClass = 0;
			List<ClassHasMethodNodeAST> classMethodNode = new ArrayList<ClassHasMethodNodeAST>();
			for (BodyDeclaration member : n.getMembers()) {
				if (member instanceof MethodDeclaration) {
					numberOfMethodsPerClass++;
					MethodDeclaration method = (MethodDeclaration) member;
					ClassHasMethodNodeAST methodClass = new ClassHasMethodNodeAST(
							method.getName(), getPackageFile());
					methodClass.setReturningType(method.getType().toString());

					if (method.getAllContainedComments().size() != 0) {
						List<CommentsNodeAST> commentsMethod = new ArrayList<CommentsNodeAST>();
						for (Comment comment : method.getAllContainedComments()) {
							commentsMethod.add(new CommentsNodeAST(comment
									.toString()));
						}
						methodClass.setComments(commentsMethod);
					}

					if (method.getAnnotations().size() != 0) {
						List<AnnotationNodeAST> annotatiosMethod = new ArrayList<AnnotationNodeAST>();
						for (AnnotationExpr ann : method.getAnnotations()) {
							annotatiosMethod.add(new AnnotationNodeAST(ann
									.toString()));
						}
						methodClass.setAnnotatios(annotatiosMethod);
					}

					if (method.getParameters().size() != 0) {
						List<ParameterMethodNodeAST> parametersMethod = new ArrayList<ParameterMethodNodeAST>();
						for (Parameter param : method.getParameters()) {
							parametersMethod.add(new ParameterMethodNodeAST(
									param.getType().toString(), param.getName()
											.toString()));
						}
						methodClass.setParameters(parametersMethod);
					}

					if (method.getThrows().size() != 0) {
						List<ThrowMethodNodeAST> throwsMethod = new ArrayList<ThrowMethodNodeAST>();
						for (ReferenceType reftype : method.getThrows()) {
							throwsMethod.add(new ThrowMethodNodeAST(reftype
									.toString()));
						}
						methodClass.setThrowsMethod(throwsMethod);
					}

					methodClass.setAllModifiers(method.getModifiers());
					classMethodNode.add(methodClass);
					classNode.setMethod(classMethodNode);
				}
			}
			classNode.setNumberOfMethods(numberOfMethodsPerClass);
		}
		getClasses().add(classNode);
	}

	private void parsingInterface(ClassOrInterfaceDeclaration n) {
		numberOfInterfaces++;
		InterfaceNodeAST interfaceNode = new InterfaceNodeAST(getRepoURL(),
				n.getName(), getPackageFile());
		interfaceNode.setAllModifiers(n.getModifiers());

		if (n.getAnnotations().size() != 0) {
			List<AnnotationNodeAST> interfAnn = new ArrayList<AnnotationNodeAST>();
			for (AnnotationExpr ann : n.getAnnotations()) {
				interfAnn.add(new AnnotationNodeAST(ann.toString()));
				// logger.info("Implements interfaces: " + impl.getName());
			}
			interfaceNode.setAnnotatios(interfAnn);
		}

		if (n.getAllContainedComments().size() != 0) {
			List<CommentsNodeAST> interfComment = new ArrayList<CommentsNodeAST>();
			for (Comment comment : n.getAllContainedComments()) {
				interfComment.add(new CommentsNodeAST(comment.toString()));
			}
			interfaceNode.setComments(interfComment);
		}

		// Method parser of Interface
		if (n.getMembers().size() != 0) {
			long numberOfMethodsPerInterface = 0;
			List<InterfaceHasMethodNodeAST> interfMethodNode = new ArrayList<InterfaceHasMethodNodeAST>();
			for (BodyDeclaration member : n.getMembers()) {
				if (member instanceof MethodDeclaration) {
					numberOfMethodsPerInterface++;
					MethodDeclaration method = (MethodDeclaration) member;
					InterfaceHasMethodNodeAST methodInterface = new InterfaceHasMethodNodeAST(
							method.getName(), getPackageFile());
					methodInterface.setReturningType(method.getType()
							.toString());

					if (method.getAllContainedComments().size() != 0) {
						List<CommentsNodeAST> commentsMethod = new ArrayList<CommentsNodeAST>();
						for (Comment comment : method.getAllContainedComments()) {
							commentsMethod.add(new CommentsNodeAST(comment
									.toString()));
						}
						methodInterface.setComments(commentsMethod);
					}

					if (method.getAnnotations().size() != 0) {
						List<AnnotationNodeAST> annotatiosMethod = new ArrayList<AnnotationNodeAST>();
						for (AnnotationExpr ann : method.getAnnotations()) {
							annotatiosMethod.add(new AnnotationNodeAST(ann
									.toString()));
						}
						methodInterface.setAnnotatios(annotatiosMethod);
					}

					if (method.getParameters().size() != 0) {
						List<ParameterMethodNodeAST> parametersMethod = new ArrayList<ParameterMethodNodeAST>();
						for (Parameter param : method.getParameters()) {
							parametersMethod.add(new ParameterMethodNodeAST(
									param.getType().toString(), param.getName()
											.toString()));
						}
						methodInterface.setParameters(parametersMethod);
					}

					if (method.getThrows().size() != 0) {
						List<ThrowMethodNodeAST> throwsMethod = new ArrayList<ThrowMethodNodeAST>();
						for (ReferenceType reftype : method.getThrows()) {
							throwsMethod.add(new ThrowMethodNodeAST(reftype
									.toString()));
						}
						methodInterface.setThrowsMethod(throwsMethod);
					}

					methodInterface.setAllModifiers(method.getModifiers());
					interfMethodNode.add(methodInterface);
					interfaceNode.setMethod(interfMethodNode);
				}
			}
			interfaceNode.setNumberOfMethods(numberOfMethodsPerInterface);
		}
		getInterfaces().add(interfaceNode);
	}

	public String getPackageFile() {
		return packageFile;
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

	public String getRepoURL() {
		return repoURL;
	}

	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}

}
