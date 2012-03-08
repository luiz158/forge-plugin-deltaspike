package org.apache.deltaspike.forge.helper.overview.adapter;

import org.apache.deltaspike.forge.helper.overview.scanner.AbstractScanner;
import org.apache.deltaspike.forge.helper.overview.store.DataStoreEntryBuilder;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;

import java.util.List;

/**
 * Inspired by http://code.google.com/p/reflections/
 * A MeteDataAdapter defines the interface used by the MetaDaat scanning to retrieve information about Java classes.
 * There exists a version for compiled Java and one for source files.
 * @author Rudy De Busscher
 */
public abstract class MetaDataAdapter<C, F, M, MP> {

    //
    public abstract String getClassName(final C cls);

    public abstract String getSuperclassName(final C cls);

    public abstract List<String> getInterfacesNames(final C cls);

    //
    public abstract List<F> getFields(final C cls);

    public abstract List<M> getMethods(final C cls);

    public abstract String getMethodName(final M method);

    public abstract List<MP> getParameters(final M method);

    public abstract List<String> getParameterNames(final M method);

    public abstract List<String> getClassAnnotationNames(final C aClass);

    public abstract List<String> getFieldAnnotationNames(final F field);

    public abstract List<String> getMethodAnnotationNames(final M method);

    public abstract List<String> getParameterAnnotationNames(final M method, final int parameterIndex);

    public abstract String getReturnTypeName(final M method);

    public abstract String getFieldName(final F field);

    public abstract String getFieldType(F Field);

    public abstract C[] getOrCreateClassObject(Vfs.File file) throws Exception;

    public abstract String getParameterName(MP methodParameter);

    public abstract String getMethodParameterType(MP methodParameter);

    public final DataStoreEntryBuilder getDataStoreEntryBuilder(AbstractScanner scanner) {
        return DataStoreEntryBuilder.getInstance(scanner, this);
    }

}