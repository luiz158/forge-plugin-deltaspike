package org.apache.deltaspike.forge.helper.overview.scanner;

import org.apache.deltaspike.forge.helper.ClassPathHelper;
import org.apache.deltaspike.forge.helper.overview.adapter.MetaDataAdapter;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntryBuilder;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;
import org.apache.deltaspike.forge.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * collects all resources that are not classes in a collection
 * <p>key: value - {web.xml: WEB-INF/web.xml}
 * <p/>
 * Inspired by http://code.google.com/p/reflections/
 *
 * @author Rudy De Busscher
 */
public class ResourcesScanner extends AbstractScanner {
    public boolean acceptsInput(String file) {
        return !file.endsWith(".class") && !file.endsWith(".java"); //not a class
    }

    public void scan(Vfs.File file) {
        String pathValue = ClassPathHelper.adjustedPath(file.getFullPath(), file.getRelativePath());
        DataStoreEntryBuilder.getInstance(this, null).setKey(file.getName()).setFileRelativePath(pathValue)
                .setResourceContent(loadFileContentsIfNeeded(file)).storeEntry();
    }

    private String loadFileContentsIfNeeded(Vfs.File someFile) {
        String result = null;
        if (keepFileContents(someFile.getRelativePath())) {
            try {
                result = loadFileContents(someFile.openInputStream());
            } catch (IOException e) {
                // FIXME
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean keepFileContents(String someRelativePath) {
        boolean result = false;
        if (someRelativePath.endsWith(".xml") || someRelativePath.endsWith(".properties")) {
            result = true;
        }
        if (someRelativePath.contains("META-INF/services/")) {
            result = true;
        }
        return result;
    }

    private String loadFileContents(InputStream stream) throws IOException {
        String result = StreamUtil.loadInputStream(stream);
        stream.close();
        return result;
    }


    public void scan(Object cls[], final MetaDataAdapter metaDataAdapter) {
        throw new UnsupportedOperationException(); //shouldn't get here
    }

    @Override
    public String toStringRepresentation(DataStoreEntry dataStoreEntry, boolean verbose) {
        return dataStoreEntry.getRelativePath();
    }
}
