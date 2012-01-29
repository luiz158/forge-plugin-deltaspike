package org.apache.deltaspike.forge.projectstage;

import com.google.common.base.CaseFormat;
import org.apache.deltaspike.forge.DeltaspikeModule;
import org.apache.deltaspike.forge.TransientDependency;
import org.apache.deltaspike.forge.helper.CompilerHelper;
import org.apache.deltaspike.forge.helper.MavenHelper;
import org.apache.deltaspike.forge.helper.SerialverHelper;
import org.apache.deltaspike.forge.template.TemplateEvaluator;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;

import javax.tools.JavaFileManager;
import java.io.File;

/**
 * @author Rudy De Busscher
 * FIXME Clean up the class.
 */
public final class ProjectStage {

    private static final String VELOCITY_TEMPLATE_NAME_CUSTOM_PROJECTSTAGE = "/template/projectStage.vm";

    private ProjectStage() {
    }

    public static void createCustomProjectStage(final Project project, final String javaClassName) {
        createJavaClass(project, javaClassName);
    }

    private static void createJavaClass(Project someProject, String someJavaClassName) {

        // FIXME split in smaller parts.

        TemplateEvaluator evaluator = TemplateEvaluator.getInstance();

        String variableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, someJavaClassName);

        JavaSourceFacet source = someProject.getFacet(JavaSourceFacet.class);
        evaluator.addParameters("basePackage", source.getBasePackage());
        evaluator.addParameters("customProjectStageName", someJavaClassName);
        evaluator.addParameters("customProjectStage", variableName);
        evaluator.addParameters("serialVersionUID", "");

        String javaSource = evaluator.evaluateToString(VELOCITY_TEMPLATE_NAME_CUSTOM_PROJECTSTAGE);


        MavenHelper mavenHelper = MavenHelper.getInstance(someProject);

        // TODO This means we have the deltaspike dependencies in local maven repository.  Do something like mvn clean
        // for those that haven't it yet.
        DependencyBuilder api = DeltaspikeModule.CORE.getDependency().getApi();
        File coreApiJar = mavenHelper.resolve(api.getGroupId(), api.getArtifactId(), DeltaspikeModule.VERSION);

        File cdiApiJar = mavenHelper.resolve(TransientDependency.CDI_API.getDependency().toCoordinates());


        Long serialver = null;
        try {
            String javaClassName = defineFullJavaClassName(someJavaClassName, source, false);
            String javaClassNameInner = defineFullJavaClassName(someJavaClassName, source, true);
            JavaFileManager fileManager = CompilerHelper.create().withDependencies(coreApiJar,
                    cdiApiJar).compile(javaClassName, javaSource);

            serialver = SerialverHelper.create(fileManager).defineFor(javaClassNameInner);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        DirectoryResource sourceRoot = source.getBasePackageResource();
        DirectoryResource projectStagePackage = sourceRoot.getOrCreateChildDirectory("projectstage");
        FileResource<?> javaFile = (FileResource<?>) projectStagePackage.getChild(someJavaClassName + "Holder.java");

        evaluator.addParameters("serialVersionUID", "private static final long serialVersionUID = " + serialver+"L;");
        javaFile.setContents(evaluator.evaluate(VELOCITY_TEMPLATE_NAME_CUSTOM_PROJECTSTAGE));

    }

    private static String defineFullJavaClassName(String someJavaName, JavaSourceFacet someSource,
                                                  boolean innerClassName) {
        StringBuilder result = new StringBuilder();
        result.append(someSource.getBasePackage()).append(".projectstage.").append(someJavaName).append("Holder");
        if (innerClassName) {
            result.append('$').append(someJavaName);
        }
        return result.toString();
    }

}
