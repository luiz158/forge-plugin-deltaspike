package org.apache.deltaspike.forge.helper.overview.store;

import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.scanner.AbstractScanner;

/**
 * Builds a DataStoreEntry because an entry is basically read-only. It retrieves the required information from the
 * MetaDataAdapter.
 * Entries are stored based on the scanner (because they define what information is available) in the entry.
 *
 * @author Rudy De Busscher
 */
public class DataStoreEntryBuilder extends AbstractDataStoreEntry {

    private AbstractScanner scanner;

    private MetaDataAdapter metaDataAdapter;

    private String key;

    private DataStoreEntryBuilder(AbstractScanner someScanner, MetaDataAdapter someMetaDataAdapter) {
        scanner = someScanner;
        metaDataAdapter = someMetaDataAdapter;
    }

    public static DataStoreEntryBuilder getInstance(AbstractScanner someScanner, MetaDataAdapter someMetaDataAdapter) {
        return new DataStoreEntryBuilder(someScanner, someMetaDataAdapter);
    }

    public DataStoreEntryBuilder setKey(String someKey) {
        key = someKey;
        return this;
    }

    public DataStoreEntryBuilder setQualifiedType(String someClassName) {
        setQualifiedTypeName(someClassName);
        return this;
    }

    public DataStoreEntryBuilder setFileRelativePath(String somePathName) {
        setRelativePath(somePathName);
        return this;
    }

    public DataStoreEntryBuilder setResourceContent(String someContents) {
        setFileContent(someContents);
        return this;
    }


    public void storeEntry() {
        if (key != null && key.trim().length() > 0) {
            // With ResourceScanner, keys can be "" for directories.
            scanner.getStore().put(key, this);
        }
    }

    public DataStoreEntryBuilder setClass(Object someClassData) {

        String qualifiedClassName = metaDataAdapter.getClassName(someClassData);
        int pos = qualifiedClassName.lastIndexOf(".");
        setJavaClassValue(new StoreEntryJavaClass(qualifiedClassName.substring(0, pos),
                qualifiedClassName.substring(pos + 1)));
        return this;
    }

    public DataStoreEntryBuilder setMethod(Object someMethodData) {
        setMethodData(new StoreEntryMethod(metaDataAdapter.getMethodName(someMethodData),
                metaDataAdapter.getReturnTypeName(someMethodData)));
        return this;
    }

    public DataStoreEntryBuilder setField(Object someFieldData) {

        setFieldData(new StoreEntryField(metaDataAdapter.getFieldName(someFieldData),
                metaDataAdapter.getFieldType(someFieldData)));
        return this;
    }

    public DataStoreEntryBuilder setMethodParameter(Object someParameterData) {

        setMethodParameterData(new StoreEntryMethodParameter(metaDataAdapter.getParameterName(someParameterData),
                metaDataAdapter.getMethodParameterType(someParameterData)));
        return this;

    }

    @Override
    public String toStringRepresentation() {
        return toStringRepresentation(false);
    }

    @Override
    public String toStringRepresentation(boolean verbose) {
        return scanner.toStringRepresentation(this, verbose);
    }

}

