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
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;

public class OpenShiftProjectExists extends AbstractWaitCondition {

	private OpenShift3Connection connection; 
	private String projectName;
	
	/**
	 * Creates condition OpenShift project exists for a project with specified 
	 * name. Beware, if project has specified project display name, then use 
	 * project display name in place of project name, because it is shown 
	 * as a project name in OpenShift Explorer view.
	 * 
	 * @param projectName project name
	 * @param projectDisplayedName project displayed name
	 */
	public OpenShiftProjectExists(String projectName) {
		this.projectName = projectName;
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		connection = explorer.getOpenShift3Connection();
	}
	
	/**
	 * Creates condition OpenShift project exists for a project defined in {@link DatastoreOS3} as first one.
	 */
	public OpenShiftProjectExists() {
		this(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
	}
	
	@Override
	public boolean test() {
		return connection.projectExists(projectName);
	}
}
