package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about java class. This is a kind of Transfer object.
 * Idea was to implement org.jboss.forge.parser.java.JavaClass but too much unused methods.
 *
 * @author Rudy De Busscher
 */
public class AdapterJavaClass {

    private String simpleName;

    private String packageName;

    private String superTypeName;

    private List<AdapterTypeName> interfaces = new ArrayList<AdapterTypeName>();

    private List<AdapterTypeName> annotations = new ArrayList<AdapterTypeName>();

    private List<AdapterField> fields = new ArrayList<AdapterField>();

    private List<AdapterMethod> methods = new ArrayList<AdapterMethod>();


    public String getQualifiedName() {
        StringBuilder result = new StringBuilder();
        if (packageName != null) {
            result.append(packageName);
            result.append('.');
        }
        result.append(simpleName);
        return result.toString();
    }

    public void addAnnotation(String className) {
        AdapterTypeName annotation = new AdapterTypeName(className);
        annotations.add(annotation);
    }

    public List<AdapterTypeName> getAnnotations() {
        return annotations;
    }

    public String getSuperType() {
        return superTypeName;
    }

    public void setSuperType(String type) {
        superTypeName = type;
    }


    public void addField(AdapterField field) {
        fields.add(field);
    }

    public List<AdapterField> getFields() {
        return fields;
    }

    public List<AdapterTypeName> getInterfaces() {
        return interfaces;
    }

    public void addInterface(String type) {
        interfaces.add(new AdapterTypeName(type));

    }

    public void addMethod(AdapterMethod method) {
        methods.add(method);
    }


    public List<AdapterMethod> getMethods() {
        return methods;
    }


    public String getName() {
        return simpleName;
    }


    public void setName(String name) {
        simpleName = name;
    }


    public void setPackage(String name) {
        packageName = name;
    }

}
