package org.apache.deltaspike.forge.overview;

import org.apache.deltaspike.forge.util.FacetsUtil;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class DefinedServices {

    private DefinedServices() {
    }

    public static Map<String, List<String>> loadServiceInformation(Project project) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        ResourceFacet resourceFacet = FacetsUtil.tryToGetFacet(project, ResourceFacet.class);
        DirectoryResource services = resourceFacet.getResourceFolder().getChildDirectory("META-INF")
                .getChildDirectory("services");

        List<Resource<?>> resources = services.listResources();
        for (Resource res : resources) {

            result.put(res.getName(), loadServiceFileContents(res));

        }
        return result;
    }

    private static List<String> loadServiceFileContents(Resource someServiceFile) {
        List<String> result = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(someServiceFile.getResourceInputStream()));
        try {
            String line = reader.readLine();
            while (line != null) {
                result.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
        }
        return result;
    }
}
