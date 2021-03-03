package org.cytoscape.LargestConnectedComponent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import java.util.Collection;
import java.util.Comparator;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
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
import static org.cytoscape.work.TaskMonitor.Level.*;

public class LargestConnectedComponentTask extends AbstractTask {

  private CyApplicationManager applicationManager;
  private CySwingApplication swingApplication;
  private CyNetworkView view;
  private CyNetwork network;
  private List < LayoutPartition > partitionList = null;
  private List < LayoutNode > layoutNodeList = new ArrayList < >();
  private List < LayoutNode > secondLargestNodeList = new ArrayList < >();
  private List < LayoutNode > largestNodeList = new ArrayList < >();
  private ArrayList < Double > partlist = new ArrayList < >();
  private List < CyNode > res = new ArrayList < >();
  private List < CyNode > nodes = new ArrayList < >();
  private CyNode eachNode;
  // Method from Cytoscape Filters Impl (filter-impl)
  static void setSelectedState(CyNetwork network, Collection < ?extends CyIdentifiable > list, Boolean selected) {
    for (CyIdentifiable edge: list) {
      CyRow row = network.getRow(edge);
      row.set(CyNetwork.SELECTED, selected);
    }
  }

  public LargestConnectedComponentTask(CyNetworkView view, CyNetwork network, CySwingApplication swingApplication) {
    this.view = view;
    this.network = network;
    this.swingApplication = swingApplication;
  }

  public void run(TaskMonitor tm) {
    if (view == null) {
      return;
    }
    // Clear previous selections of nodes and edges
    setSelectedState(network, CyTableUtil.getNodesInState(network, CyNetwork.SELECTED, true), false);
    setSelectedState(network, CyTableUtil.getEdgesInState(network, CyNetwork.SELECTED, true), false);
    List < List < LayoutNode >> nestedList = new ArrayList < >();
    // Algorithm from layout-api PartitionUtil
    partitionList = PartitionUtil.partition(view, false, null);
    // Edge case: if we only have one partition
    if (partitionList.size() == 1) {
      setSelectedState(network, CyTableUtil.getNodesInState(network, CyNetwork.SELECTED, false), true);
    } else {
    // Save all partitions in a nested list
    for (LayoutPartition partition: partitionList) {
      layoutNodeList = partition.getNodeList();
      nestedList.add(layoutNodeList);
    }
    // Sort the nested list and find the largest partition list
    Collections.sort(nestedList, new Comparator < List < LayoutNode >> () {
      public int compare(List < LayoutNode > a1, List < LayoutNode > a2) {
        return a2.size() - a1.size();
      }
    });
    // Get the largest partition
    largestNodeList = nestedList.get(0);
    // Get the second largest partition
    secondLargestNodeList = nestedList.get(1);
    // Get the largest partition size
    int largestSize = largestNodeList.size();
    // Get the second largest partition size
    int secondSize = secondLargestNodeList.size();
    // Turn layoutNode into CyNode
    for (LayoutNode layoutNode: largestNodeList) {
      eachNode = layoutNode.getNode();
      res.add(eachNode);
    }
    // Select the largest connected component
    setSelectedState(network, res, true);
    // Warn users if we have multiple largest components
    if (largestSize == secondSize) {
      tm.setTitle("Largest connected component is not unique");
      tm.showMessage(INFO, "There is more than one largest connected component. One was selected randomly.");
      }
    }
  }
}
