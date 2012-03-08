package org.apache.deltaspike.forge.helper.overview.adapter.forge;

import org.jboss.forge.parser.java.Import;

/**
 * We need here the Forge class as parent because we are using the {@link org.jboss.forge.parser.spi
 * .WildcardImportResolver}.
 *
 * @author Rudy De Busscher
 */
class AdapterImport implements Import {

    private String fullName;

    private String packageName;

    private String simpleName;

    public AdapterImport(String someFullName) {
        fullName = someFullName;
        String[] nameParts = someFullName.split("\\.");
        simpleName = nameParts[nameParts.length - 1];
        packageName = someFullName.substring(0, someFullName.length() - simpleName.length() - 1);
    }

    @Override
    public String getPackage() {
        return packageName;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public String getQualifiedName() {
        return fullName;
    }

    @Override
    public Import setName(String name) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public boolean isStatic() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Import setStatic(boolean value) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public boolean isWildcard() {
        return fullName.contains("*");
    }

    @Override
    public Object getInternal() {
        throw new IllegalStateException("not implemented");
    }
}
