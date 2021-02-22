package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;


public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {
		private CyApplicationManager applicationManager;
			public LargestConnectedComponentTaskFactory(CyApplicationManager applicationManager){
					this.applicationManager = applicationManager;
				}
				public TaskIterator createTaskIterator(){
					return new TaskIterator(new LargestConnectedComponentTask(this.applicationManager.getCurrentNetworkView(), this.applicationManager.getCurrentNetwork()));
				}
}
