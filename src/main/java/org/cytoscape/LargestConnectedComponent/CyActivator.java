package org.cytoscape.LargestConnectedComponent;

import static org.cytoscape.work.ServiceProperties.COMMAND;
import static org.cytoscape.work.ServiceProperties.COMMAND_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_EXAMPLE_JSON;
import static org.cytoscape.work.ServiceProperties.COMMAND_LONG_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_NAMESPACE;
import static org.cytoscape.work.ServiceProperties.COMMAND_SUPPORTS_JSON;
import static org.cytoscape.work.ServiceProperties.ENABLE_FOR;
import static org.cytoscape.work.ServiceProperties.ID;
import static org.cytoscape.work.ServiceProperties.IN_MENU_BAR;
import static org.cytoscape.work.ServiceProperties.IN_TOOL_BAR;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;

import java.util.Properties;

import org.cytoscape.LargestConnectedComponent.LargestConnectedComponentTaskFactory;
import org.osgi.framework.BundleContext;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.StringToModel;

public class CyActivator extends AbstractCyActivator {
  public CyActivator() {
    super();
  }

  public void start(BundleContext bc) {
    final CyApplicationManager applicationManager = getService(bc, CyApplicationManager.class);
    final CySwingApplication swingApplication = getService(bc, CySwingApplication.class);
    final StringToModel stringToModel = getService(bc, StringToModel.class);
    LargestConnectedComponentTaskFactory largestConnectedComponentTaskFactory = new LargestConnectedComponentTaskFactory(applicationManager, swingApplication, stringToModel);
    Properties largestConnectedComponentTaskFactoryProps = new Properties();
    largestConnectedComponentTaskFactoryProps.clear();
    largestConnectedComponentTaskFactoryProps.setProperty("preferredMenu", "Select.Nodes");
    largestConnectedComponentTaskFactoryProps.setProperty("title", "Select Largest Component");
    largestConnectedComponentTaskFactoryProps.setProperty(MENU_GRAVITY,"7.0");
    largestConnectedComponentTaskFactoryProps.put(COMMAND_NAMESPACE, "network");
		largestConnectedComponentTaskFactoryProps.put(COMMAND, "select component");
		largestConnectedComponentTaskFactoryProps.put(COMMAND_DESCRIPTION,  "Select the largest component on the current network.");
		largestConnectedComponentTaskFactoryProps.put(COMMAND_LONG_DESCRIPTION, "Select the largest connected component on the current network. If largest connected component is not unique, one will be selected randomly.");
    largestConnectedComponentTaskFactoryProps.put(COMMAND_EXAMPLE_JSON, "{   \"networkTitle\": \"galFiltered.sif (undirected)\",   \"nodeCount\": \"330\",  \"avNeighbors\": \"2.167\"}");
		largestConnectedComponentTaskFactoryProps.put(COMMAND_SUPPORTS_JSON, "true");
		largestConnectedComponentTaskFactoryProps.put(ENABLE_FOR, "network");
    registerService(bc, largestConnectedComponentTaskFactory, TaskFactory.class, largestConnectedComponentTaskFactoryProps);
  }
}
