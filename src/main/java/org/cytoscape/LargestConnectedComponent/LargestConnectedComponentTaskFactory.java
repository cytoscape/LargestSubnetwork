package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.StringToModel;
import org.cytoscape.view.model.CyNetworkViewManager;


public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {
  private CyApplicationManager applicationManager;
  private CySwingApplication swingApplication;
  private StringToModel stringToModel;
  private CyNetworkViewManager cynetworkviewmanager;

  public LargestConnectedComponentTaskFactory(CyApplicationManager applicationManager, CySwingApplication swingApplication, StringToModel stringToModel, CyNetworkViewManager cynetworkviewmanager) {
    this.applicationManager = applicationManager;
    this.swingApplication = swingApplication;
    this.stringToModel = stringToModel;
    this.cynetworkviewmanager = cynetworkviewmanager;
  }
  public TaskIterator createTaskIterator() {
    return new TaskIterator(new LargestConnectedComponentTask(this.applicationManager, this.applicationManager.getCurrentNetworkView(), this.applicationManager.getCurrentNetwork(), this.swingApplication, this.stringToModel, this.cynetworkviewmanager));
  }
}
