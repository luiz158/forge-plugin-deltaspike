package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about field or method parameter. This is a kind of Transfer object.
 *
 * @author Rudy De Busscher
 */
public abstract class AdapterFieldAndParameter {
    private String name;

    private List<AdapterTypeName> annotations = new ArrayList<AdapterTypeName>();

    private String qualifiedType;

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
}
