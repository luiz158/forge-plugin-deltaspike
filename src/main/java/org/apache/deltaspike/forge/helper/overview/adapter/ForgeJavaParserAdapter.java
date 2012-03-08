package org.apache.deltaspike.forge.helper.overview.adapter;

import org.apache.deltaspike.forge.helper.overview.adapter.forge.*;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rudy De Busscher
 */
public class ForgeJavaParserAdapter extends MetaDataAdapter<AdapterJavaClass, AdapterField, AdapterMethod,
        AdapterParameter> {

    private static Map<Vfs.File, AdapterJavaClass[]> classFileCache = new HashMap<Vfs.File, AdapterJavaClass[]>();

    public ForgeJavaParserAdapter() {
        clearCache();
    }

    @Override
    public String getClassName(AdapterJavaClass cls) {
        return cls.getQualifiedName();
    }

    @Override
    public String getSuperclassName(AdapterJavaClass cls) {

        return cls.getSuperType();

    }

    @Override
    public List<String> getInterfacesNames(AdapterJavaClass cls) {
        List<String> result = new ArrayList<String>();
        for (AdapterTypeName interfaceName : cls.getInterfaces()) {
            result.add(interfaceName.getQualifiedName());
        }
        return result;
    }

    @Override
    public List<AdapterField> getFields(AdapterJavaClass cls) {

        return cls.getFields();
    }

    @Override
    public List<AdapterMethod> getMethods(AdapterJavaClass cls) {
        return cls.getMethods();
    }

    @Override
    public String getMethodName(AdapterMethod method) {
        return method.getName();
    }

    @Override
    public List<String> getParameterNames(AdapterMethod method) {
        List<String> result = new ArrayList<String>();
        for (AdapterParameter parameter : method.getParameters()) {
            result.add(parameter.getName());
        }
        return result;
    }

    @Override
    public List<String> getClassAnnotationNames(AdapterJavaClass cls) {
        List<String> result = new ArrayList<String>();
        for (AdapterTypeName annotation : cls.getAnnotations()) {
            result.add(annotation.getQualifiedName());
        }
        return result;
    }

    @Override
    public List<String> getFieldAnnotationNames(AdapterField field) {
        List<String> result = new ArrayList<String>();
        for (AdapterTypeName typeName : field.getAnnotations()) {
            result.add(typeName.getQualifiedName());
        }
        return result;
    }

    @Override
    public List<String> getMethodAnnotationNames(AdapterMethod method) {
        List<String> result = new ArrayList<String>();
        for (AdapterTypeName typeName : method.getAnnotations()) {
            result.add(typeName.getQualifiedName());
        }
        return result;
    }

    @Override
    public List<String> getParameterAnnotationNames(AdapterMethod method, int parameterIndex) {
        List<String> result = new ArrayList<String>();
        List<AdapterParameter> parameters = method.getParameters();
        if (parameterIndex < parameters.size()) {
            for (AdapterTypeName typeName : parameters.get(parameterIndex).getAnnotations()) {
                result.add(typeName.getQualifiedName());
            }
        }
        return result;
    }

    @Override
    public List<AdapterParameter> getParameters(AdapterMethod method) {
        return method.getParameters();
    }

    @Override
    public String getReturnTypeName(AdapterMethod method) {
        return method.getQualifiedType();
    }

    @Override
    public String getFieldName(AdapterField field) {
        return field.getName();
    }

    public String getFieldType(final AdapterField field) {
        return field.getQualifiedType();
    }

    @Override
    public String getMethodParameterType(AdapterParameter methodParameter) {
        return methodParameter.getQualifiedType();
    }

    @Override
    public String getParameterName(AdapterParameter methodParameter) {
        return methodParameter.getName();
    }

    @Override
    public AdapterJavaClass[] getOrCreateClassObject(Vfs.File file) throws Exception {
        AdapterJavaClass[] result = classFileCache.get(file);
        if (result == null) {
            JavaSource<?> javaSource = JavaParser.parse(file.openInputStream());
            CompilationUnit unit = getCompilationUnit(javaSource);
            DeltaSpikeJavaResourceVisitor resourceVisitor = new DeltaSpikeJavaResourceVisitor();
            unit.accept(resourceVisitor);
            result = resourceVisitor.getClassData();

            classFileCache.put(file, result);

        }
        return result;

    }

    public void clearCache() {
        classFileCache.clear();
    }

    private CompilationUnit getCompilationUnit(JavaSource<?> cls) {
        return (CompilationUnit) cls.getInternal();
    }
}
