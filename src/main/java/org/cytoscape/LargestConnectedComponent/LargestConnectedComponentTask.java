package org.cytoscape.LargestConnectedComponent;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.awt.EventQueue;

import org.cytoscape.task.AbstractNetworkViewTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.view.layout.PartitionUtil;
import org.cytoscape.view.layout.LayoutPartition;




public class LargestConnectedComponentTask extends AbstractNetworkViewTask {
	protected List <LayoutPartition> partitionList = null;
	protected double current_size = 0;
	protected ArrayList<Double> partlist = new ArrayList<>();
	private void ShowMessage(String message) {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, message);
        }
    });
}

	LargestConnectedComponentTask(CyNetworkView v) {
		super(v);
	}

	public void run(TaskMonitor tm) {
		if(this.view == null){
			return;
		}
		partitionList = PartitionUtil.partition(this.view, false, null);
		for (LayoutPartition partition: partitionList) {
				current_size = (double)partition.size();
				partlist.add(current_size);
				Collections.sort(partlist, Collections.reverseOrder());
		}
		ShowMessage("Number of selected nodes are "+partlist);
	}
}
