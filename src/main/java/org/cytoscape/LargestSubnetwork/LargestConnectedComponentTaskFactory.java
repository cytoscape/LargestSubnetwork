package org.cytoscape.LargestSubnetwork;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.StringToModel;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.service.util.CyServiceRegistrar;


public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {
  private CyApplicationManager applicationManager;
  private CySwingApplication swingApplication;
  private StringToModel stringToModel;
  private CyNetworkViewManager cynetworkviewmanager;
  private CyServiceRegistrar serviceRegistrar;

  public LargestConnectedComponentTaskFactory(CyApplicationManager applicationManager, CySwingApplication swingApplication, StringToModel stringToModel, CyNetworkViewManager cynetworkviewmanager, CyServiceRegistrar serviceRegistrar) {
    this.applicationManager = applicationManager;
    this.swingApplication = swingApplication;
    this.stringToModel = stringToModel;
    this.cynetworkviewmanager = cynetworkviewmanager;
    this.serviceRegistrar = serviceRegistrar;
  }
  public TaskIterator createTaskIterator() {
    return new TaskIterator(new LargestConnectedComponentTask(this.applicationManager, this.applicationManager.getCurrentNetworkView(), this.applicationManager.getCurrentNetwork(), this.swingApplication, this.stringToModel, this.cynetworkviewmanager, this.serviceRegistrar));
  }
}
