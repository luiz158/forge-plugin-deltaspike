package org.apache.deltaspike.forge.helper;

import org.apache.deltaspike.forge.serialversion.ClassFileManager;

import javax.tools.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rudy De Busscher
 * http://www.javablogging.com/dynamic-in-memory-compilation/
 *
 */
public class CompilerHelper {

    // TODO : Verify if this kind of class constructs is the easiest/most readible.
    // TODO : What happens if it can be compiled.
    private File[] dependencies;

    private CompilerHelper() {
    }

    public static CompilerHelper create() {
        return new CompilerHelper();
    }

    public CompilerHelper withDependencies(File... someDependencies) {
        dependencies = someDependencies;
        return this;
    }

    public JavaFileManager compile(final String javaClassName, final String javaSource) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaFileObject file = new JavaSourceFromString(javaClassName, javaSource);
        Iterable compilationUnits = Arrays.asList(file);

        List<String> optionList = new ArrayList<String>();

        // set compiler's classpath to be same as the runtime's
        optionList.addAll(Arrays.asList("-classpath", addDependenciesToClassPath()));

        ClassLoader loader = null;
        try {
            loader = defineClassLoaderWithDependencies();
        } catch (MalformedURLException e) {
            // FIXME
            e.printStackTrace();
        }

        JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null), loader);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null,
                compilationUnits);
        if (!task.call()) {
            // FIXME
            System.out.println("Does not compile ");
            System.out.println(javaSource);
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                System.out.println(diagnostic.getCode());
                System.out.println(diagnostic.getKind());
                System.out.println(diagnostic.getPosition());
                System.out.println(diagnostic.getStartPosition());
                System.out.println(diagnostic.getEndPosition());
                System.out.println(diagnostic.getSource());
                System.out.println(diagnostic.getMessage(null));

            }
        }
        return fileManager;
    }


    private ClassLoader defineClassLoaderWithDependencies() throws MalformedURLException {

        URL[] classPathUrls = new URL[dependencies.length];
        for (int idx = 0; idx < dependencies.length; idx++) {
            classPathUrls[idx] = dependencies[idx].toURI().toURL();
        }
        return new URLClassLoader(classPathUrls);

    }

    private String addDependenciesToClassPath() {
        StringBuilder result = new StringBuilder();
        for (File dependency : dependencies) {
            if (result.length() > 0) {
                result.append(';');
            }
            result.append(dependency.getAbsolutePath());
        }

        return result.toString();
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {

        private String code;

        public JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

}
