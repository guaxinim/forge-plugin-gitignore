package org.jboss.forge.git.plugins;

import javax.inject.Inject;

import org.jboss.forge.git.resources.GitIgnoreResource;
import org.jboss.forge.shell.completer.SimpleTokenCompleter;
import org.jboss.forge.shell.plugins.Current;

/**
 * @author Dan Allen
 */
public class GitIgnorePatternCompleter extends SimpleTokenCompleter {

	@Inject
	@Current
	private GitIgnoreResource resource;
	
	@Override
	public Iterable<?> getCompletionTokens() {
		return resource.getPatterns();
	}

}
