package org.apache.deltaspike.forge.util;

import org.jboss.forge.project.dependencies.DependencyBuilder;

import java.util.List;

/**
 * @author Rudy De Busscher
 */
public class DeltaspikeDependency {

    private static final String MODULE_GROUP_ID = "org.apache.deltaspike.";

    private String description;

    private DependencyBuilder api;

    private DependencyBuilder impl;

    private List<DeltaspikeDependency> subDependencyList;

    public DeltaspikeDependency(String description, String moduleName, String apiArtifactId, String implArtifactId) {
        this(description, DependencyBuilder.create().setGroupId(MODULE_GROUP_ID + moduleName).setArtifactId
                (apiArtifactId), DependencyBuilder.create().setGroupId(MODULE_GROUP_ID + moduleName).setArtifactId
                (implArtifactId));
    }

    public DeltaspikeDependency(String description, DependencyBuilder api, DependencyBuilder impl) {
        this.description = description;
        this.api = api;
        this.impl = impl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DependencyBuilder getApi() {
        return api;
    }

    public DependencyBuilder getImpl() {
        return impl;
    }

    public List<DeltaspikeDependency> getSubDependencyList() {
        return subDependencyList;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
