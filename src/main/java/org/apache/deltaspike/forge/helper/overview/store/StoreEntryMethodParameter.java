package org.apache.deltaspike.forge.helper.overview.store;

/**
 * Information about a method parameter that will be stored into the datastore.  (This in contrast to the
 * AdapterParameter which is related to the MetaDataAdapter.
 *
 * @author Rudy De Busscher
 */
public class StoreEntryMethodParameter {

    private String name;

    private String qualifiedType;

    public StoreEntryMethodParameter(String someName, String someQualifiedType) {
        name = someName;
        qualifiedType = someQualifiedType;
    }

    public String getName() {
        return name;
    }

    public String getQualifiedType() {
        return qualifiedType;
    }
}
