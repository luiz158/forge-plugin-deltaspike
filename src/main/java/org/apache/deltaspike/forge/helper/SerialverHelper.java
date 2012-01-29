package org.apache.deltaspike.forge.helper;

import javax.tools.JavaFileManager;
import java.io.ObjectStreamClass;

/**
 * @author Rudy De Busscher
 * http://www.koders.com/java/fid9C000A36992600FE1843A5AED6FE613BF68B8B1E.aspx?s=199
 */
public class SerialverHelper {

    private JavaFileManager fileManager;

    private SerialverHelper(final JavaFileManager someFileManager) {
        fileManager = someFileManager;
    }


    public static SerialverHelper create(final JavaFileManager someFileManager) {
        return new SerialverHelper(someFileManager);
    }

    public Long defineFor(String someJavaClassName) throws ClassNotFoundException {
        ObjectStreamClass osc = ObjectStreamClass.lookupAny(fileManager.getClassLoader(null).loadClass
                (someJavaClassName));
        return osc.getSerialVersionUID();
    }
}
