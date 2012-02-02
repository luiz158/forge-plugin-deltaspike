package org.apache.deltaspike.forge.overview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rudy De Busscher
 *         FIXME Review data structures and flow, POC
 */
public class ClassInformation {

    private String simpleName;

    private String qualifiedName;

    private String superType;

    private List<String> interfaceTypes = new ArrayList<String>();

    private List<String> importList = new ArrayList<String>();

    public List<String> getInterfaceTypes() {
        return interfaceTypes;
    }

    public void setInterfaceTypes(List<String> someInterfaceTypes) {
        interfaceTypes = someInterfaceTypes;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String someQualifiedName) {
        qualifiedName = someQualifiedName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String someSimpleName) {
        simpleName = someSimpleName;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String someSuperType) {
        superType = someSuperType;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> someImportList) {
        importList = someImportList;
    }
}
