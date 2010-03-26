package util;

public class OctTreeInternalNode extends OctTreeNode 
{
	Vector centerOfCharge = new Vector(0,0,0);
	double sumCharge;
	OctTreeNode[] child = new OctTreeNode[8];

	public static OctTreeInternalNode newNode(Vector pos) 
	{
		OctTreeInternalNode in;
		in = new OctTreeInternalNode();

		in.charge = 0.0;
		in.position = pos;
		for (int i = 0; i < 8; i++) in.child[i] = null;

		return in;
	}

	public void insert(OctTreeLeafNode b, double r) 
	{

		//the next bit of code cleverly determines which quadrant to place into
		int i = 0;
		double x = 0.0, y = 0.0, z = 0.0;

		if (position.x < b.position.x) {
			i = 1;
			x = r;
		}
		if (position.y < b.position.y) {
			i += 2;
			y = r;
		}
		if (position.z < b.position.z) {
			i += 4;
			z = r;
		}

		if (child[i] == null) 
		{
			child[i] = b;
		} 
		else if (child[i] instanceof OctTreeInternalNode) 
		{
			((OctTreeInternalNode) (child[i])).insert(b, 0.5 * r);
		} 
		else 
		{
			double rh = 0.5 * r;
			OctTreeInternalNode cell = newNode(new Vector(position.x - rh + x, position.y - rh + y, position.z - rh + z));
			cell.insert(b, rh);
			cell.insert((OctTreeLeafNode) (child[i]), rh);
			child[i] = cell;
		}
	}
	// recursively summarizes info about subtrees
	public void computeCenterOfCharge() 
	{
		OctTreeNode ch;
		sumCharge = 0.0;

		for(int i = 0; i < 8; i++)
		{
			ch = child[i];
			if(ch != null)
			{
				if(ch instanceof OctTreeLeafNode)
				{
					//System.out.println("Encountered a leaf node in the process of finding center of charge.");
					//System.out.println("This node has charge " + ch.charge + " and position " + ch.position);
					double c = ch.charge;
					
					sumCharge += c;
					//System.out.println("current sumCharge: " + sumCharge);
					centerOfCharge = centerOfCharge.add(ch.position.scale(c));
				}
				else
				{
					((OctTreeInternalNode) ch).computeCenterOfCharge();
				}
			}        
		}
		if(sumCharge != 0)	
		{
			centerOfCharge = centerOfCharge.scale(1.0 / sumCharge);
		}
	}
}


