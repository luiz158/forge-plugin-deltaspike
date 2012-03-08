package org.apache.deltaspike.forge.helper.overview.store;

/**
 * The methods that are available on the entry. Depending on the scanner, the information that is available can be
 * different.
 *
 * @author Rudy De Busscher
 */
public interface DataStoreEntry {
    StoreEntryField getField();

    String getFileContents();

    StoreEntryJavaClass getJavaClass();

    StoreEntryMethod getMethod();

    StoreEntryMethodParameter getMethodParameter();

    String getQualifiedTypeName();

    String getRelativePath();

    String toStringRepresentation();

    String toStringRepresentation(boolean verbose);
}
