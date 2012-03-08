package org.apache.deltaspike.forge;

import com.google.common.base.CaseFormat;
import org.apache.deltaspike.forge.deactivatable.DeactivatableTokenCompleter;
import org.apache.deltaspike.forge.helper.MavenHelper;
import org.apache.deltaspike.forge.helper.overview.JavaMetaData;
import org.apache.deltaspike.forge.helper.overview.query.MetaDataQuery;
import org.apache.deltaspike.forge.helper.overview.scanner.*;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.util.ConfigurationBuilder;
import org.apache.deltaspike.forge.projectstage.ProjectStage;
import org.apache.deltaspike.forge.projectstage.ProjectStageTokenCompleter;
import org.apache.deltaspike.forge.util.FacetsUtil;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.maven.facets.MavenResourceFacet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.events.InitProject;
import org.jboss.forge.shell.plugins.*;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Set;

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
        // TODO Verify if the projectStageName is a valid one.
        ProjectStage.defineProjectStageWithProperties(project, projectStageName);
    }

    @Command("verify-project-stage")
    public void verifyProjectStage() {

        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new ResourcesScanner(),
                new FieldAnnotationsScanner(), new MethodAnnotationsScanner(), new MethodParameterAnnotationsScanner
                (), new MethodTypeAnnotationScanner());

        JavaSourceFacet sourceFacet = FacetsUtil.tryToGetFacet(project, JavaSourceFacet.class);
        MavenResourceFacet resourceFacet = FacetsUtil.tryToGetFacet(project, MavenResourceFacet.class);
        try {

            builder.addUrls(sourceFacet.getSourceFolder().getUnderlyingResourceObject().getAbsoluteFile().toURI()
                    .toURL());
            builder.addUrls(resourceFacet.getResourceFolder().getUnderlyingResourceObject().getAbsoluteFile().toURI()
                    .toURL());



            for (File f : MavenHelper.getInstance(project).getBuildClassPath()) {
                builder.addUrls(f.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            // FIXME
            e.printStackTrace();
        }

        JavaMetaData metaData = new JavaMetaData(builder);

        Set<String> typesOf = MetaDataQuery.getInstance(metaData).getSubTypesOf("org.apache.deltaspike.core.spi.activation.Deactivatable");
        System.out.println(typesOf);

        Collection<DataStoreEntry> serviceFiles = MetaDataQuery.getInstance(metaData).getServiceFiles();
        for (DataStoreEntry dataStoreEntry : serviceFiles) {
            System.out.println(dataStoreEntry.getRelativePath());
            System.out.println("----- " + dataStoreEntry.getFileContents());
        }

        /*
        ProjectOverview projectOverview = new ProjectOverview(project);
        List<ClassInformation> informationList = projectOverview.getClassInformationList();
        for (ClassInformation classInformation : informationList) {
            System.out.println(classInformation.getSimpleName() + " / " + classInformation.getQualifiedName());
            System.out.println("   super type = " + classInformation.getSuperType());
            for (String interfaceName : classInformation.getInterfaceTypes()) {
                System.out.println("   interface = " + interfaceName);
            }
        }
        */

    }

    @Command("deactivate")
    public void deactivateClass(@Option(name = "name", completer = DeactivatableTokenCompleter.class) final String
                                            deactivatableName, final PipeOut out) {

        System.out.println("deactivate " + deactivatableName);

    }

    public void changeProject(@Observes InitProject projectChanged) {
        System.out.println("[DetaSpikePlugin] Changed the project !! ");
        // Here we need to check if the project has DeltaSpike facet installed.
        // If so execute this
        // JavaParser.parse("package foo\nimport javax.enterprise.inject.Typed\n @Typed\npublic class test {}");
        // This to 'power up' the Eclipse Java Parser so that the actual parsing goes very fast.

    }
}
