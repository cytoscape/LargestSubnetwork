package org.cytoscape.LargestSubnetwork;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JFrame;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;
import org.cytoscape.work.json.JSONResult;
import org.cytoscape.work.ObservableTask;
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
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.util.json.CyJSONUtil;
import org.cytoscape.task.create.NewNetworkSelectedNodesOnlyTaskFactory;
import org.cytoscape.work.swing.DialogTaskManager;
import static org.cytoscape.work.TaskMonitor.Level.*;

public class LargestConnectedComponentTask extends AbstractTask implements TunableValidator, ObservableTask{
  private CyServiceRegistrar serviceRegistrar;
  private CyApplicationManager applicationManager;
  private CyNetworkViewManager cynetworkviewmanager;
  private CySwingApplication swingApplication;
  private CyNetworkView view;
  private CyNetwork networks;
  private StringToModel stringToModel;
  private List < LayoutPartition > partitionList = null;
  private List < LayoutNode > layoutNodeList = new ArrayList < >();
  private List < LayoutNode > tempNodeList = new ArrayList < >();
  private List < LayoutNode > secondLargestNodeList = new ArrayList < >();
  private List < LayoutNode > largestNodeList = new ArrayList < >();
  private ArrayList < Double > partlist = new ArrayList < >();
  private List < CyNode > res = new ArrayList < >();
  private List < CyNode > temp = new ArrayList < >();
  private List < CyNode > selectNode = new ArrayList < >();
  private CyNode eachNode;
  private CyNode startNode;
  private Collection<CyNetworkView> viewCollection;

  @Tunable(description = "Network to select?", context="nogui",
         exampleStringValue=StringToModel.CY_NETWORK_EXAMPLE_STRING,
         longDescription=StringToModel.CY_NETWORK_LONG_DESCRIPTION)
  public String network = null;

  @Tunable(description = "Create subnetwork?", context="nogui", exampleStringValue="false",
          longDescription="If true, new subnetwork will be created.")
  public Boolean createSubnetwork = false;

  @Tunable(description = "Must contain this node?", context="nogui", exampleStringValue="suid:114",
          longDescription="Selects a node by name, or, if the parameter has the prefix suid:, selects a node by SUID.")
  public String includesNode = null;

  // Method from Cytoscape Filters Impl (filter-impl)
  static void setSelectedState(CyNetwork networks, Collection < ?extends CyIdentifiable > list, Boolean selected) {
    for (CyIdentifiable edge: list) {
      CyRow row = networks.getRow(edge);
      row.set(CyNetwork.SELECTED, selected);
    }
  }

  public LargestConnectedComponentTask(CyApplicationManager applicationManager, CyNetworkView view, CyNetwork networks, CySwingApplication swingApplication, StringToModel stringToModel, CyNetworkViewManager cynetworkviewmanager, CyServiceRegistrar serviceRegistrar) {
    this.applicationManager = applicationManager;
    this.view = view;
    this.networks = networks;
    this.swingApplication = swingApplication;
    this.stringToModel = stringToModel;
    this.cynetworkviewmanager = cynetworkviewmanager;
    this.serviceRegistrar = serviceRegistrar;
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
    // Edge case: if there is no node, stop
    if (partitionList.size() == 0) {
      tm.showMessage(WARN, "Empty network!");
      return;
    }
    // Edge case: if we only have one partition, select all nodes
    if (partitionList.size() == 1) {
      setSelectedState(networks, CyTableUtil.getNodesInState(networks, CyNetwork.SELECTED, false), true);
      res = CyTableUtil.getNodesInState(networks, CyNetwork.SELECTED, true);
    } else {
            // Save all partitions in a nested list
            for (LayoutPartition partition: partitionList) {
              layoutNodeList = partition.getNodeList();
              nestedList.add(layoutNodeList);
            }
            if (includesNode == null){
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
              tm.showMessage(WARN, "There is more than one largest connected component. One was selected randomly.");
            }
          } else {
            selectNode = stringToModel.getNodeList(networks, includesNode);
            if (selectNode.size() == 0) {
            tm.showMessage(ERROR, "No nodes found. Please enter correct node.");
            return;
            }
            if (selectNode.size() != 1) {
            tm.showMessage(ERROR, "Only one node can be included. Please only enter one node.");
            return;
            }
            startNode = selectNode.get(0);
            List < CyNode > tempList = new ArrayList < >();
            search:{
              for (List < LayoutNode > tempNodeList:nestedList){
                for (LayoutNode layoutNode:tempNodeList){
                  eachNode = layoutNode.getNode();
                  tempList.add(eachNode);
                }
                if (tempList.contains(startNode)) {
                  res = tempList;
                  break search;
                } else {
                  tempList.clear();
                }
              }
            }
            setSelectedState(networks, res, true);
          }
          }
    if (createSubnetwork) {
      DialogTaskManager taskManager = serviceRegistrar.getService(DialogTaskManager.class);
      NewNetworkSelectedNodesOnlyTaskFactory factory = serviceRegistrar.getService(NewNetworkSelectedNodesOnlyTaskFactory.class);
      taskManager.execute(factory.createTaskIterator(networks));
        }
      }

      @Override
      public ValidationState getValidationState(Appendable errMsg) {
        return ValidationState.OK;
      }

      public Object getResults(Class type) {
    		List<CyIdentifiable> identifiables = new ArrayList<>();
    		if (res != null)
    			identifiables.addAll(res);
    		if (type.equals(List.class)) {
    			return identifiables;
    		} else if (type.equals(String.class)){
    			if (res.size() == 0)
    				return "<none>";
    			String ret = "";
    			if (res != null && res.size() > 0) {
    				ret += "Nodes selected: \n";
    				for (CyNode node: res) {
    					ret += "   "+networks.getRow(node).get(CyNetwork.NAME, String.class)+"\n";
    				}
    			}
    			return ret;
    		}  else if (type.equals(JSONResult.class)) {
    			JSONResult resJson = () -> {
    				if (identifiables == null || identifiables.size() == 0) {
    					return "{}";
    				} else {
    					CyJSONUtil cyJSONUtil = serviceRegistrar.getService(CyJSONUtil.class);
    					String result = "{\"nodes\":";
    					if (res == null || res.size() == 0)
    						result += "[]";
    					else
    						result += cyJSONUtil.cyIdentifiablesToJson(res);
    					return result + "}";
    				}
    			};
    			return resJson;
    		}
    		return identifiables;
    	}

    	@Override
    	public List<Class<?>> getResultClasses() {
    		return Arrays.asList(String.class, List.class, JSONResult.class);
    	}
}
