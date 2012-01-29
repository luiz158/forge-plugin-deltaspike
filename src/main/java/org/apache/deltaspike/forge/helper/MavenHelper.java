package org.apache.deltaspike.forge.helper;

import org.apache.deltaspike.forge.util.FacetsUtil;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;

import java.io.File;

/**
 * @author Rudy De Busscher
 */
public class MavenHelper {

    private File mavenLocalRepository;

    private MavenHelper(Project project) {
        // The deltaspike plugin can only be called on a maven project.
        MavenCoreFacet mavenFacet = FacetsUtil.tryToGetFacet(project, MavenCoreFacet.class);
        mavenLocalRepository = mavenFacet.getLocalRepositoryDirectory().getUnderlyingResourceObject();
    }

    /**
     * Create an instance of the maven helper.
     * @param project current project we are working on.
     * @return new instance of the maven helper.
     */
    public static MavenHelper getInstance(Project project) {
        return new MavenHelper(project);
    }

    /**
     * Return the file to the artifact in the local maven repository.
     * @param groupId The maven group id
     * @param artifactId the maven artifact id
     * @param version the maven version
     * @return file to the artifact in the local maven repository.
     */
    public File resolve(final String groupId, final String artifactId, final String version) {
        return resolve(groupId, artifactId, version, null);
    }

    public File resolve(final String groupId, final String artifactId, final String version, final String classifier) {
        return new File(mavenLocalRepository.getAbsolutePath() + File.separatorChar +
                groupId.replace(".", File.separator) + File.separatorChar +
                artifactId + File.separatorChar +
                version + File.separatorChar +
                artifactId + "-" + version +
                (classifier != null && classifier.length() > 0 ? ("-" + classifier) : "") + ".jar");
    }

    /**
     * @param qualifiedArtifactId groupId:artifactId:packaging:classifier:version
     * @return
     */
    public File resolve(final String qualifiedArtifactId) {
        String[] segments = qualifiedArtifactId.split(":");
        if (segments.length == 5) {
            return resolve(segments[0], segments[1], segments[4], segments[3]);
        }
        throw new IllegalArgumentException("Invalid qualified artifactId syntax: " + qualifiedArtifactId);
    }

    public File[] resolve(final String... qualifiedArtifactIds) {
        int n = qualifiedArtifactIds.length;
        File[] artifacts = new File[n];
        for (int i = 0; i < n; i++) {
            artifacts[i] = resolve(qualifiedArtifactIds[i]);
        }

        return artifacts;
    }
}
