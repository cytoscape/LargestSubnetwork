# Largest Subnetwork App for Cytoscape

Largest Subnetwork App is a Cytoscape app. It provides the functionality to select the largest connected subnetwork in the current network.

This simple app adds a menu option under ***Select > Nodes*** to select the ***Largest Subnetwork*** in the current network. This operation is useful when working with a partitioned network consisting of two or more distinctly connected graphs. Partition networks are common following MCL clustering, for example, or from importing interactions from databases like STRING.

The selection operation is supported in automation use cases as well. The basic command syntax is `network select subnetwork`. You can optionally specify a network with name or SUID with the `network` parameter. You can optionally create a new subnetwork with the `createSubnetwork` parameter. You can also optionally select subnetwork with a must-contain node with the `containNode` parameter
