package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.LargestConnectedComponent.LargestConnectedComponentTaskFactory;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.NetworkViewTaskFactory;

import java.util.Properties;



public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		LargestConnectedComponentTaskFactory largestConnectedComponentTaskFactory = new LargestConnectedComponentTaskFactory();
		Properties largestConnectedComponentTaskFactoryProps = new Properties();
		largestConnectedComponentTaskFactoryProps.setProperty("preferredMenu","Apps.LargestConnectedComponent");
		largestConnectedComponentTaskFactoryProps.setProperty("title","Select Largest Connected Component");
		registerService(bc,largestConnectedComponentTaskFactory,NetworkViewTaskFactory.class, largestConnectedComponentTaskFactoryProps);
	}
}
