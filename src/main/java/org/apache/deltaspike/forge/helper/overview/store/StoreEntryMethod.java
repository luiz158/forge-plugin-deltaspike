package org.apache.deltaspike.forge.helper.overview.store;

/**
 * Information about a method that will be stored into the datastore.  (This in contrast to the AdapterMethod which is
 * related to the MetaDataAdapter.
 *
 * @author Rudy De Busscher
 */
public class StoreEntryMethod {

    private String name;

    private String returnType;

    public StoreEntryMethod(String someName, String someReturnType) {
        name = someName;
        returnType = someReturnType;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }
}
