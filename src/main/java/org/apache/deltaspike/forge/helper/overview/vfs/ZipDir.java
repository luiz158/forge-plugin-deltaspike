package org.apache.deltaspike.forge.helper.overview.vfs;

import com.google.common.collect.AbstractIterator;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;

/**
 * an implementation of {@link org.apache.deltaspike.forge.helper.overview.vfs.Vfs.Dir} for {@link java.util.zip
 * .ZipFile}
 * Taken from by http://code.google.com/p/reflections/
 */
public class ZipDir implements Vfs.Dir {
    private final java.util.zip.ZipFile zipFile;

    private String path;

    public ZipDir(URL url) {
        path = Vfs.normalizePath(url);

        try {
            zipFile = new java.util.zip.ZipFile(this.path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return path;
    }

    protected java.util.zip.ZipFile getZipFile() {
        return zipFile;
    }

    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            public Iterator<Vfs.File> iterator() {
                return new AbstractIterator<Vfs.File>() {
                    private final Enumeration<? extends ZipEntry> entries = zipFile.entries();

                    protected Vfs.File computeNext() {
                        return entries.hasMoreElements() ? new ZipFile(ZipDir.this,
                                entries.nextElement()) : endOfData();
                    }
                };
            }
        };
    }

    public void close() {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException e) {
                throw new RuntimeException("could not close zip file " + path, e);
            }
        }
    }

    @Override
    public String toString() {
        return zipFile.getName();
    }
}