package org.apache.deltaspike.forge;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Rudy De Busscher
 */
public class TransientDependencyTest extends TestCase {

    public void testToString() throws Exception {
        Assert.assertEquals("javax.enterprise:cdi-api:::1.0-SP4", TransientDependency.CDI_API.getDependency().toCoordinates());
    }
}
