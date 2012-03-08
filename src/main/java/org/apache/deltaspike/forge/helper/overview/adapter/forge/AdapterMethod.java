package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about java class. This is a kind of Transfer object.
 * Idea was to implement org.jboss.forge.parser.java.Method but too much unused methods.
 *
 * @author Rudy De Busscher
 */

public class AdapterMethod {

    private String name;

    private List<AdapterTypeName> annotations = new ArrayList<AdapterTypeName>();

    private String qualifiedType;

    private List<AdapterParameter> parameters = new ArrayList<AdapterParameter>();

    public String getName() {
        return name;
    }

    public void setName(String someName) {
        name = someName;
    }

    public String getQualifiedType() {
        return qualifiedType;
    }

    public void setQualifiedType(String someQualifiedType) {
        qualifiedType = someQualifiedType;
    }

    public void addAnnotation(String className) {
        AdapterTypeName annotation = new AdapterTypeName(className);
        annotations.add(annotation);
    }

    public List<AdapterTypeName> getAnnotations() {
        return annotations;
    }

    public void addParameter(AdapterParameter parameter) {
        parameters.add(parameter);
    }

    public List<AdapterParameter> getParameters() {
        return parameters;
    }
}
