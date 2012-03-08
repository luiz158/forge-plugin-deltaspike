package org.apache.deltaspike.forge.helper.overview.adapter.forge;

/**
 * Holds information about a java type. This is a kind of Transfer object.
 *
 * @author Rudy De Busscher
 */

public class AdapterTypeName {

    private String qualifiedName;

    private String simpleName;

    public AdapterTypeName(String someQualifiedName) {
        qualifiedName = someQualifiedName;
        String[] nameParts = someQualifiedName.split("\\.");
        simpleName = nameParts[nameParts.length - 1];
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getSimpleName() {
        return simpleName;
    }
}
