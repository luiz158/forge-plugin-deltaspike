package org.apache.deltaspike.forge.helper;

import org.jboss.forge.project.Project;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 */
public final class ClassPathHelper {

    private ClassPathHelper() {
    }

    public static URL[] findCDIArchivesOnClassPath(Project project) {
        MavenHelper mavenHelper = MavenHelper.getInstance(project);

        File[] buildClassPath = mavenHelper.getBuildClassPath();
        URL[] classPath = convertToURL(buildClassPath);
        return defineCDIJars(classPath);
    }

    private static URL[] defineCDIJars(URL[] someClassPath) {
        // No parent as we only want the jars that contain a beans.xml in the specified classpath.
        ClassLoader ldr = new URLClassLoader(someClassPath, null);

        Enumeration<URL> resources = null;
        try {

            resources = ldr.getResources("META-INF/beans.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<URL> result = new ArrayList<URL>();

        // FIXME What if no URL has beans.xml -> NullPointer
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            final JarURLConnection connection;
            try {
                connection = (JarURLConnection) url.openConnection();
                URL urlFile = connection.getJarFileURL();

                result.add(urlFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result.toArray(new URL[result.size()]);
    }

    private static URL[] convertToURL(File[] someBuildClassPath) {
        URL[] result = new URL[someBuildClassPath.length];
        int idx = 0;
        for (File file : someBuildClassPath) {
            try {
                result[idx++] = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String adjustedPath(String fullPath, String relativePath) {
        String result;
        int idx = fullPath.indexOf(relativePath);
        if (idx == -1) {
            result = relativePath;
        } else {
            String base = fullPath.substring(0, idx - 1);
            int lastPos = base.lastIndexOf("/");
            result = base.substring(lastPos) + "/" + relativePath;
        }
        return result;
    }
}
