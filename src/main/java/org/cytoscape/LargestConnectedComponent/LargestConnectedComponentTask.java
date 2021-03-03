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
import org.cytoscape.work.Tunable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.layout.PartitionUtil;
import org.cytoscape.view.layout.LayoutPartition;
import org.cytoscape.view.layout.LayoutNode;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.command.StringToModel;
import static org.cytoscape.work.TaskMonitor.Level.*;

public class LargestConnectedComponentTask extends AbstractTask {

  private CyApplicationManager applicationManager;
  private CyNetworkViewManager cynetworkviewmanager;
  private CySwingApplication swingApplication;
  private CyNetworkView view;
  private CyNetwork networks;
  private StringToModel stringToModel;
  private List < LayoutPartition > partitionList = null;
  private List < LayoutNode > layoutNodeList = new ArrayList < >();
  private List < LayoutNode > secondLargestNodeList = new ArrayList < >();
  private List < LayoutNode > largestNodeList = new ArrayList < >();
  private ArrayList < Double > partlist = new ArrayList < >();
  private List < CyNode > res = new ArrayList < >();
  private List < CyNode > nodes = new ArrayList < >();
  private CyNode eachNode;
  private Collection<CyNetworkView> viewCollection;

  @Tunable(description = "Network to select?", context="nogui",
         exampleStringValue=StringToModel.CY_NETWORK_EXAMPLE_STRING,
         longDescription=StringToModel.CY_NETWORK_LONG_DESCRIPTION)
  public String network = null;
  // Method from Cytoscape Filters Impl (filter-impl)
  static void setSelectedState(CyNetwork networks, Collection < ?extends CyIdentifiable > list, Boolean selected) {
    for (CyIdentifiable edge: list) {
      CyRow row = networks.getRow(edge);
      row.set(CyNetwork.SELECTED, selected);
    }
  }

  public LargestConnectedComponentTask(CyApplicationManager applicationManager, CyNetworkView view, CyNetwork networks, CySwingApplication swingApplication, StringToModel stringToModel, CyNetworkViewManager cynetworkviewmanager) {
    this.applicationManager = applicationManager;
    this.view = view;
    this.networks = networks;
    this.swingApplication = swingApplication;
    this.stringToModel = stringToModel;
    this.cynetworkviewmanager = cynetworkviewmanager;
  }

  public void run(TaskMonitor tm) {
    if (view == null) {
      return;
    }


    if (network == null){
      networks = networks;
      view = view;
    }
    else {
      networks = stringToModel.getNetwork(network);
      //applicationManager.setCurrentNetwork(networks);
      //view = applicationManager.getCurrentNetworkView();
      viewCollection = cynetworkviewmanager.getNetworkViews(networks);
      view = viewCollection.iterator().next();
    }
    // Clear previous selections of nodes and edges
    setSelectedState(networks, CyTableUtil.getNodesInState(networks, CyNetwork.SELECTED, true), false);
    setSelectedState(networks, CyTableUtil.getEdgesInState(networks, CyNetwork.SELECTED, true), false);
    List < List < LayoutNode >> nestedList = new ArrayList < >();
    // Algorithm from layout-api PartitionUtil
    partitionList = PartitionUtil.partition(view, false, null);
    // Edge case: if we only have one partition, select all nodes
    if (partitionList.size() == 1) {
      setSelectedState(networks, CyTableUtil.getNodesInState(networks, CyNetwork.SELECTED, false), true);
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
            setSelectedState(networks, res, true);
            // Warn users if we have multiple largest components
            if (largestSize == secondSize) {
              tm.setTitle("Largest connected component is not unique");
              tm.showMessage(INFO, "There is more than one largest connected component. One was selected randomly.");
            }
          }
      }
}
