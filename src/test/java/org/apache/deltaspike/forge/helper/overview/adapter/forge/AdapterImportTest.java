package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AdapterImportTest {

    @Test
    public void testConstructor() {
        AdapterImport test = new AdapterImport("org.junit.Test");
        Assert.assertEquals("Test", test.getSimpleName());
        Assert.assertEquals("org.junit", test.getPackage());
        Assert.assertEquals("org.junit.Test", test.getQualifiedName());
        Assert.assertFalse(test.isWildcard());
    }

    @Test
    public void testConstructorWildcard() {
        AdapterImport test = new AdapterImport("org.junit.*");
        Assert.assertTrue(test.isWildcard());
    }

}
