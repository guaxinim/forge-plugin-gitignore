/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.forge.git.plugins;

import javax.inject.Inject;

import org.jboss.forge.git.facets.GitRepositoryFacet;
import org.jboss.forge.git.resources.GitIgnoreResource;
import org.jboss.forge.project.Project;
import org.jboss.forge.resources.ResourceException;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Current;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;

@Alias("gitignore")
@Help("Manage the contents of .gitignore files")
@RequiresProject
@RequiresFacet(GitRepositoryFacet.class)
public class GitIgnore implements Plugin {
	public static final String[] PROJECT_FILE_PATTERNS = {
		"/.project",
		"/.classpath",
		"/.settings/",
		"/.*iml"
	};
	
	public static final String[] BUILD_OUTPUT_PATTERNS = {
		"/target/"
	};
	
	@Inject
	@Current
	private GitIgnoreResource gitIgnoreResource;
	
    @Inject
    private Project project;
    
    @Inject
    private Shell shell;
    
    @Command(value = "build-output", help = "Ignore temporary build output files for Maven and Gradle")
    public void buildOutput(final PipeOut out) {
    	GitIgnoreResource gitignore = getRootGitIgnoreResource(true);
    	gitignore.addPatterns(BUILD_OUTPUT_PATTERNS);
    	out.println("Standard build output patterns have been added to the .gitignore at the root of the project.");
    }
    
    @Command(value = "project-files", help = "Ignore standard project files for Eclipse, IntelliJ and NetBeans")
    public void projectFiles(final PipeOut out) {
    	GitIgnoreResource gitignore = getRootGitIgnoreResource(true);
    	gitignore.addPatterns(PROJECT_FILE_PATTERNS);
    	out.println("Standard project file patterns have been added to the .gitignore at the root of the project.");
    }
    
    @Command(help = "Ignore typical project file and build output patterns")
    public void defaults(PipeOut out) {
    	projectFiles(out);
    	buildOutput(out);
    }
    
    @Command(help = "List the ignore patterns")
    public void list(PipeOut out) {
    	requireResourceInContext();
    	// shell.execute("cat ."); ??
    	for (String pattern : gitIgnoreResource.getPatterns()) {
    		out.println(pattern);
    	}
    }
    
    @Command(help = "Add ignore pattern")
    public void add(@Option(description = "pattern", required = true) String pattern, PipeOut out) {
    	GitIgnoreResource gitignore = getPwdGitIgnoreResource(true);
    	gitignore.addPattern(pattern);
    	out.println("Pattern added to the .gitignore in the current directory");
    }
    
    // FIXME make the completer work when not inside the .gitignore file
    @Command(help = "Remove ignore pattern")
    public void remove(@Option(description = "pattern", required = true, completer = GitIgnorePatternCompleter.class) String pattern, PipeOut out) {
    	GitIgnoreResource gitignore = getPwdGitIgnoreResource(true);
    	gitignore.removePattern(pattern);
    	out.println("Pattern removed from the .gitignore in the current directory");
    }

	protected GitIgnoreResource getRootGitIgnoreResource(boolean create) {
		GitIgnoreResource gitignore = project.getProjectRoot().getChildOfType(GitIgnoreResource.class, GitIgnoreResource.RESOURCE_NAME);
    	if (create && !gitignore.exists()) {
    		gitignore.createNewFile();
    	}
		return gitignore;
	}
	
	protected GitIgnoreResource getPwdGitIgnoreResource(boolean create) {
		GitIgnoreResource gitignore = shell.getCurrentDirectory().getChildOfType(GitIgnoreResource.class, GitIgnoreResource.RESOURCE_NAME);
		if (create && !gitignore.exists()) {
			gitignore.createNewFile();
		}
		return gitignore;
	}

    private void requireResourceInContext()
    {
       if (gitIgnoreResource == null)
       {
          throw new ResourceException("No gitignore resource in context");
       }
    }

}
