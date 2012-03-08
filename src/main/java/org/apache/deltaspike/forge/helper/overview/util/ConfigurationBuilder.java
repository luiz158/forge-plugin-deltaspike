package org.apache.deltaspike.forge.helper.overview.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import org.apache.deltaspike.forge.helper.overview.Configuration;
import org.apache.deltaspike.forge.helper.overview.scanner.Scanner;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

/**
 * a fluent builder for {@link org.apache.deltaspike.forge.helper.overview.Configuration},
 * to be used for constructing a {@link org.apache.deltaspike.forge.helper.overview.JavaMetaData} instance
 * <p>usage:
 * <pre>
 *      new Reflections(
 *          new ConfigurationBuilder()
 *              .filterInputsBy(new FilterBuilder().include("your project's common package prefix here..."))
 *              .setUrls(ClasspathHelper.forClassLoader())
 *              .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner().filterResultsBy
 *              (myClassAnnotationsFilter)));
 * </pre>
 * <p>defaults: accept all for {@link #inputsFilter},
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
@SuppressWarnings({"RawUseOfParameterizedType"})
public class ConfigurationBuilder implements Configuration {
    private final Set<Scanner> scanners = Sets.<Scanner>newHashSet();

    private Set<URL> urls = Sets.newHashSet();

    private Predicate<String> inputsFilter = Predicates.alwaysTrue();

    public Set<Scanner> getScanners() {
        return scanners;
    }

    /**
     * set the scanners instances for scanning different metadata
     *
     * @param scanners The scanners to be used by the configuration
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder setScanners(final Scanner... scanners) {
        this.scanners.clear();
        return addScanners(scanners);
    }

    /**
     * set the scanners instances for scanning different metadata.
     *
     * @param scanners The scanners to add to the configuration
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder addScanners(final Scanner... scanners) {
        this.scanners.addAll(Sets.newHashSet(scanners));
        return this;
    }

    public Set<URL> getUrls() {
        return urls;
    }

    /**
     * set the urls to be scanned
     *
     * @param urls theURLs to be used by the configuration
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder setUrls(final URL... urls) {
        this.urls = Sets.newHashSet(urls);
        return this;
    }


    /**
     * add urls to be scanned
     *
     * @param urls The URLs to add to the configuration
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder addUrls(final Collection<URL> urls) {
        this.urls.addAll(urls);
        return this;
    }

    /**
     * add urls to be scanned
     *
     * @param urls The URLs to add to the configuration
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder addUrls(final URL... urls) {
        this.urls.addAll(Sets.newHashSet(urls));
        return this;
    }

    public boolean acceptsInput(String inputFqn) {
        return inputsFilter.apply(inputFqn);
    }

    /**
     * sets the input filter for all resources to be scanned
     * <p> supply a {@link com.google.common.base.Predicate} or use the {@link FilterBuilder}
     *
     * @param inputsFilter The filter to keep the interesting parts.
     * @return this builder for the fluent api.
     */
    public ConfigurationBuilder filterInputsBy(Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
        return this;
    }

}
