package org.apache.deltaspike.forge.util;

import org.apache.deltaspike.forge.helper.overview.JavaMetaDataException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public final class StreamUtil {

    private StreamUtil() {
    }

    public static String loadInputStream(InputStream stream) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        try {
            int size = stream.read(buffer);
            while (size > 0) {
                data.write(buffer, 0, size);
                size = stream.read(buffer);
            }
            stream.close();
            data.close();
            return data.toString("UTF-8");
        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
            throw new JavaMetaDataException(e.getMessage());
        }
    }
}
