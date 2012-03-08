package org.apache.deltaspike.forge.helper.overview.adapter;

import com.google.common.collect.Lists;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import org.apache.deltaspike.forge.helper.overview.JavaMetaDataException;
import org.apache.deltaspike.forge.helper.overview.adapter.forge.AdapterParameter;
import org.apache.deltaspike.forge.helper.overview.util.Utils;
import org.apache.deltaspike.forge.helper.overview.vfs.Vfs;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Inspired by http://code.google.com/p/reflections/
 * Read information from compiled class, through Javassist.
 * @author Rudy De Busscher
 */
public class JavassistAdapter extends MetaDataAdapter<ClassFile, FieldInfo, MethodInfo, AdapterParameter> {

    private static Map<Vfs.File, ClassFile> classFileCache = new HashMap<Vfs.File, ClassFile>();

    public JavassistAdapter() {
        clearCache();
    }

    public List<FieldInfo> getFields(final ClassFile cls) {
        //noinspection unchecked
        return cls.getFields();
    }

    public List<MethodInfo> getMethods(final ClassFile cls) {
        //noinspection unchecked
        return cls.getMethods();
    }

    public String getMethodName(final MethodInfo method) {
        return method.getName();
    }

    @Override
    public List<AdapterParameter> getParameters(MethodInfo method) {
        List<AdapterParameter> result = new ArrayList<AdapterParameter>();
        for (String parameterName : getParameterNames(method)) {
            AdapterParameter parameter = new AdapterParameter();
            parameter.setName(parameterName);
            parameter.setQualifiedType(null);

            for (String annotation : getMethodAnnotationNames(method)) {
                parameter.addAnnotation(annotation);
            }
            result.add(parameter);
        }
        return result;
    }

    public List<String> getParameterNames(final MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        return splitDescriptorToTypeNames(descriptor);
    }

    public List<String> getClassAnnotationNames(final ClassFile aClass) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) aClass.getAttribute(AnnotationsAttribute
                .visibleTag);
        return getAnnotationNames(annotationsAttribute);
    }

    public List<String> getFieldAnnotationNames(final FieldInfo field) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) field.getAttribute(AnnotationsAttribute
                .visibleTag);

        return getAnnotationNames(annotationsAttribute);
    }

    public List<String> getMethodAnnotationNames(final MethodInfo method) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) method.getAttribute(AnnotationsAttribute
                .visibleTag);

        return getAnnotationNames(annotationsAttribute);
    }

    public List<String> getParameterAnnotationNames(final MethodInfo method, final int parameterIndex) {
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = (ParameterAnnotationsAttribute) method
                .getAttribute(ParameterAnnotationsAttribute.visibleTag);

        if (parameterAnnotationsAttribute != null) {
            Annotation[][] annotations = parameterAnnotationsAttribute.getAnnotations();
            if (parameterIndex < annotations.length) {
                Annotation[] annotation = annotations[parameterIndex];
                return getAnnotationNames(annotation);
            }
        }

        return new ArrayList<String>();
    }

    public String getReturnTypeName(final MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.lastIndexOf(")") + 1);
        return splitDescriptorToTypeNames(descriptor).get(0);
    }

    public String getFieldName(final FieldInfo field) {
        return field.getName();
    }

    public String getFieldType(final FieldInfo field) {
        return Descriptor.toClassName(field.getDescriptor());
    }

    public ClassFile[] getOrCreateClassObject(final Vfs.File file) {

        ClassFile result = classFileCache.get(file);
        if (result == null) {
            result = createClassObject(file);
            classFileCache.put(file, result);
        }
        return new ClassFile[]{result};
    }

    protected ClassFile createClassObject(final Vfs.File file) {
        InputStream inputStream = null;
        try {
            inputStream = file.openInputStream();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
            return new ClassFile(dis);
        } catch (IOException e) {
            throw new JavaMetaDataException("could not create class file from " + file.getName(), e);
        } finally {
            Utils.close(inputStream);
        }
    }

    /*
    public String getMethodModifier(MethodInfo method) {
        int accessFlags = method.getAccessFlags();
        return AccessFlag.isPrivate(accessFlags) ? "private" : AccessFlag.isProtected(accessFlags) ? "protected" :
                isPublic(accessFlags) ? "public" : "";
    }
    */

    @Override
    public String getMethodParameterType(AdapterParameter methodParameter) {
        return methodParameter.getQualifiedType();
    }

    @Override
    public String getParameterName(AdapterParameter methodParameter) {
        return methodParameter.getName();
    }

    /*
    public boolean isPublic(Object o) {
        Integer accessFlags = null;
        if (o instanceof ClassFile) {
            accessFlags = ((ClassFile) o).getAccessFlags();
        }
        if (o instanceof FieldInfo) {
            accessFlags = ((FieldInfo) o).getAccessFlags();
        }
        if (o instanceof MethodInfo) {
            accessFlags = ((MethodInfo) o).getAccessFlags();
        }
        return accessFlags != null && AccessFlag.isPublic(accessFlags);
    }
    */

    //
    public String getClassName(final ClassFile cls) {
        return cls.getName();
    }

    public String getSuperclassName(final ClassFile cls) {
        return cls.getSuperclass();
    }

    public List<String> getInterfacesNames(final ClassFile cls) {
        return Arrays.asList(cls.getInterfaces());
    }

    public void clearCache() {
        classFileCache.clear();
    }

    //
    private List<String> getAnnotationNames(final AnnotationsAttribute annotationsAttribute) {
        if (annotationsAttribute == null) {
            return new ArrayList<String>(0);
        }

        final Annotation[] annotations = annotationsAttribute.getAnnotations();
        return getAnnotationNames(annotations);
    }

    private List<String> getAnnotationNames(final Annotation[] annotations) {
        List<String> result = Lists.newArrayList();

        for (Annotation annotation : annotations) {
            result.add(annotation.getTypeName());
        }

        return result;
    }

    private List<String> splitDescriptorToTypeNames(final String descriptors) {
        List<String> result = Lists.newArrayList();

        if (descriptors != null && descriptors.length() != 0) {

            List<Integer> indices = Lists.newArrayList();
            Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);
            while (iterator.hasNext()) {
                indices.add(iterator.next());
            }
            indices.add(descriptors.length());

            for (int i = 0; i < indices.size() - 1; i++) {
                String s1 = Descriptor.toString(descriptors.substring(indices.get(i), indices.get(i + 1)));
                result.add(s1);
            }

        }

        return result;
    }
}
