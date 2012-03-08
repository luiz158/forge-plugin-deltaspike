package org.apache.deltaspike.forge.helper.overview.query;

import com.google.common.base.Predicate;

import java.util.regex.Pattern;

/**
 *
 */
// FIXME Is this really no longer needed?
public class PredicateResourceMatcher implements Predicate<String> {

    private Pattern pattern;

    public PredicateResourceMatcher(Pattern somePattern) {
        pattern = somePattern;
    }

    @Override
    public boolean apply(String input) {
        return pattern.matcher(input).matches();
    }

}