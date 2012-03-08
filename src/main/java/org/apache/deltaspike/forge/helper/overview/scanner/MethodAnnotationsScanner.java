package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;

import java.util.List;

/**
 * scans for method's annotations
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
public class MethodAnnotationsScanner extends AbstractScanner {
    public void scan(final Object cls[], final MetaDataAdapter metaDataAdapter) {
        for (Object classData : cls) {
            for (Object method : metaDataAdapter.getMethods(classData)) {
                for (String methodAnnotation : (List<String>) metaDataAdapter.getMethodAnnotationNames(method)) {
                    if (acceptResult(methodAnnotation)) {
                        metaDataAdapter.getDataStoreEntryBuilder(this).setKey(methodAnnotation).setClass(classData)
                                .setMethod(method).storeEntry();
                    }
                }
            }
        }
    }

    @Override
    public String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose) {
        String result;
        if (verbose) {
            result = String.format("%s %s#%s()", dataStoreEntry.getMethod().getReturnType(),
                    dataStoreEntry.getJavaClass().getQualifiedName(), dataStoreEntry.getMethod().getName());
        } else {
            result = String.format("%s#%s", dataStoreEntry.getJavaClass().getQualifiedName(),
                    dataStoreEntry.getMethod().getName());
        }
        return result;
    }
}
