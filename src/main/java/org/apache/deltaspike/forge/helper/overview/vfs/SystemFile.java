package org.apache.deltaspike.forge.helper.overview.vfs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * an implementation of {@link org.apache.deltaspike.forge.helper.overview.vfs.Vfs.File} for a directory {@link java
 * .io.File}
 *
 * Taken from by http://code.google.com/p/reflections/
 */
public class SystemFile implements Vfs.File {
    private final SystemDir dir;

    private final java.io.File file;

    public SystemFile(final SystemDir dir, java.io.File file) {
        this.dir = dir;
        this.file = file;
    }

    public String getFullPath() {
        return file.getPath();
    }

    public String getName() {
        return file.getName();
    }

    public String getRelativePath() {
        if (file.getPath().startsWith(dir.getPath())) {
            return file.getPath().substring(dir.getPath().length() + 1).replace('\\', '/');
        }

        return null; //should not get here
    }

    public InputStream openInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
