package org.apache.deltaspike.forge;

import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.project.services.ResourceFactory;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;

public class DeltaspikePluginTest extends AbstractShellTest {

    @Inject
    private ResourceFactory factory;

    @Deployment
    public static JavaArchive getDeployment() {
        return AbstractShellTest.getDeployment().addPackages(true, DeltaspikePlugin.class.getPackage());
    }

    @Test
    public void testDefaultCommand() throws Exception {
        getShell().execute("deltaspike");
    }

    @Test
    @Ignore
    public void testCreateCustomProjectStage() throws Exception {

        // FIXME : Make this testable for all developers by preparing a directory
        DirectoryResource resource = factory.getResourceFrom(new File("c:/temp/deltaspike")).reify(DirectoryResource
                .class);

        getShell().setCurrentResource(resource);
        getShell().execute("deltaspike create-custom-project-stage --name rudy");
    }

}
