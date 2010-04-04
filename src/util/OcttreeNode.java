package util;

/* Every OctTreeNode needs to keep track of its position in space as well as its radius.
 * The two classes that extend OctTreeNode are OctTreeInternalNode and OctTreeLeafNode.
 * 
 * The internal node structure keeps data on the center and magnitude of charge of all of its children.
 * 
 * The leaf nodes contain a pointer to their PointCharge. These leaf nodes are also stored in an array in the main BarraCUDA class. 
 */


public abstract class OctTreeNode 
{
	public Vector position; //the center of the node
	public double radius; //the apothem distance to an edge
}

