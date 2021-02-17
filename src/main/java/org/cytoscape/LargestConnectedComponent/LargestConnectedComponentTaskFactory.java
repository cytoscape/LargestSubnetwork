package org.cytoscape.LargestConnectedComponent;

import org.cytoscape.task.AbstractNetworkViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;


public class LargestConnectedComponentTaskFactory extends AbstractNetworkViewTaskFactory {

	public TaskIterator createTaskIterator(CyNetworkView networkView) {
		return new TaskIterator(new LargestConnectedComponentTask(networkView));
	}
}
