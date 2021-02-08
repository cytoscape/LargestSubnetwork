package org.cytoscape.LargestConnected;

import org.cytoscape.task.NetworkViewTaskFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;


public class LargestConnectedTaskFactory implements NetworkViewTaskFactory {
	private CyLayoutAlgorithmManager layoutManager;

	public LargestConnectedTaskFactory(CyLayoutAlgorithmManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	public TaskIterator createTaskIterator(CyNetworkView view) {
		CyLayoutAlgorithm layout = layoutManager.getLayout("force-directed");
		Object context = layout.createLayoutContext();
		String layoutAttribute = null;
		return layout.createTaskIterator(view, context, CyLayoutAlgorithm.ALL_NODE_VIEWS, layoutAttribute);
	}

	public boolean isReady(CyNetworkView view) {
		return view != null;
	};
}
