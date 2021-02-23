package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {
  private CyApplicationManager applicationManager;
  private CySwingApplication swingApplication;
  public LargestConnectedComponentTaskFactory(CyApplicationManager applicationManager, CySwingApplication swingApplication) {
    this.applicationManager = applicationManager;
    this.swingApplication = swingApplication;
  }
  public TaskIterator createTaskIterator() {
    return new TaskIterator(new LargestConnectedComponentTask(this.applicationManager.getCurrentNetworkView(), this.applicationManager.getCurrentNetwork(), this.swingApplication));
  }
}
