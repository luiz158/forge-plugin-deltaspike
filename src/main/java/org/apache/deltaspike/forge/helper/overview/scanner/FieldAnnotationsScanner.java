package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;

import java.util.List;

/**
 * scans for field's annotations
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
@SuppressWarnings({"unchecked"})
public class FieldAnnotationsScanner extends AbstractScanner {
    public void scan(final Object cls[], final MetaDataAdapter metaDataAdapter) {
        for (Object classData : cls) {
            List<Object> fields = metaDataAdapter.getFields(classData);
            for (final Object field : fields) {
                List<String> fieldAnnotations = metaDataAdapter.getFieldAnnotationNames(field);
                for (String fieldAnnotation : fieldAnnotations) {

                    if (acceptResult(fieldAnnotation)) {
                        metaDataAdapter.getDataStoreEntryBuilder(this).setKey(fieldAnnotation).setClass(classData)
                                .setField(field).storeEntry();

                    }
                }
            }
        }
    }

    @Override
    public String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose) {
        String result;
        if (verbose) {
            result = String.format("%s %s.%s", dataStoreEntry.getField().getType(),
                    dataStoreEntry.getJavaClass().getQualifiedName(), dataStoreEntry.getField().getName());
        } else {
            result = String.format("%s.%s", dataStoreEntry.getJavaClass().getQualifiedName(), dataStoreEntry.getField().getName());
        }
        return result;
    }
}