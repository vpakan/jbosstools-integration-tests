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
package org.jboss.tools.openshift.ui.bot.test.application.v3.adapter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.After;
import org.junit.Test;

public class ServerAdapterWizardHandlingTest extends AbstractCreateApplicationTest {

	@Test
	public void testPreselectedConnectionForNewOpenShift3ServerAdapter() {
		openNewServerAdapterWizard();
		
		assertTrue("There should be preselected an existing OpenShift 3 connection in new server adapter wizard.",
				new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).getSelection().contains(DatastoreOS3.USERNAME));
	}
	
	@Test
	public void testProjectSelectedInProjectExplorerIsPreselected() {
		new ProjectExplorer().selectProjects(projectName);
		
		openNewServerAdapterWizard();
		next();
		
		assertTrue("Selected project from workspace should be preselected",
				new LabeledCombo("Eclipse Project: ").getText().equals(projectName));
	}
	
	@Test
	public void testPodPathWidgetAccessibility() {
		openNewServerAdapterWizard();
		next();
		
		LabeledText podPath = new LabeledText("Pod Deployment Path: ");
		String podPathText = podPath.getText();
		podPath.setText("");
		
		assertFalse("Next button should be disable if pod path is empty is selected.",
				nextButtonIsEnabled());
		
		podPath.setText(podPathText);
		
		assertTrue("Next button should be reeenabled if pod path is correctly filled in.",
				nextButtonIsEnabled());
	}
	
	@Test
	public void testApplicationSelectionWidgetAccessibility() {
		openNewServerAdapterWizard();
		next();
		
		new DefaultTreeItem(DatastoreOS3.PROJECT1).select();
		
		assertFalse("Next button should be disable if no application is selected.",
				nextButtonIsEnabled());
		
		new DefaultTreeItem(DatastoreOS3.PROJECT1).getItems().get(0).select();
		
		assertTrue("Next button should be enabled if application for a new server adapter is created.",
				nextButtonIsEnabled());
	}
	
	@Test
	public void testFinishButtonAccessibility() {
		openNewServerAdapterWizard();
		
		assertFalse("Finish button should be disabled on new server "
				+ "adapter wizard page where selection of a connection is done, "
				+ "because there are still missing details to successfully create a new"
				+ "OpenShift 3 server adapter.", buttonIsEnabled(new FinishButton()));
	}
	
	@Test
	public void testSourcePathWidgetAccessibility() {
		openNewServerAdapterWizard();
		next();
		
		LabeledText srcPath = new LabeledText("Source Path: ");
		String srcPathText = srcPath.getText();
		srcPath.setText("");
		
		assertFalse("Next button should be disable if source path is empty is selected.",
				nextButtonIsEnabled());
		
		srcPath.setText(srcPathText);
		
		assertTrue("Next button should be reeenabled if source path is correctly filled in.",
				nextButtonIsEnabled());
		
		srcPath.setText("invalid path");
		
		assertFalse("Next button should be disabled if source path is invalid or not existing.",
				nextButtonIsEnabled());
		
		srcPath.setText(srcPathText);
		
		assertTrue("Next button should be reeenabled if source path is correctly filled in.",
				nextButtonIsEnabled());
	}
	
	private boolean nextButtonIsEnabled() {
		return buttonIsEnabled(new NextButton());
	}
	
	private boolean buttonIsEnabled(Button button) {
		try {
			new WaitUntil(new WidgetIsEnabled(button), TimePeriod.getCustom(5));
			return true;
		} catch (WaitTimeoutExpiredException ex) {
			return false;
		}
	}
	
	private void next() {
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new WidgetIsEnabled(new BackButton()));
	}
	
	private void openNewServerAdapterWizard() {
		NewServerWizardDialog dialog = new NewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		dialog.open();
		page.selectType(OpenShiftLabel.Others.OS3_SERVER_ADAPTER);
		dialog.next();
	}
	
	@After
	public void closeShell() {
		try {
			// if shell is opened, close it
			new DefaultShell(OpenShiftLabel.Shell.ADAPTER);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsActive(OpenShiftLabel.Shell.ADAPTER));
			new WaitWhile(new JobIsRunning());
		} catch (RedDeerException ex) {
			// do nothing
		}
	}
}
