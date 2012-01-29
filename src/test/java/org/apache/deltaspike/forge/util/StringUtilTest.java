package org.apache.deltaspike.forge.util;

import com.google.common.base.CaseFormat;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Rudy De Busscher
 */
public class StringUtilTest {

    @Test
    public void testStringManipulation() {
        Assert.assertEquals("Rudy", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "rudy"));

        Assert.assertEquals("rudy", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "Rudy"));

    }
}
