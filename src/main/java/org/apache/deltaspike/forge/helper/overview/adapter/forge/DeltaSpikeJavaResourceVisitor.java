package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import org.eclipse.jdt.core.dom.*;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.impl.JDTHelper;
import org.jboss.forge.parser.java.util.Types;
import org.jboss.forge.parser.spi.WildcardImportResolver;

import javax.enterprise.inject.Typed;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * An AST Visitor for the Eclipse Java parser that can handle nested classes. (unlike the Forge default)
 *
 * @author Rudy De Busscher
 */

@Typed
public class DeltaSpikeJavaResourceVisitor extends ASTVisitor {

    private static ServiceLoader<WildcardImportResolver> loader = ServiceLoader.load(WildcardImportResolver.class);

    private static List<WildcardImportResolver> resolvers;

    private List<String> nestedClasses = new ArrayList<String>();

    private String packageName;

    private List<Import> importList = new ArrayList<Import>();

    private List<AdapterJavaClass> classData = new ArrayList<AdapterJavaClass>();

    public DeltaSpikeJavaResourceVisitor() {
    }

    public boolean visit(final TypeDeclaration node) {

        nestedClasses.add(node.getName().getIdentifier());

        AdapterJavaClass javaClass = new AdapterJavaClass();
        javaClass.setName(defineName(nestedClasses));

        javaClass.setPackage(packageName);

        if (node.getSuperclassType() != null) {
            javaClass.setSuperType(resolveType(node.getSuperclassType()));
        }

        for (Object o : node.superInterfaceTypes()) {
            if (o instanceof SimpleType) {
                javaClass.addInterface(resolveType((SimpleType) o));
            } else {
                // FIXME
                throw new IllegalArgumentException(o.getClass().getName());
            }
        }

        List<?> modifiers = node.modifiers();
        for (Object object : modifiers) {
            if (object instanceof Annotation) {

                Name typeName = ((Annotation) object).getTypeName();
                javaClass.addAnnotation(resolveType(typeName.getFullyQualifiedName()));
            }
        }

        for (FieldDeclaration field : node.getFields()) {

            AdapterField adapterField = new AdapterField();

            adapterField.setQualifiedType(resolveType(field.getType()));

            // FIXME support for multiple variable declarations ??
            Object fragment = field.fragments().get(0);
            if (fragment instanceof VariableDeclarationFragment) {
                adapterField.setName(((VariableDeclarationFragment) fragment).getName().getIdentifier());
            }

            for (Object object : field.modifiers()) {
                if (object instanceof Annotation) {

                    Name typeName = ((Annotation) object).getTypeName();
                    adapterField.addAnnotation(resolveType(typeName.getFullyQualifiedName()));
                }
            }
            javaClass.addField(adapterField);
        }

        for (MethodDeclaration method : node.getMethods()) {

            AdapterMethod adapterMethod = new AdapterMethod();

            adapterMethod.setQualifiedType(resolveType(method.getReturnType2()));
            adapterMethod.setName(method.getName().getIdentifier());

            for (Object object : method.modifiers()) {
                if (object instanceof Annotation) {

                    Name typeName = ((Annotation) object).getTypeName();
                    adapterMethod.addAnnotation(resolveType(typeName.getFullyQualifiedName()));
                }
            }

            for (Object parameter : method.parameters()) {

                if (parameter instanceof SingleVariableDeclaration) {

                    SingleVariableDeclaration parameterDeclaration = (SingleVariableDeclaration) parameter;
                    AdapterParameter adapterParameter = new AdapterParameter();

                    adapterParameter.setQualifiedType(resolveType(parameterDeclaration.getType()));

                    adapterParameter.setName(parameterDeclaration.getName().getIdentifier());

                    for (Object object : parameterDeclaration.modifiers()) {
                        if (object instanceof Annotation) {

                            Name typeName = ((Annotation) object).getTypeName();
                            adapterParameter.addAnnotation(resolveType(typeName.getFullyQualifiedName()));
                        }
                    }
                    adapterMethod.addParameter(adapterParameter);
                }
            }

            javaClass.addMethod(adapterMethod);
        }
        classData.add(javaClass);

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
        importList.add(new AdapterImport(node.getName().getFullyQualifiedName()));
    }

    public void endVisit(PackageDeclaration node) {
        packageName = node.getName().getFullyQualifiedName();
    }

    public void endVisit(final TypeDeclaration node) {
        nestedClasses.remove(nestedClasses.size() - 1);
    }

    public AdapterJavaClass[] getClassData() {
        return classData.toArray(new AdapterJavaClass[classData.size()]);
    }


    private boolean hasImport(final String type) {
        return getImport(type) != null;
    }


    private Import getImport(final String className) {
        for (Import imprt : importList) {
            if (imprt.getQualifiedName().equals(className) || imprt.getSimpleName().equals(className)) {
                return imprt;
            }
        }
        return null;
    }

    private String resolveType(final Type type) {
        return resolveType(JDTHelper.getTypeName(type));
    }

    private String resolveType(final String type) {
        String original = type;
        String result = type;

        if (Types.isPrimitive(result) || "void".equals(type)) {
            return result;
        }

        // Strip away any characters that might hinder the type matching process
        if (Types.isArray(result)) {
            original = Types.stripArray(result);
            result = Types.stripArray(result);
        }

        if (Types.isGeneric(result)) {
            original = Types.stripGenerics(result);
            result = Types.stripGenerics(result);
        }

        // Check for direct import matches first since they are the fastest and least work-intensive
        if (Types.isSimpleName(result)) {
            if (!hasImport(type) && Types.isJavaLang(type)) {
                result = "java.lang." + result;
            }

            if (result.equals(original)) {
                for (Import imprt : importList) {
                    if (Types.areEquivalent(result, imprt.getQualifiedName())) {
                        result = imprt.getQualifiedName();
                        break;
                    }
                }
            }
        }

        // If we didn't match any imports directly, we might have a wild-card/on-demand import.
        if (Types.isSimpleName(result)) {
            for (Import imprt : importList) {
                if (imprt.isWildcard()) {
                    // TODO warn if no wild-card resolvers are configured
                    // TODO Test wild-card/on-demand import resolving
                    for (WildcardImportResolver r : getImportResolvers()) {
                        result = r.resolve(null, result);
                        if (Types.isQualified(result)) break;
                    }
                }
            }
        }

        // No import matches and no wild-card/on-demand import matches means this class is in the same package.
        if (Types.isSimpleName(result)) {
            if (packageName != null) {
                result = packageName + "." + result;
            }
        }

        return result;
    }

    private List<WildcardImportResolver> getImportResolvers() {
        if (resolvers == null) {
            resolvers = new ArrayList<WildcardImportResolver>();
            for (WildcardImportResolver r : loader) {
                resolvers.add(r);
            }
        }
        if (resolvers.size() == 0) {
            throw new IllegalStateException("No instances of [" + WildcardImportResolver.class.getName() + "] were " +
                    "found on the classpath.");
        }
        return resolvers;
    }

}