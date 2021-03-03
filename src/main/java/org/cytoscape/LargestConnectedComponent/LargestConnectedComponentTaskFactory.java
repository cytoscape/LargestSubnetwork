package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.StringToModel;

public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {
  private CyApplicationManager applicationManager;
  private CySwingApplication swingApplication;
  private StringToModel stringToModel;
  public LargestConnectedComponentTaskFactory(CyApplicationManager applicationManager, CySwingApplication swingApplication, StringToModel stringToModel) {
    this.applicationManager = applicationManager;
    this.swingApplication = swingApplication;
    this.stringToModel = stringToModel;
  }
  public TaskIterator createTaskIterator() {
    return new TaskIterator(new LargestConnectedComponentTask(this.applicationManager.getCurrentNetworkView(), this.applicationManager.getCurrentNetwork(), this.swingApplication, this.stringToModel));
  }
}
