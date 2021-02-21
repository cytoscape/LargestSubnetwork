package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.application.CyApplicationManager;


public class LargestConnectedComponentTaskFactory extends AbstractTaskFactory {


		private CyApplicationManager appMgr;

			public LargestConnectedComponentTaskFactory(CyApplicationManager appMgr){
					this.appMgr = appMgr;
				}

				public TaskIterator createTaskIterator(){
					return new TaskIterator(new LargestConnectedComponentTask(this.appMgr.getCurrentNetworkView(), this.appMgr.getCurrentNetwork()));
				}
}
