package org.apache.deltaspike.forge.helper.overview.store;

/**
 * Information about a field that will be stored into the datastore.  (This in contrast to the AdapterField which is
 * related to the MetaDataAdapter.
 *
 * @author Rudy De Busscher
 */
public class StoreEntryField {

    private String name;

    private String type;

    public StoreEntryField(String someName, String someType) {
        name = someName;
        type = someType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
