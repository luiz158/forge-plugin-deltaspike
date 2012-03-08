package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.util.FilterBuilder;

import java.util.List;

/**
 * scans for superclass and interfaces of a class, allowing a reverse lookup for subtypes
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
public class SubTypesScanner extends AbstractScanner {

    public SubTypesScanner() {
        filterResultsBy(new FilterBuilder().exclude(Object.class.getName())); //exclude direct Object subtypes by
        // default
    }

    @SuppressWarnings({"unchecked"})
    public void scan(final Object cls[], final MetaDataAdapter metaDataAdapter) {
        for (Object classData : cls) {
            String className = metaDataAdapter.getClassName(classData);
            String superclass = metaDataAdapter.getSuperclassName(classData);

            if (acceptResult(superclass)) {
                metaDataAdapter.getDataStoreEntryBuilder(this).setKey(superclass).setQualifiedType(className)
                        .storeEntry();

            }

            for (String anInterface : (List<String>) metaDataAdapter.getInterfacesNames(classData)) {
                if (acceptResult(anInterface)) {
                    metaDataAdapter.getDataStoreEntryBuilder(this).setKey(anInterface).setQualifiedType(className)
                            .storeEntry();
                }
            }
        }
    }

    @Override
    public String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose) {
        return dataStoreEntry.getQualifiedTypeName();
    }
}
