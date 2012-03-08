package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;

import java.lang.annotation.Inherited;
import java.util.List;

/**
 * scans for class's annotations, where @Retention(RetentionPolicy.RUNTIME)
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
@SuppressWarnings({"unchecked"})
public class TypeAnnotationsScanner extends AbstractScanner {
    public void scan(final Object cls[], final MetaDataAdapter metaDataAdapter) {
        for (Object classData : cls) {
            final String className = metaDataAdapter.getClassName(classData);

            for (String annotationType : (List<String>) metaDataAdapter.getClassAnnotationNames(classData)) {

                if (acceptResult(annotationType) || annotationType.equals(Inherited.class.getName())) { //as an
                    // exception, accept Inherited as well
                    metaDataAdapter.getDataStoreEntryBuilder(this).setKey(annotationType).setQualifiedType(className)
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
