
package org.apache.deltaspike.forge;

import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.*;

import javax.inject.Inject;

/**
 * @author Rudy De Busscher
 */
@Alias("deltaspike")
public class DeltaspikePlugin implements Plugin
{
   @Inject
   private ShellPrompt prompt;

   @DefaultCommand
   public void defaultCommand(@PipeIn String in, PipeOut out)
   {
      out.println("Welcome to the DeltaSpike forge plugin.  Please recheck later when the functionality is implemented.");
   }

}
