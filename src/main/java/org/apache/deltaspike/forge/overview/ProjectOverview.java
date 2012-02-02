package org.apache.deltaspike.forge.overview;

import org.apache.deltaspike.forge.util.FacetsUtil;
import org.apache.deltaspike.forge.util.JavaResourceVisitor;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFilter;
import org.jboss.forge.resources.java.JavaResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rudy De Busscher
 *         FIXME Review data structures and flow, POC
 */
public class ProjectOverview {

    private Project project;


    public ProjectOverview(Project someProject) {
        project = someProject;
    }

    private List<ClassInformation> classInformationList;

    public List<ClassInformation> getClassInformationList() {
        if (classInformationList == null) {
            classInformationList = new ArrayList<ClassInformation>();
            loadClassInformation();
        }
        return classInformationList;
    }

    private void loadClassInformation() {
        JavaSourceFacet javaFacet = FacetsUtil.tryToGetFacet(project, JavaSourceFacet.class);

        visitSources(javaFacet, new DeltaSpikeJavaResourceVisitor(this));

    }

    public void visitSources(JavaSourceFacet facet, JavaResourceVisitor visitor) {
        visitSources(facet.getSourceFolder(), visitor);

    }

    public void visitSources(Resource<?> searchFolder, final JavaResourceVisitor visitor) {
        if (searchFolder instanceof DirectoryResource) {

            searchFolder.listResources(new ResourceFilter() {
                @Override
                public boolean accept(Resource<?> resource) {
                    if (resource instanceof DirectoryResource) {
                        visitSources(resource, visitor);
                    }
                    if (resource instanceof JavaResource) {
                        visitor.visit((JavaResource) resource);
                    }

                    return false;
                }
            });
        }
    }

    protected void addClassInformation(String somePackageName, List<ClassInformation> someClassInformationList) {
        // List is never empty and all
        Map<String, String> importNames = parseImportNames(someClassInformationList.get(0).getImportList());
        for (ClassInformation classInformation : someClassInformationList) {
            classInformation.setQualifiedName(somePackageName + "." + classInformation.getSimpleName());
            if (classInformation.getSuperType() != null) {
                classInformation.setSuperType(toFullyQualifiedName(classInformation.getSuperType(), importNames,
                        somePackageName));
            }
            if (!classInformation.getInterfaceTypes().isEmpty()) {
                List<String> data = new ArrayList<String>();
                for (String interfaceType : classInformation.getInterfaceTypes()) {
                    data.add(toFullyQualifiedName(interfaceType, importNames, somePackageName));
                }
                classInformation.setInterfaceTypes(data);
            }
            classInformationList.add(classInformation);
        }

    }

    private String toFullyQualifiedName(String someSuperType, Map<String, String> someImportNames,
                                        String somePackageName) {
        String result = someImportNames.get(someSuperType);
        if (result == null) {
            result = somePackageName + "." + someSuperType;
        }
        return result;
    }

    private Map<String, String> parseImportNames(List<String> someImportNames) {
        Map<String, String> result = new HashMap<String, String>();
        for (String name : someImportNames) {
            String[] parts = name.split("\\.");
            result.put(parts[parts.length - 1], name);
        }
        return result;
    }
}
