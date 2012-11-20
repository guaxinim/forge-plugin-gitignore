package org.jboss.forge.git.plugins;

import org.jboss.forge.git.resources.GitIgnoreResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresResource;
import org.jboss.forge.shell.plugins.Topic;

/**
 * @author Dan Allen
 */
@Alias("ls")
@RequiresResource(GitIgnoreResource.class)
@Topic("Files & Resources")
@Help("Prints the contents of the current gitignore file")
public class LsGitIgnore implements Plugin {
	
	@DefaultCommand
	public void ls(@Option(description = "path", defaultValue = ".") final Resource<?> resource, PipeOut out) {
		// shell.execute("cat ."); ??
		//out.println("Ignored patterns:");
		//out.println();
		for (String pattern : ((GitIgnoreResource) resource).getPatterns()) {
			out.println(pattern);
		}
		//out.println();
	}
}
