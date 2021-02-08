package org.cytoscape.LargestConnected;

import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;

import java.util.Properties;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.NetworkViewTaskFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.osgi.framework.BundleContext;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		CyLayoutAlgorithmManager layoutManager = getService(bc, CyLayoutAlgorithmManager.class);
		LargestConnectedTaskFactory largestConnectedTaskFactory = new LargestConnectedTaskFactory(layoutManager);
		Properties applyCustomLayoutProperties = new Properties();
		applyCustomLayoutProperties.setProperty(PREFERRED_MENU, "Apps.LargestConnectedComponent");
		applyCustomLayoutProperties.setProperty(TITLE, "Select Largest Connected Component");
		registerService(bc, largestConnectedTaskFactory, NetworkViewTaskFactory.class, applyCustomLayoutProperties);
	}
}
