package org.apache.deltaspike.forge.util;

import org.jboss.forge.project.Facet;
import org.jboss.forge.project.Project;

/**
 * @author Rudy De Busscher
 */
public final class FacetsUtil {

    private FacetsUtil() {
    }
    
    public static <T extends Facet> T tryToGetFacet(Project project, Class<T> facet) {
        T result = null;
        if (project.hasFacet(facet)) {
            result = project.getFacet(facet);
        }
        return result;
    }
}
