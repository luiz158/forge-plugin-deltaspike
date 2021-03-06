package org.apache.deltaspike.forge.helper.overview.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * an implementation of {@link org.apache.deltaspike.forge.helper.overview.vfs.Vfs.File} for {@link java.util.zip
 * .ZipEntry}
 * Taken from by http://code.google.com/p/reflections/
 */
public class ZipFile implements Vfs.File {
    private final ZipDir dir;

    private final ZipEntry entry;

    public ZipFile(final ZipDir dir, ZipEntry entry) {
        this.dir = dir;
        this.entry = entry;
    }

    public String getFullPath() {
        return dir.getPath() + "/" + entry.getName();
    }

    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public String getPath() {
        return dir.getPath();
    }

    public String getRelativePath() {
        return entry.getName();
    }

    public InputStream openInputStream() throws IOException {
        return dir.getZipFile().getInputStream(entry);
    }

    @Override
    public String toString() {
        return dir.getPath() + "!" + java.io.File.separatorChar + entry.toString();
    }
}
