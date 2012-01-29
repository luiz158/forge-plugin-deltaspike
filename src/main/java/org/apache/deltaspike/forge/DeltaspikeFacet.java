package org.apache.deltaspike.forge;

import org.apache.deltaspike.forge.util.DeltaspikeDependency;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.ScopeType;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrintWriter;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.spec.javaee.CDIFacet;

import javax.inject.Inject;

/**
 * @author Rudy De Busscher
 */
@Alias("org.apache.deltaspike")
@RequiresFacet({DependencyFacet.class, CDIFacet.class})
public class DeltaspikeFacet extends BaseFacet {

    @Inject
    private ShellPrintWriter writer;

    @Override
    public boolean install() {
        DependencyFacet deps = getProject().getFacet(DependencyFacet.class);

        installDependency(deps, DeltaspikeModule.CORE.getDependency());

        return true;
    }

    @Override
    public boolean isInstalled() {
        DependencyFacet deps = getProject().getFacet(DependencyFacet.class);


        return deps.hasEffectiveDependency(DeltaspikeModule.CORE.getDependency().getApi()) &&
                deps.hasEffectiveDependency(DeltaspikeModule.CORE.getDependency().getImpl());

    }

    private void installDependency(DependencyFacet deps, DeltaspikeDependency dependency) {

        DependencyBuilder api = dependency.getApi();
        DependencyBuilder impl = dependency.getImpl();

        writer.println();

        Dependency apiVersion = api.setVersion(DeltaspikeModule.VERSION);

        deps.addDirectDependency(apiVersion);

        if (impl != null) {
            deps.addDirectDependency(impl.setVersion(DeltaspikeModule.VERSION).setScopeType(ScopeType.RUNTIME));
        }

        ShellMessages.info(writer, "'" + dependency + "' added to dependencies list");
    }


}
