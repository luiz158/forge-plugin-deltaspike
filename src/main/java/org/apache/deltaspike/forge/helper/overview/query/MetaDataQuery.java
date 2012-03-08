package org.apache.deltaspike.forge.helper.overview.query;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.apache.deltaspike.forge.helper.overview.JavaMetaData;
import org.apache.deltaspike.forge.helper.overview.scanner.ResourcesScanner;
import org.apache.deltaspike.forge.helper.overview.scanner.SubTypesScanner;
import org.apache.deltaspike.forge.helper.overview.store.DataStore;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntry;

import java.util.Collection;
import java.util.Set;

/**
 * A query object to do some searches on the datastore.
 * @author Rudy De Busscher
 */
public class MetaDataQuery {

    private DataStore dataStore;

    private MetaDataQuery(DataStore someDataStore) {
        dataStore = someDataStore;
    }

    public static MetaDataQuery getInstance(JavaMetaData someMetaData) {
        return new MetaDataQuery(someMetaData.getDataStore());
    }

    /**
     * get resources relative paths where simple name (key) matches given namePredicate
     * <p>depends on ResourcesScanner configured, otherwise an empty set is returned
     */
    public Collection<String> getResources(final Predicate<String> namePredicate) {

        Collection<DataStoreEntry> entries = getResourcesAsEntries(namePredicate);
        return extractStoreEntryInformation(entries, new Function<DataStoreEntry, String>() {
            @Override
            public String apply(DataStoreEntry storeEntry) {
                return storeEntry.getRelativePath();
            }
        });
    }

    public Collection<DataStoreEntry> getResourcesAsEntries(final Predicate<String> namePredicate) {

        Multimap<String, DataStoreEntry> mmap = dataStore.get(ResourcesScanner.class);
        return findMatchingEntries(mmap, namePredicate);
    }

    /**
     * gets all sub types in hierarchy of a given type
     * <p/>depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public Set<String> getSubTypesOf(final String type) {

        Set<String> result = Sets.newHashSet();

        Set<DataStoreEntry> subTypes = dataStore.get(SubTypesScanner.class, type);
        result.addAll(extractStoreEntryInformation(subTypes, new Function<DataStoreEntry, String>() {
            @Override
            public String apply(DataStoreEntry storeEntry) {
                return storeEntry.getQualifiedTypeName();
            }
        }));

        for (DataStoreEntry subType : subTypes) {
            result.addAll(getSubTypesOf(subType.getQualifiedTypeName()));
        }

        return ImmutableSet.copyOf(result);
    }

    public Collection<DataStoreEntry> getServiceFiles() {
        Multimap<String, DataStoreEntry> entryMultimap = dataStore.get(ResourcesScanner.class);
        return Collections2.filter(entryMultimap.values(), new Predicate<DataStoreEntry>() {
            @Override
            public boolean apply( DataStoreEntry input) {
                return input.getRelativePath().contains("META-INF/services/");
            }
        });
    }

    private <T> Collection<T> extractStoreEntryInformation(Collection<DataStoreEntry> someEntries,
                                                           Function<? super DataStoreEntry, T> someTransformFunction) {

        return Collections2.transform(someEntries, someTransformFunction);
    }

    private Set<DataStoreEntry> findMatchingEntries(Multimap<String, DataStoreEntry> someMultiMap,
                                                    Predicate<String> namePredicate) {
        if (someMultiMap != null) {
            Set<String> matches = Sets.newHashSet(Iterables.filter(someMultiMap.keySet(), namePredicate));
            return dataStore.get(ResourcesScanner.class, matches.toArray(new String[matches.size()]));
        } else {
            return Sets.newHashSet();
        }
    }

/**
 * <p/>
 * *
 * get types annotated with a given annotation, both classes and annotations
 * <p>{@link java.lang.annotation.Inherited} is honored
 * <p><i>Note that this (@Inherited) meta-annotation type has no effect if the annotated type is used for
 * anything other than a class.
 * Also, this meta-annotation causes annotations to be inherited only from superclasses; annotations on
 * implemented interfaces have no effect.</i>
 * <p/>depends on TypeAnnotationsScanner and SubTypesScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
 * Set<String> typesAnnotatedWith = dataStore.getTypesAnnotatedWith(annotation.getName());
 * return ImmutableSet.copyOf(ReflectionUtils.forNames(typesAnnotatedWith));
 * }
 * <p/>
 * *
 * get types annotated with a given annotation, both classes and annotations
 * <p>{@link java.lang.annotation.Inherited} is honored according to given honorInherited
 * <p><i>Note that this (@Inherited) meta-annotation type has no effect if the annotated type is used for
 * anything other than a class.
 * Also, this meta-annotation causes annotations to be inherited only from superclasses; annotations on
 * implemented interfaces have no effect.</i>
 * <p/>depends on TypeAnnotationsScanner and SubTypesScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation,
 * boolean honorInherited) {
 * Set<String> typesAnnotatedWith = dataStore.getTypesAnnotatedWith(annotation.getName(), honorInherited);
 * return ImmutableSet.copyOf(ReflectionUtils.forNames(typesAnnotatedWith));
 * }
 * <p/>
 * *
 * get types annotated with a given annotation, both classes and annotations, including annotation member values
 * matching
 * <p>{@link java.lang.annotation.Inherited} is honored
 * <p/>depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation) {
 * return getTypesAnnotatedWith(annotation, true);
 * }
 * <p/>
 * *
 * get types annotated with a given annotation, both classes and annotations, including annotation member values
 * matching
 * <p>{@link java.lang.annotation.Inherited} is honored according to given honorInherited
 * <p/>depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation, boolean honorInherited) {
 * Set<String> types = dataStore.getTypesAnnotatedWithDirectly(annotation.annotationType().getName());
 * Set<Class<?>> annotated = ReflectionUtils.getAll(ReflectionUtils.forNames(types),
 * ReflectionUtils.withAnnotation(annotation));
 * Set<String> inherited = dataStore.getInheritedSubTypes(ReflectionUtils.names(annotated),
 * annotation.annotationType().getName(), honorInherited);
 * return ImmutableSet.copyOf(ReflectionUtils.forNames(inherited));
 * }
 * <p/>
 * *
 * get all methods annotated with a given annotation
 * <p/>depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
 * Set<String> annotatedWith = dataStore.getMethodsAnnotatedWith(annotation.getName());
 * <p/>
 * Set<Method> result = Sets.newHashSet();
 * for (String annotated : annotatedWith) {
 * result.add(Utils.getMethodFromDescriptor(annotated, configuration.getClassLoaders()));
 * }
 * <p/>
 * return result;
 * }
 * <p/>
 * *
 * get all methods annotated with a given annotation, including annotation member values matching
 * <p/>depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Method> getMethodsAnnotatedWith(final Annotation annotation) {
 * return ReflectionUtils.getAll(getMethodsAnnotatedWith(annotation.annotationType()),
 * ReflectionUtils.withAnnotation(annotation));
 * }
 * <p/>
 * *
 * get all fields annotated with a given annotation
 * <p/>depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation) {
 * final Set<Field> result = Sets.newHashSet();
 * <p/>
 * Collection<String> annotatedWith = dataStore.getFieldsAnnotatedWith(annotation.getName());
 * for (String annotated : annotatedWith) {
 * result.add(Utils.getFieldFromString(annotated, configuration.getClassLoaders()));
 * }
 * <p/>
 * return result;
 * }
 * <p/>
 * *
 * get all methods annotated with a given annotation, including annotation member values matching
 * <p/>depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
 * <p/>
 * public Set<Field> getFieldsAnnotatedWith(final Annotation annotation) {
 * return ReflectionUtils.getAll(getFieldsAnnotatedWith(annotation.annotationType()),
 * ReflectionUtils.withAnnotation(annotation));
 * }
 * <p/>
 * <p/>
 * <p/>
 * *
 * get resources relative paths where simple name (key) matches given regular expression
 * <p>depends on ResourcesScanner configured, otherwise an empty set is returned
 * <pre>Set<String> xmls = reflections.getResources(".*\\.xml");</pre>
 * <p/>
 * public Set<String> getResources(final Pattern pattern) {
 * Predicate<String> predicate = new PredicateResourceMatcher(pattern);
 * return getResources(predicate);
 * }
 */

}
