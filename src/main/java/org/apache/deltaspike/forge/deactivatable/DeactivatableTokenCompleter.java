package org.apache.deltaspike.forge.deactivatable;

import org.apache.deltaspike.forge.helper.ClassPathHelper;
import org.apache.deltaspike.forge.helper.overview.JavaMetaData;
import org.apache.deltaspike.forge.helper.overview.query.MetaDataQuery;
import org.apache.deltaspike.forge.helper.overview.scanner.*;
import org.apache.deltaspike.forge.helper.overview.util.ConfigurationBuilder;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.completer.SimpleTokenCompleter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 */
public class DeactivatableTokenCompleter extends SimpleTokenCompleter {

    @Inject
    private Shell shell;


    @Override
    public Iterable<?> getCompletionTokens() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setScanners(new TypeAnnotationsScanner(),
                new SubTypesScanner());

        builder.setUrls(ClassPathHelper.findCDIArchivesOnClassPath(shell.getCurrentProject()));
        JavaMetaData metaData = new JavaMetaData(builder);
        Set<String> deactivatable = MetaDataQuery.getInstance(metaData).getSubTypesOf("org.apache.deltaspike.core.spi.activation.Deactivatable");

        return new ArrayList<String>(deactivatable);
    }
}
