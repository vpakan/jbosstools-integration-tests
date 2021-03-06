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

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;

public class AmountOfResourcesExists extends AbstractWaitCondition {

	private OpenShiftProject project;
	private Resource resource;
	private int amount;
	
	public AmountOfResourcesExists(Resource resource, int amount) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		project = explorer.getOpenShift3Connection().getProject();
		this.resource = resource;
		this.amount = amount;
	}

	@Override
	public boolean test() {
		return project.getOpenShiftResources(resource).size() == amount;
	}

	@Override
	public String description() {
		return "Waiting for existence of " + amount + " " + resource.toString() + " resource(s).";  
	}
}
