/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.condition;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;

/**
 * Wait condition to wait for existence of an OpenShift resource.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ResourceExists extends AbstractWaitCondition {

	private OpenShiftProject project;
	private Matcher resourceNameMatcher;
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	private ResourceState buildState;
	private Resource resource;
	
	/**
	 * Creates new ResourceExists to wait for existence of any resource of specified type.
	 * 
	 * @param server OpenShift server
	 * @param username user name
	 * @param projectName project name
	 * @param resource resource type
	 */
	public ResourceExists(Resource resource) {
		this(resource, (Matcher) null, ResourceState.UNSPECIFIED);
	}
	
	/**
	 * Creates new ResourceExists to wait for existence of a resource of specified type
	 * matching specified resource name.
	 * 
	 * @param server OpenShift server
	 * @param username user name
	 * @param projectName project name
	 * @param resource resource type
	 * @param resourceName resource name
	 */
	public ResourceExists(Resource resource, String resourceName) {
		this(resource, new WithTextMatcher(resourceName), ResourceState.UNSPECIFIED);
	}
	
	/**
	 * Creates new ResourceExists to wait for existence of a resource of specified type
	 * matching specified resource name and in specified state.
	 * 
	 * @param server OpenShift server
	 * @param username user name
	 * @param projectName project name
	 * @param resource resource type
	 * @param resourceName resource name
	 * @param resourceState state of a resource
	 */
	public ResourceExists(Resource resource, String resourceName, ResourceState resourceState) {
		this(resource, new WithTextMatcher(resourceName), resourceState);
	}
	
	/**
	 * Creates new ResourceExists to wait for existence of a resource of specified type
	 * matching specified resource name matcher.
	 * 
	 * @param server OpenShift server
	 * @param username user name
	 * @param projectName project name
	 * @param resource resource type
	 * @param nameMatcher resource name matcher
	 */
	public ResourceExists(Resource resource, Matcher nameMatcher) {
		this(resource, nameMatcher, ResourceState.UNSPECIFIED);
	}
		
	/**
	 * Creates new ResourceExists to wait for existence of a resource of specified type
	 * matching specified resource name matcher and in specified state.
	 * 
	 * @param server OpenShift server
	 * @param username user name
	 * @param projectName project name
	 * @param resource resource type
	 * @param nameMatcher resource name matcher
	 * @param resourceState state of a resource
	 */
	public ResourceExists(Resource resource, Matcher nameMatcher, ResourceState buildState) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		project = explorer.getOpenShift3Connection().getProject();
		resourceNameMatcher = nameMatcher;
		this.buildState = buildState;
		this.resource = resource;
	}

	@Override
	public boolean test() {
		List<OpenShiftResource> resources = project.getOpenShiftResources(resource);
		if (resources.isEmpty()) {
			return false;
		}
		
		for (OpenShiftResource rsrc: resources) {
			if (resourceNameMatcher == null) {
				return true;
			}
			if (resourceNameMatcher.matches(treeViewerHandler.getNonStyledText(rsrc.getTreeItem()))) {
				if (!buildState.equals(ResourceState.UNSPECIFIED)) {
					return treeViewerHandler.getStyledTexts(rsrc.getTreeItem())[0].contains(buildState.toString());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		String matcherDescription = resourceNameMatcher == null ? "" : " matching resource name matcher " + 
			resourceNameMatcher.toString(); 
		return "Waiting for resource " + resource.toString() + matcherDescription;  
	}
}
