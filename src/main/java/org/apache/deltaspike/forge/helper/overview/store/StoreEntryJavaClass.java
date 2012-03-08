package org.apache.deltaspike.forge.helper.overview.store;

/**
 * Information about a java class that will be stored into the datastore.  (This in contrast to the AdapterJavaClass
 * which is related to the MetaDataAdapter.
 *
 * @author Rudy De Busscher
 */
public class StoreEntryJavaClass {

    private String simpleName;

    private String packageName;

    public StoreEntryJavaClass(String somePackageName, String someSimpleName) {
        packageName = somePackageName;
        simpleName = someSimpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return packageName + "." + getSimpleName();
    }
}
