package org.apache.deltaspike.forge.helper.overview.util;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.apache.deltaspike.forge.helper.overview.JavaMetaDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * an include exclude filter builder
 * <p>for example:
 * <pre>
 * Predicate<String> filter1 = FilterBuilder.parse("+.*, -java.*");
 * Predicate<String> filter2 = new FilterBuilder().include(".*").exclude("java.*");
 * </pre>
 * Taken from by http://code.google.com/p/reflections/
 */
public class FilterBuilder implements Predicate<String> {
    private final List<Predicate<String>> chain;

    public FilterBuilder() {
        chain = Lists.newArrayList();
    }

    private FilterBuilder(final Iterable<Predicate<String>> filters) {
        chain = Lists.newArrayList(filters);
    }

    /**
     * include a regular expression
     *
     * @param regex Regeul expression for thing to keep.
     * @return this builder for the fluent api.
     */
    public FilterBuilder include(final String regex) {
        return add(new Include(regex));
    }

    /**
     * exclude a regular expression
     *
     * @param regex Regular expression for things to reject.
     * @return this builder for the fluent api.
     */
    public FilterBuilder exclude(final String regex) {
        add(new Exclude(regex));
        return this;
    }

    /**
     * add a Predicate to the chain of predicates
     *
     * @param filter When resource matches this predicate it is kept.
     * @return this builder for the fluent api.
     */
    public FilterBuilder add(Predicate<String> filter) {
        chain.add(filter);
        return this;
    }

    /**
     * include a package of a given class
     *
     * @param aClass Will add the package of this class as a predicate
     * @return this builder for the fluent api.
     */
    public FilterBuilder includePackage(final Class<?> aClass) {
        return add(new Include(packageNameRegex(aClass)));
    }

    /**
     * exclude a package of a given class
     *
     * @param aClass Will add the package of this class as a predicate
     * @return this builder for the fluent api.
     */
    public FilterBuilder excludePackage(final Class<?> aClass) {
        return add(new Exclude(packageNameRegex(aClass)));
    }

    private static String packageNameRegex(Class<?> aClass) {
        return prefix(aClass.getPackage().getName() + ".");
    }

    public static String prefix(String qualifiedName) {
        return qualifiedName.replace(".", "\\.") + ".*";
    }

    @Override
    public String toString() {
        return Joiner.on(", ").join(chain);
    }

    public boolean apply(String regex) {
        boolean accept = chain == null || chain.isEmpty() || chain.get(0) instanceof Exclude;

        if (chain != null) {
            for (Predicate<String> filter : chain) {
                if (accept && filter instanceof Include) {
                    continue;
                } //skip if this filter won't change
                if (!accept && filter instanceof Exclude) {
                    continue;
                }
                accept = filter.apply(regex);
            }
        }
        return accept;
    }

    /**
     * parses a string representation of include exclude filter
     * <p>the given includeExcludeString is a comma separated list of patterns, each starts with either + or - to
     * indicate include/exclude resp.
     * followed by the regular expression pattern
     * <p>for example parse("-java., -javax., -sun., -com.sun.") or parse("+com.myn,-com.myn.excluded")
     *
     * @param includeExcludeString The string with the DSL of the filter.
     * @return this builder for the fluent api.
     */
    public static FilterBuilder parse(String includeExcludeString) {
        List<Predicate<String>> filters = new ArrayList<Predicate<String>>();

        if (!Utils.isEmpty(includeExcludeString)) {
            for (String string : includeExcludeString.split(",")) {
                String trimmed = string.trim();
                char prefix = trimmed.charAt(0);
                String pattern = trimmed.substring(1);

                Predicate<String> filter;
                switch (prefix) {
                    case '+':
                        filter = new Include(pattern);
                        break;
                    case '-':
                        filter = new Exclude(pattern);
                        break;
                    default:
                        throw new JavaMetaDataException("includeExclude should start with either + or -");
                }

                filters.add(filter);
            }

            return new FilterBuilder(filters);
        } else {
            return new FilterBuilder();
        }
    }

    public abstract static class Matcher extends FilterBuilder {
        private final Pattern pattern;

        public Matcher(final String regex) {
            pattern = Pattern.compile(regex);
        }

        @Override
        public abstract boolean apply(String regex);

        protected Pattern getPattern() {
            return pattern;
        }

        @Override
        public String toString() {
            return pattern.pattern();
        }
    }

    public static class Include extends Matcher {
        public Include(final String patternString) {
            super(patternString);
        }

        @Override
        public boolean apply(final String regex) {
            return getPattern().matcher(regex).matches();
        }

        @Override
        public String toString() {
            return "+" + super.toString();
        }
    }

    public static class Exclude extends Matcher {
        public Exclude(final String patternString) {
            super(patternString);
        }

        @Override
        public boolean apply(final String regex) {
            return !getPattern().matcher(regex).matches();
        }

        @Override
        public String toString() {
            return "-" + getPattern().pattern();
        }
    }

}
