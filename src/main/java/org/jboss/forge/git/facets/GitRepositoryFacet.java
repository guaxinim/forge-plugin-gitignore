/*
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.forge.git.facets;

import javax.inject.Inject;

import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.shell.Shell;

/**
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
public class GitRepositoryFacet extends BaseFacet {

    @Inject
    private Shell shell;
    
    @Override
    public boolean install() {
        try {
            shell.execute("git init " + getProject().getProjectRoot().getFullyQualifiedName());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isInstalled() {
        return project.getProjectRoot().getChildDirectory(".git").exists();
    }
}
