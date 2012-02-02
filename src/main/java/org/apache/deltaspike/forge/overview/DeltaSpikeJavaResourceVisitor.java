package org.apache.deltaspike.forge.overview;

import com.google.common.io.CharStreams;
import org.apache.deltaspike.forge.util.JavaResourceVisitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jface.text.Document;
import org.jboss.forge.resources.java.JavaResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rudy De Busscher
 *         FIXME Review data structures and flow, POC
 */
public class DeltaSpikeJavaResourceVisitor implements JavaResourceVisitor {

    private ProjectOverview projectOverview;

    public DeltaSpikeJavaResourceVisitor(ProjectOverview someProjectOverview) {
        projectOverview = someProjectOverview;
    }

    @Override
    public void visit(JavaResource javaResource) {

        String data = loadJavaFileContents(javaResource);

        CompilationUnit unit = prepareParser(data);

        DeltaSpikeASTVisitor visitor = new DeltaSpikeASTVisitor();
        unit.accept(visitor);

        String packageName = unit.getPackage().getName().getFullyQualifiedName();
        projectOverview.addClassInformation(packageName, visitor.getClassInformationList());

    }

    private CompilationUnit prepareParser(String someData) {
        Document document = new Document(someData);
        ASTParser parser = ASTParser.newParser(AST.JLS3);

        parser.setSource(document.get().toCharArray());
        Map options = JavaCore.getOptions();
        options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
        parser.setCompilerOptions(options);

        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        unit.recordModifications();
        return unit;
    }

    private String loadJavaFileContents(JavaResource javaResource) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(javaResource.getUnderlyingResourceObject());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // FIXME
        }
        String data = null;
        try {
            data = CharStreams.toString(new InputStreamReader(stream));
        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
        }
        return data;
    }

    public static class DeltaSpikeASTVisitor extends ASTVisitor {

        private List<String> nestedClasses = new ArrayList<String>();

        private List<String> importList = new ArrayList<String>();

        private List<ClassInformation> classInformationList = new ArrayList<ClassInformation>();

        public boolean visit(final TypeDeclaration node) {

            ClassInformation classInformation = new ClassInformation();

            nestedClasses.add(node.getName().getIdentifier());

            classInformation.setSimpleName(defineName(nestedClasses));
            if (node.getSuperclassType() != null) {
                classInformation.setSuperType(((SimpleType) node.getSuperclassType()).getName().getFullyQualifiedName
                        ());
            }

            for (Object o : node.superInterfaceTypes()) {
                if (o instanceof SimpleType) {
                    classInformation.getInterfaceTypes().add(((SimpleType) o).getName().getFullyQualifiedName());
                } else {
                    // FIXME
                    throw new IllegalArgumentException(o.getClass().getName());
                }
            }

            classInformation.setImportList(importList);
            classInformationList.add(classInformation);
            return true;
        }

        private String defineName(List<String> someNestedClasses) {
            StringBuilder result = new StringBuilder();
            for (String className : someNestedClasses) {
                if (result.length() > 0) {
                    result.append('$');
                }
                result.append(className);
            }
            return result.toString();
        }

        public void endVisit(ImportDeclaration node) {
            importList.add(node.getName().getFullyQualifiedName());
        }

        public void endVisit(final TypeDeclaration node) {
            nestedClasses.remove(nestedClasses.size() - 1);
        }

        public List<ClassInformation> getClassInformationList() {
            return classInformationList;
        }
    }

}
