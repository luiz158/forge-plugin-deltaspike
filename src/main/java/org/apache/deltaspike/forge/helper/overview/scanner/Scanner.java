package org.apache.deltaspike.forge.helper.overview.scanner;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;

/**
 * Defines how scanning of resources (java files or other resources) should be performed)
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
public interface Scanner {

    void setStore(Multimap<String, DataStoreEntry> store);

    Scanner filterResultsBy(Predicate<String> filter);

    boolean acceptsInput(String file);

    void scan(Vfs.File file);

    boolean acceptResult(String fqn);

    String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose);
}