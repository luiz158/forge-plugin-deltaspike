package org.apache.deltaspike.forge.projectstage;

import org.jboss.forge.shell.completer.SimpleTokenCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ProjectStageTokenCompleter extends SimpleTokenCompleter {

    private List<String> projectStageList;

    public ProjectStageTokenCompleter() {
        // TODO Is there a way of getting the custom defined project stages (also in dependency jars ??)
        projectStageList = new ArrayList<String>();
        projectStageList.add("UnitTest");
        projectStageList.add("Development");
        projectStageList.add("SystemTest");
        projectStageList.add("IntegrationTest");
        projectStageList.add("Staging");
        projectStageList.add("Production");

    }

    @Override
    public Iterable<?> getCompletionTokens() {
        return projectStageList;
    }
}
