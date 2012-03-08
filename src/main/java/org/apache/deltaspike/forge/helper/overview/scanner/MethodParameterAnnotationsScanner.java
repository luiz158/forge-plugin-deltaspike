package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;

import java.util.List;

/**
 * Scans for method parameter annotations (for things like @Observes)
 *
 * @author Rudy De Busscher
 */
public class MethodParameterAnnotationsScanner extends AbstractScanner {
    public void scan(final Object cls[], final MetaDataAdapter metaDataAdapter) {
        for (Object classData : cls) {
            for (Object method : metaDataAdapter.getMethods(classData)) {
                int idx = 0;
                for (Object methodParameter : metaDataAdapter.getParameters(method)) {
                    for (String methodParameterAnnotation : (List<String>) metaDataAdapter
                            .getParameterAnnotationNames(method, idx)) {
                        if (acceptResult(methodParameterAnnotation)) {
                            metaDataAdapter.getDataStoreEntryBuilder(this).setKey(methodParameterAnnotation).setClass
                                    (classData).setMethod(method).setMethodParameter(methodParameter).storeEntry();
                        }
                    }
                    idx++;
                }
            }
        }
    }

    @Override
    public String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose) {
        String result;
        if (verbose) {
            result = String.format("%s#%s(%s %s)", dataStoreEntry.getJavaClass().getQualifiedName(),
                    dataStoreEntry.getMethod().getName(), dataStoreEntry.getMethodParameter().getQualifiedType(),
                    dataStoreEntry.getMethodParameter().getName());
        } else {
            result = String.format("%s#%s(%s)", dataStoreEntry.getJavaClass().getQualifiedName(), dataStoreEntry.getMethod().getName(), dataStoreEntry.getMethodParameter().getName());

        }
        return result;
    }
}