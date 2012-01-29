package org.apache.deltaspike.forge.serialversion;

import javax.tools.*;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rudy De Busscher
 * http://www.javablogging.com/dynamic-in-memory-compilation/
 */
public class ClassFileManager extends ForwardingJavaFileManager {
    /**
     * Instance of JavaClassObject that will store the
     * compiled bytecode of our class
     */
    private Map<String, JavaClassObject> jclassObjectMap = new HashMap<String, JavaClassObject>();

    private ClassLoader loader;

    /**
     * Will initialize the manager with the specified
     * standard java file manager
     *
     * @param standardManager
     * @param dependencyLoader
     */
    public ClassFileManager(StandardJavaFileManager standardManager, ClassLoader dependencyLoader) {
        super(standardManager);
        loader = dependencyLoader;
    }

    /**
     * Will be used by us to get the class loader for our
     * compiled class. It creates an anonymous class
     * extending the SecureClassLoader which uses the
     * byte code created by the compiler and stored in
     * the JavaClassObject, and returns the Class for it
     */
    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {

                if (jclassObjectMap.containsKey(name)) {
                    // In memory compiled, so take those bytes.
                    byte[] b = jclassObjectMap.get(name).getBytes();
                    return super.defineClass(name, b, 0, b.length);
                } else {
                    // Transient dependency so forward to the classloader we received;
                    return loader.loadClass(name);
                }
            }
        };
    }

    /**
     * Gives the compiler an instance of the JavaClassObject
     * so that the compiler can write the byte code into it.
     */
    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
                                               JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaClassObject jclassObject = new JavaClassObject(className, kind);
        jclassObjectMap.put(className, jclassObject);
        return jclassObject;
    }
}
