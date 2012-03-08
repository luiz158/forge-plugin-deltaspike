package org.apache.deltaspike.forge.helper.overview.scanner;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import org.apache.deltaspike.forge.helper.overview.JavaMetaDataException;
import org.apache.deltaspike.forge.helper.overview.adapter.ForgeJavaParserAdapter;
import org.apache.deltaspike.forge.helper.overview.adapter.JavassistAdapter;
import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;

/**
 * This implements the Scanner interface with the common functionality for all scanners.  It also decides which
 * MetaDataAdapter will be used, the one for source or the compiled java.
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
public abstract class AbstractScanner implements Scanner {

    private static MetaDataAdapter binaryMetaDataAdapter = new JavassistAdapter();

    private static MetaDataAdapter sourceMetaDataAdapter = new ForgeJavaParserAdapter();

    public static final String JAVA_CLASS_FILE_EXTENSION = ".class";

    public static final String JAVA_SOURCE_FILE_EXTENSION = ".java";

    private Multimap<String, DataStoreEntry> store;

    private Predicate<String> resultFilter = Predicates.alwaysTrue(); //accept all by default

    public boolean acceptsInput(String file) {
        return file.endsWith(JAVA_CLASS_FILE_EXTENSION) || file.endsWith(JAVA_SOURCE_FILE_EXTENSION); //is a class
        // file (binary or source)
    }

    public void scan(Vfs.File file) {
        try {
            MetaDataAdapter adapter = getMetadataAdapter(file);
            final Object[] classObject = adapter.getOrCreateClassObject(file);
            scan(classObject, adapter);
        } catch (Exception e) {
            throw new JavaMetaDataException("could not create class file from " + file.getName(), e);
        }
    }

    public abstract void scan(Object[] cls, MetaDataAdapter metaDataAdapter);

    public Multimap<String, DataStoreEntry> getStore() {
        return store;
    }

    public void setStore(final Multimap<String, DataStoreEntry> store) {
        this.store = store;
    }

    public Predicate<String> getResultFilter() {
        return resultFilter;
    }

    public void setResultFilter(Predicate<String> resultFilter) {
        this.resultFilter = resultFilter;
    }

    public Scanner filterResultsBy(Predicate<String> filter) {
        this.setResultFilter(filter);
        return this;
    }

    //
    public boolean acceptResult(final String fqn) {
        return fqn != null && resultFilter.apply(fqn);
    }

    protected MetaDataAdapter getMetadataAdapter(Vfs.File somefile) {
        MetaDataAdapter result = null;

        if (somefile.getName().endsWith(JAVA_CLASS_FILE_EXTENSION)) {
            result = binaryMetaDataAdapter;
        }
        if (somefile.getName().endsWith(JAVA_SOURCE_FILE_EXTENSION)) {
            result = sourceMetaDataAdapter;
        }
        // FIXME Throw exception when no adapter found

        return result;
    }

    //
    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
