package org.apache.deltaspike.forge.helper.overview.store;

/**
 * Basic implementation of the DataStoreEntry. The only real implementation is a kind of builder that retrieves the
 * required information (the data that needs to be kept in the datasotre) from the MetaDataAdapter.
 *
 * @author Rudy De Busscher
 */
public abstract class AbstractDataStoreEntry implements DataStoreEntry {

    private String qualifiedTypeName;

    private String relativePath;

    private String fileContents;

    private StoreEntryJavaClass javaClass;

    private StoreEntryMethod method;

    private StoreEntryField field;

    private StoreEntryMethodParameter methodParameter;

    @Override
    public StoreEntryField getField() {
        return field;
    }

    void setFieldData(StoreEntryField someField) {
        field = someField;
    }

    @Override
    public String getFileContents() {
        return fileContents;
    }

    void setFileContent(String someFileContents) {
        fileContents = someFileContents;
    }

    @Override
    public StoreEntryJavaClass getJavaClass() {
        return javaClass;
    }

    void setJavaClassValue(StoreEntryJavaClass someJavaClass) {
        javaClass = someJavaClass;
    }

    @Override
    public StoreEntryMethod getMethod() {
        return method;
    }

    void setMethodData(StoreEntryMethod someMethod) {
        method = someMethod;
    }

    @Override
    public StoreEntryMethodParameter getMethodParameter() {
        return methodParameter;
    }

    void setMethodParameterData(StoreEntryMethodParameter someMethodParameter) {
        methodParameter = someMethodParameter;
    }

    @Override
    public String getQualifiedTypeName() {
        return qualifiedTypeName;
    }

    void setQualifiedTypeName(String someQualifiedTypeName) {
        qualifiedTypeName = someQualifiedTypeName;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    void setRelativePath(String someRelativePath) {
        relativePath = someRelativePath;
    }

}
