package org.apache.deltaspike.forge.helper.overview;

import org.apache.deltaspike.forge.helper.overview.scanner.Scanner;

import java.net.URL;
import java.util.Set;

/**
 * Inspired by http://code.google.com/p/reflections/
 * <p/>
 * Configuration is used to create a configured instance of {@link JavaMetaData}
 * <p>it is preferred to use {@link org.apache.deltaspike.forge.helper.overview.util.ConfigurationBuilder}
 * @author Rudy De Busscher
 */
public interface Configuration {
    /**
     * The scanner instances used for scanning different metadata.
     *
     * @return configured scanners
     */
    Set<Scanner> getScanners();

    /**
     * The urls to be scanned.
     *
     * @return Configured URLs
     */
    Set<URL> getUrls();

    /**
     * Determines if this resources (Java (source) class or other resource) needs to be processed and retained.
     *
     * @param inputFqn A fully qualified (class) name
     * @return true if resource needs to be processed.
     */
    boolean acceptsInput(String inputFqn);

}
