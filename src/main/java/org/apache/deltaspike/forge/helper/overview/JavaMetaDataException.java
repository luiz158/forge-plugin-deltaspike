package org.apache.deltaspike.forge.helper.overview;

/**
 * Taken from by http://code.google.com/p/reflections/
 */
public class JavaMetaDataException extends RuntimeException {

    private static final long serialVersionUID = -7826320656865581970L;

    public JavaMetaDataException(String message) {
        super(message);
    }

    public JavaMetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaMetaDataException(Throwable cause) {
        super(cause);
    }
}
