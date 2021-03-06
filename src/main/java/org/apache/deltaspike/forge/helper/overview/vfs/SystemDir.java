package org.apache.deltaspike.forge.helper.overview.vfs;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * an implementation of {@link org.apache.deltaspike.forge.helper.overview.vfs.Vfs.Dir} for directory {@link java
 * .io.File}
 * Taken from by http://code.google.com/p/reflections/
 */
public class SystemDir implements Vfs.Dir {
    private final File file;

    public SystemDir(URL url) {
        file = new File(Vfs.normalizePath(url));
    }

    public String getPath() {
        return file.getPath();
    }

    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            public Iterator<Vfs.File> iterator() {
                return new AbstractIterator<Vfs.File>() {
                    private Stack<File> stack = new Stack<File>();

                    {
                        stack.addAll(listFiles(file));
                    }

                    protected Vfs.File computeNext() {
                        while (!stack.isEmpty()) {
                            final File file = stack.pop();
                            if (file.isDirectory()) {
                                stack.addAll(listFiles(file));
                            } else {
                                return new SystemFile(SystemDir.this, file);
                            }
                        }

                        return endOfData();
                    }
                };
            }
        };
    }

    public void close() {
    }

    @Override
    public String toString() {
        return file.toString();
    }

    private static List<File> listFiles(final File file) {
        File[] files = file.listFiles();

        if (files != null) {
            return Lists.newArrayList(files);
        } else {
            return Lists.newArrayList();
        }
    }
}
