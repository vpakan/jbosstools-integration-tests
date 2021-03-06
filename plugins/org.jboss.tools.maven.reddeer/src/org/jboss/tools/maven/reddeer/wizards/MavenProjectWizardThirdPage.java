package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;

public class MavenProjectWizardThirdPage extends WizardPage{
	
	public void setGAV(String groupId, String artifactId, String version){
		new WaitUntil(new ButtonWithTextIsEnabled(new PushButton("Cancel")),TimePeriod.LONG); //wait for progressbar to finish
		new LabeledCombo("Group Id:").setText(groupId);
		new LabeledCombo("Artifact Id:").setText(artifactId);
		if(version!=null){
			new LabeledCombo("Version:").setText(version);
		}
	}

}
