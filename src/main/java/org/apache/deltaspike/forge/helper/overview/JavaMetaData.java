package org.apache.deltaspike.forge.helper.overview;

import org.apache.deltaspike.forge.helper.overview.scanner.Scanner;
import org.apache.deltaspike.forge.helper.overview.store.DataStore;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inspired by http://code.google.com/p/reflections/
 * @author Rudy De Busscher
 */
public class JavaMetaData {

    private Logger log = Logger.getLogger(JavaMetaData.class.getName());

    private final transient Configuration configuration;

    private DataStore dataStore;

    /**
     * constructs a Reflections instance and scan according to given {@link Configuration}
     * <p>it is preferred to use {@link org.apache.deltaspike.forge.helper.overview.util.ConfigurationBuilder}
     *
     * @param someConfiguration The configuration for scanning.
     */
    public JavaMetaData(final Configuration someConfiguration) {
        configuration = someConfiguration;
        dataStore = new DataStore();

        if (someConfiguration.getScanners() != null && !someConfiguration.getScanners().isEmpty()) {
            //inject to scanners
            for (Scanner scanner : someConfiguration.getScanners()) {
                scanner.setStore(dataStore.getOrCreate(scanner.getClass()));
            }

            scan();
        }
    }

    protected void scan() {
        if (configuration.getUrls() == null || configuration.getUrls().isEmpty()) {
            log.severe("given scan urls are empty. set urls in the configuration");
            return;
        } else {
            if (log.isLoggable(Level.FINE)) {
                StringBuilder urls = new StringBuilder();
                for (URL url : configuration.getUrls()) {
                    urls.append("\t").append(url.toExternalForm()).append("\n");
                }
                log.fine("going to scan these urls:\n" + urls);
            }
        }

        long time = System.currentTimeMillis();

        for (URL url : configuration.getUrls()) {
            try {
                for (final Vfs.File file : Vfs.fromURL(url).getFiles()) {
                    scan(file);
                }
            } catch (JavaMetaDataException e) {
                log.log(Level.SEVERE, "could not create Vfs.Dir from url. ignoring the exception and continuing", e);
            }

        }

        time = System.currentTimeMillis() - time;

        Integer keys = dataStore.getKeysCount();
        Integer values = dataStore.getValuesCount();

        log.info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values", time,
                configuration.getUrls().size(), keys, values));

    }

    private void scan(Vfs.File file) {
        String input = file.getRelativePath().replace('/', '.');
        if (configuration.acceptsInput(input)) {
            for (Scanner scanner : configuration.getScanners()) {
                try {
                    if (scanner.acceptsInput(input)) {
                        scanner.scan(file);
                    }
                } catch (Exception e) {
                    log.log(Level.WARNING, "could not scan file " + file.getFullPath() + " with scanner " + scanner
                            .getClass().getSimpleName(), e);
                }
            }
        }
    }

    /**
     * returns the dataStore used for storing and querying the metadata
     * @return The scanned information
     */
    public DataStore getDataStore() {
        return dataStore;
    }

}
