package org.apache.deltaspike.forge;

import org.apache.deltaspike.forge.util.DeltaspikeDependency;

/**
 * @author Rudy De Busscher
 */
public enum DeltaspikeModule {
    CORE(new DeltaspikeDependency("Deltaspike Core", "core", "deltaspike-core-api", "deltaspike-core-impl"));

    // It is no problem that this property isn't serializable.
    private DeltaspikeDependency dependency;

    DeltaspikeModule(DeltaspikeDependency someDependency) {
        dependency = someDependency;
    }

    public DeltaspikeDependency getDependency() {
        return dependency;
    }

    @Override
    public String toString() {
        return dependency.getDescription();
    }
}
