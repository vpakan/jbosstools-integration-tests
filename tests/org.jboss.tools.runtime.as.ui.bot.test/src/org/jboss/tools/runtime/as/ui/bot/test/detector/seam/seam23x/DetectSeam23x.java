package org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23x;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectSeam23x extends DetectRuntimeTemplate {

	public static final String PATH_ID = "jboss-seam-2.3.x";
		
	public static final String VERSION = "2.3.3.Final-redhat-1";
	
	public static final String NAME = "jboss-seam-" + VERSION;
	
	@Override
	protected String getPathID() {
		return PATH_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime seam = new Runtime();
		seam.setName(NAME);
		seam.setType("SEAM");
		seam.setVersion(VERSION);
		seam.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()));
		return Arrays.asList(seam);
	}
}
