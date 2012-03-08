package org.apache.deltaspike.forge;

import com.google.common.collect.Multimap;
import org.apache.deltaspike.forge.helper.overview.JavaMetaData;
import org.apache.deltaspike.forge.helper.overview.query.MetaDataQuery;
import org.apache.deltaspike.forge.helper.overview.scanner.Scanner;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;
import org.apache.deltaspike.forge.helper.overview.util.ConfigurationBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Set;

/**
 *
 */
public class Performance {
    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        try {
            File f = new File("C:\\temp\\deltaspike\\src\\main\\java");
            builder.addUrls(f.toURI().toURL());
            f = new File("C:\\temp\\deltaspike\\src\\main\\resources");
            builder.addUrls(f.toURI().toURL());

        } catch (MalformedURLException e) {
            // FIXME
            e.printStackTrace();
        }

        JavaMetaData metaData = new JavaMetaData(builder);

        /*
        printStoreForScanner(metaData, ResourcesScanner.class);
        printStoreForScanner(metaData, SubTypesScanner.class);
        printStoreForScanner(metaData, TypeAnnotationsScanner.class);
        printStoreForScanner(metaData, FieldAnnotationsScanner.class);
        printStoreForScanner(metaData, MethodAnnotationsScanner.class);
        printStoreForScanner(metaData, MethodTypeAnnotationScanner.class);
        printStoreForScanner(metaData, MethodParameterAnnotationsScanner.class);
        */

        /*
        Collection<String> resources = MetaDataQuery.getInstance(metaData).getResources(new PredicateResourceMatcher
                (Pattern.compile("beans.xml")));
        for (String res : resources) {
            System.out.println(res);
        }

        Collection<DataStoreEntry> entries = MetaDataQuery.getInstance(metaData).getResourcesAsEntries(new
                PredicateResourceMatcher(Pattern.compile("beans.xml")));
        for (DataStoreEntry entry : entries) {
            System.out.println(entry.getFileContents());
        }
        */
        Set<String> typesOf = MetaDataQuery.getInstance(metaData).getSubTypesOf("org.apache.deltaspike.core.api" +
                ".projectstage.ProjectStage");
        System.out.println(typesOf);

        Collection<DataStoreEntry> serviceFiles = MetaDataQuery.getInstance(metaData).getServiceFiles();
        for (DataStoreEntry dataStoreEntry : serviceFiles) {
            System.out.println(dataStoreEntry.getRelativePath());
            System.out.println("----- " + dataStoreEntry.getFileContents());
        }
    }

    private static void printStoreForScanner(JavaMetaData someMetaData, Class<? extends Scanner> someScannerClass) {
        System.out.println("===============");
        System.out.println(someScannerClass.getName());
        System.out.println("===============");
        Multimap<String,DataStoreEntry> storeEntryMultimap = someMetaData.getDataStore().get(someScannerClass);
        for (String key : storeEntryMultimap.keys()) {
            System.out.println("***********"+key);
            for (DataStoreEntry entry : storeEntryMultimap.get(key)) {
                System.out.println(entry.toStringRepresentation(true));
            }
        }
    }
}
