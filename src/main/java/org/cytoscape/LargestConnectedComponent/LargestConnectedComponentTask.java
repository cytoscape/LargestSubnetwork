package org.cytoscape.LargestConnectedComponent;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.util.Collection;
import java.util.Comparator;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.layout.PartitionUtil;
import org.cytoscape.view.layout.LayoutPartition;
import org.cytoscape.view.layout.LayoutNode;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyIdentifiable;


public class LargestConnectedComponentTask extends AbstractTask {

	protected List <LayoutPartition> partitionList = null;
	private final CyNetworkView view;
	private final CyNetwork network;
	private List<LayoutNode> layoutNodeList = new ArrayList<>();
	private List<LayoutNode> largestNodeList = new ArrayList<>();
	protected ArrayList<Double> partlist = new ArrayList<>();
	private CyApplicationManager applicationManager;
	private List<CyNode> res = new ArrayList<>();
	private List<CyNode> nodes = new ArrayList<>();
	private CyNode eachNode;

	private void ShowMessage(String message) {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, message);
        }
    });
}

	// Method from Cytoscape Filters Impl (filter-impl)
	static void setSelectedState(CyNetwork network, Collection<? extends CyIdentifiable> list, Boolean selected) {
		for (CyIdentifiable edge : list) {
			CyRow row = network.getRow(edge);
			row.set(CyNetwork.SELECTED, selected);
		}
	}

	public LargestConnectedComponentTask(CyNetworkView view, CyNetwork network) {
		this.view = view;
		this.network = network;
	}

	public void run(TaskMonitor tm) {
		if(view == null){
			return;
		}
		// Clear previous selections of nodes and edges
		setSelectedState(network, CyTableUtil.getNodesInState(network, CyNetwork.SELECTED, true), false);
		setSelectedState(network, CyTableUtil.getEdgesInState(network, CyNetwork.SELECTED, true), false);
		List<List<LayoutNode>> nestedList = new ArrayList<>();
		// Algorithm from layout-api PartitionUtil
		partitionList = PartitionUtil.partition(view, false, null);
		// Save all partitions in a nested list
		for (LayoutPartition partition: partitionList) {
				layoutNodeList = partition.getNodeList();
				nestedList.add(layoutNodeList);
		}
		// Sort the nested list and find the largest partition list
		Collections.sort(nestedList, new Comparator<List<LayoutNode>>(){
    public int compare(List<LayoutNode> a1, List<LayoutNode> a2) {
        return a2.size() - a1.size();
    	}
		});
		// Get the largest partition
		largestNodeList = nestedList.get(0);
		// Turn layoutNode into CyNode
		for (LayoutNode layoutNode: largestNodeList) {
			eachNode = layoutNode.getNode();
			res.add(eachNode);
		}
		int resSize = res.size();
		// Select the largest connected component
		setSelectedState(network, res, true);
		ShowMessage("The largest connected component has " + resSize + " nodes");
	}
}
