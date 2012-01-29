package org.apache.deltaspike.forge;

import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;

/**
 * @author Rudy De Busscher
 */
public enum TransientDependency {

    // Transient dependency used in serial version calculation
    CDI_API(DependencyBuilder.create().setGroupId("javax.enterprise").setArtifactId("cdi-api").setVersion( "1.0-SP4"));

    // It is no problem that this property isn't serializable.
    private Dependency dependency;

    private TransientDependency(Dependency someDependency) {
        dependency = someDependency;
    }

    public Dependency getDependency() {
        return dependency;
    }

    @Override
    public String toString() {
        return dependency.toString();
    }    
}
