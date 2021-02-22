package org.cytoscape.LargestConnectedComponent;

import java.util.Properties;

import org.cytoscape.LargestConnectedComponent.LargestConnectedComponentTaskFactory;
import org.osgi.framework.BundleContext;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}

	public void start(BundleContext bc) {
		final CyApplicationManager applicationManager = getService(bc, CyApplicationManager.class);
		final CySwingApplication swingApplication = getService(bc, CySwingApplication.class);
		LargestConnectedComponentTaskFactory largestConnectedComponentTaskFactory = new LargestConnectedComponentTaskFactory(applicationManager, swingApplication);
		Properties largestConnectedComponentTaskFactoryProps = new Properties();
		largestConnectedComponentTaskFactoryProps.setProperty("preferredMenu","Apps.LargestConnectedComponent");
		largestConnectedComponentTaskFactoryProps.setProperty("title","Select Largest Connected Component");
		registerService(bc, largestConnectedComponentTaskFactory, TaskFactory.class, largestConnectedComponentTaskFactoryProps);
	}
}
