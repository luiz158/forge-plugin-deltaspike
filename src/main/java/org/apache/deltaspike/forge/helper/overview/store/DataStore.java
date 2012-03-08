package org.apache.deltaspike.forge.helper.overview.store;

import com.google.common.base.Supplier;
import com.google.common.collect.*;
import org.apache.deltaspike.forge.helper.overview.scanner.Scanner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * stores metadata information in multimaps
 * Inspired by http://code.google.com/p/reflections/
 */
public class DataStore {

    private static transient Supplier<Set<DataStoreEntry>> setSupplier = new Supplier<Set<DataStoreEntry>>() {
        public Set<DataStoreEntry> get() {
            return Sets.newHashSet();
        }
    };

    private final Map<String, Multimap<String, DataStoreEntry>> storeMap = Maps.newHashMap();

    private SetMultimap<String, DataStoreEntry> createMultiMap() {
        return Multimaps.newSetMultimap(new HashMap<String, Collection<DataStoreEntry>>(), setSupplier);
    }

    public Multimap<String, DataStoreEntry> getOrCreate(Class<? extends Scanner> indexName) {
        Multimap<String, DataStoreEntry> multiMap = storeMap.get(indexName.getSimpleName());
        if (multiMap == null) {
            multiMap = createMultiMap();
            storeMap.put(indexName.getSimpleName(), multiMap);
        }
        return multiMap;
    }

    /**
     * return the multimap store of the given scanner class. not immutable
     */
    public Multimap<String, DataStoreEntry> get(Class<? extends Scanner> scannerClass) {
        return storeMap.get(scannerClass.getSimpleName());
    }

    /**
     * get the values of given keys stored for the given scanner class
     */
    public Set<DataStoreEntry> get(Class<? extends Scanner> scannerClass, String... keys) {
        Set<DataStoreEntry> result = Sets.newHashSet();

        Multimap<String, DataStoreEntry> map = get(scannerClass);
        if (map != null) {
            for (String key : keys) {
                result.addAll(map.get(key));
            }
        }

        return result;
    }

    /**
     * return the keys count
     */
    public Integer getKeysCount() {
        Integer keys = 0;
        for (Multimap<String, DataStoreEntry> multimap : storeMap.values()) {
            keys += multimap.keySet().size();
        }
        return keys;
    }

    /**
     * return the values count
     */
    public Integer getValuesCount() {
        Integer values = 0;
        for (Multimap<String, DataStoreEntry> multimap : storeMap.values()) {
            values += multimap.size();
        }
        return values;
    }

    //query
/*


    public Set<AbstractDataStoreEntry> getTypesAnnotatedWithDirectly(final String annotation) {
        return get(TypeAnnotationsScanner.class, annotation);
    }


    public Set<String> getTypesAnnotatedWith(final String annotation) {
        return getTypesAnnotatedWith(annotation, true);
    }


    public Set<String> getTypesAnnotatedWith(final String annotation, boolean honorInherited) {
        final Set<String> result = new HashSet<String>();

        if (isAnnotation(annotation)) {
            final Set<AbstractDataStoreEntry> types = getTypesAnnotatedWithDirectly(annotation);
            Set<String> inherited = getInheritedSubTypes(types, annotation, honorInherited);
            result.addAll(inherited);
        }
        return result;
    }

    public Set<String> getInheritedSubTypes(Iterable<AbstractDataStoreEntry> types, String annotation,
    boolean honorInherited) {
        Set<String> result = Sets.newHashSet();

        if (honorInherited && isInheritedAnnotation(annotation)) {
            //when honoring @Inherited, meta-annotation should only effect annotated super classes and it's sub types
            for (AbstractDataStoreEntry type : types) {
                if (isClass(type.getQualifiedTypeName())) {
                    result.addAll(getSubTypesOf(type.getQualifiedTypeName()));
                }
            }
        } else if (!honorInherited) {
            //when not honoring @Inherited, meta annotation effects all subtypes, including annotations interfaces
            // and classes
            for (AbstractDataStoreEntry type : types) {
                if (isAnnotation(type.getQualifiedTypeName())) {
                    result.addAll(getTypesAnnotatedWith(type.getQualifiedTypeName(), false));
                } else {
                    result.addAll(getSubTypesOf(type));
                }
            }
        }

        return result;
    }

    public Set<String> getMethodsAnnotatedWith(String annotation) {
        return get(MethodAnnotationsScanner.class, annotation);
    }


    public Set<String> getFieldsAnnotatedWith(String annotation) {
        return get(FieldAnnotationsScanner.class, annotation);
    }





    public Set<String> getResources(final Pattern pattern) {
        return getResources(new Predicate<String>() {
            public boolean apply(String input) {
                return pattern.matcher(input).matches();
            }
        });
    }

*/
    //support

    /*

    public boolean isClass(String type) {
        //todo create a string version of this
        return !isInterface(type);
    }


    public boolean isInterface(String aClass) {
        //todo create a string version of this
        return ReflectionUtils.forName(aClass).isInterface();
    }


    public boolean isAnnotation(String typeAnnotatedWith) {
        Multimap<String, String> mmap = get(TypeAnnotationsScanner.class);
        return mmap != null && mmap.keySet().contains(typeAnnotatedWith);
    }


    public boolean isInheritedAnnotation(String typeAnnotatedWith) {
        Multimap<String, String> mmap = get(TypeAnnotationsScanner.class);
        return mmap != null && mmap.get(Inherited.class.getName()).contains(typeAnnotatedWith);
    }
     */

    //

}
