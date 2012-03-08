package org.apache.deltaspike.forge.helper;

import org.apache.deltaspike.forge.util.FacetsUtil;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.Project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

/**
 * @author Rudy De Busscher
 */
public class MavenHelper {

    private File mavenLocalRepository;

    private MavenCoreFacet mavenFacet;

    private MavenHelper(Project project) {
        // The deltaspike plugin can only be called on a maven project.
        mavenFacet = FacetsUtil.tryToGetFacet(project, MavenCoreFacet.class);
        mavenLocalRepository = mavenFacet.getLocalRepositoryDirectory().getUnderlyingResourceObject();
    }

    /**
     * Create an instance of the maven helper.
     *
     * @param project current project we are working on.
     * @return new instance of the maven helper.
     */
    public static MavenHelper getInstance(Project project) {
        return new MavenHelper(project);
    }

    /**
     * Return the file to the artifact in the local maven repository.
     *
     * @param groupId    The maven group id
     * @param artifactId the maven artifact id
     * @param version    the maven version
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

    public File[] getBuildClassPath() {
        ByteArrayOutputStream mavenOutput = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(mavenOutput);

        ByteArrayOutputStream mavenError = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(mavenError);

        boolean processResult = mavenFacet.executeMavenEmbedded(out, err, new String[]{"dependency:build-classpath",
                "-o"});
        File[] result = null;

        if (processResult) {
            String mavenExecutionResult = mavenOutput.toString();

            String marker = "[INFO] Dependencies classpath:";
            int start = mavenExecutionResult.indexOf(marker);
            int end = mavenExecutionResult.indexOf("[INFO]", start + 20);

            String[] classPathEntries = mavenExecutionResult.substring(start + marker.length() + 1,
                    end - 2).split(";");
            result = new File[classPathEntries.length];
            int idx = 0;
            for (String entry : classPathEntries) {
                result[idx++] = new File(entry);
            }

        }

        return result;

    }
}
