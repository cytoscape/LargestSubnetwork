package org.cytoscape.LargestConnectedComponent;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.util.Collection;
import java.util.Comparator;



import org.cytoscape.work.AbstractTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.view.layout.PartitionUtil;
import org.cytoscape.view.layout.LayoutPartition;
import org.cytoscape.view.layout.LayoutNode;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.filter.internal.filters.util.SelectUtil;
import org.cytoscape.application.CyApplicationManager;



public class LargestConnectedComponentTask extends AbstractTask {

	protected List <LayoutPartition> partitionList = null;
	private CyNetworkView view;
	private CyNetwork network;
	private List<LayoutNode> layoutNodeList = new ArrayList<>();
	private List<LayoutNode> largestNodeList = new ArrayList<>();
	private CyApplicationManager applicationManager;
	private List<CyNode> res = new ArrayList<>();;
	private CyNode eachNode;
	private CyNode testNode;
	protected ArrayList<Double> partlist = new ArrayList<>();

	private void ShowMessage(String message) {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, message);
        }
    });
}

	public LargestConnectedComponentTask(CyNetworkView view, CyNetwork network) {
		this.view = view;
		this.network = network;
	}

	public void run(TaskMonitor tm) {
		if(view == null){
			return;
		}
		List<List<LayoutNode>> nestedList = new ArrayList<>();
		partitionList = PartitionUtil.partition(view, false, null);
		for (LayoutPartition partition: partitionList) {
				layoutNodeList = partition.getNodeList();
				nestedList.add(layoutNodeList);
		}
		Collections.sort(nestedList, new Comparator<List<LayoutNode>>(){
    public int compare(List<LayoutNode> a1, List<LayoutNode> a2) {
        return a2.size() - a1.size();
    	}
		});
		largestNodeList = nestedList.get(0);
		for (LayoutNode layoutNode: largestNodeList) {
			eachNode = layoutNode.getNode();
			res.add(eachNode);
		}
		int resSize = res.size();
		testNode = res.get(2);
		ShowMessage("The largest connected component has " + resSize + " nodes");
	}
}
