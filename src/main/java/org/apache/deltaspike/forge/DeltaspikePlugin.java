package org.apache.deltaspike.forge;

import com.google.common.base.CaseFormat;
import org.apache.deltaspike.forge.overview.ClassInformation;
import org.apache.deltaspike.forge.overview.ProjectOverview;
import org.apache.deltaspike.forge.projectstage.ProjectStage;
import org.apache.deltaspike.forge.projectstage.ProjectStageTokenCompleter;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Rudy De Busscher
 */
@Alias("deltaspike")
@RequiresProject
@RequiresFacet(MavenCoreFacet.class)
public class DeltaspikePlugin implements Plugin {

    @Inject
    private ShellPrompt prompt;

    private final Project project;

    private final Event<InstallFacets> installFacets;

    @Inject
    public DeltaspikePlugin(final Project someProject, final Event<InstallFacets> someEvent) {
        project = someProject;
        installFacets = someEvent;
    }

    @DefaultCommand
    public void defaultCommand(@PipeIn String in, PipeOut out) {
        // FIXME Give some meaningful things that can be done.
        out.println("Welcome to the DeltaSpike forge plugin.  Please recheck later when the functionality is " +
                "implemented.");
    }

    @Command("setup")
    public void setup(final PipeOut out) {
        if (!project.hasFacet(DeltaspikeFacet.class)) {
            installFacets.fire(new InstallFacets(DeltaspikeFacet.class));
        }
        if (project.hasFacet(DeltaspikeFacet.class)) {
            ShellMessages.success(out, "DeltaspikeFacet is configured.");
        }
    }

    @Command("create-custom-project-stage")
    public void createCustomProjectStage(@Option(name = "name") final String customProjectStageName,
                                         final PipeOut out) {

        String javaName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, customProjectStageName);
        // TODO Check if the javaName is valid name for a java class.
        // TODO Verify if the filename doesn't exist already.
        ProjectStage.createCustomProjectStage(project, javaName);
    }

    @Command("define-project-stage")
    public void defineProjectStageWithProperties(@Option(name = "name", completer = ProjectStageTokenCompleter.class)
                                                     final String projectStageName, final PipeOut out) {
        // TODO Verifiy if the projectStageName is a valid one.
        ProjectStage.defineProjectStageWithProperties(project, projectStageName);
    }

    @Command("verify-project-stage")
    public void verifyProjectStage() {

        ProjectOverview projectOverview = new ProjectOverview(project);
        List<ClassInformation> informationList = projectOverview.getClassInformationList();
        for (ClassInformation classInformation : informationList) {
            System.out.println(classInformation.getSimpleName() + " / " + classInformation.getQualifiedName());
            System.out.println("   super type = " + classInformation.getSuperType());
            for (String interfaceName : classInformation.getInterfaceTypes()) {
                System.out.println("   interface = " + interfaceName);
            }
        }
    }

}
