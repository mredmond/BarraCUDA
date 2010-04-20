package util;

public class OctTreeInternalNode extends OctTreeNode 
{
	Vector centerOfCharge = new Vector(0,0,0);
	double cumulativeCharge;
	OctTreeNode[] child = new OctTreeNode[8]; //an array of its children

	public OctTreeInternalNode(Vector _position, double _radius) 
	{
		this.position = _position;
		this.radius = _radius;
		this.centerOfCharge = new Vector(0,0,0);
		this.cumulativeCharge = 0;
		for (int i = 0; i < 8; i++) 
		{
			this.child[i] = null;
		}
	}

	public void insert(OctTreeLeafNode b, double r) 
	{
		//the next bit of code cleverly determines which quadrant to place into
		int i = 0;
		double x = 0.0, y = 0.0, z = 0.0;

		if (position.x <= b.position.x) {
			i = 1;
			x = r;
		}
		if (position.y <= b.position.y) {
			i += 2;
			y = r;
		}
		if (position.z <= b.position.z) {
			i += 4;
			z = r;
		}

		
		//if nothing's there yet, stick a new leaf node in.
		if (child[i] == null) 
		{
			b.radius = 0.5*r;
			child[i] = b;
		} 
		
		//if we have an internal node already, recursively stick ourselves in.
		else if (child[i] instanceof OctTreeInternalNode) 
		{
			((OctTreeInternalNode) (child[i])).insert(b, 0.5 * r);
		} 
		
		//else, we need to shuffle some nodes around to get this one to fit.
		else 
		{
			double rh = 0.5 * r;
			OctTreeInternalNode cell = new OctTreeInternalNode(new Vector(position.x - rh + x, position.y - rh + y, position.z - rh + z), rh);
			cell.insert(b, rh);
			cell.insert((OctTreeLeafNode) (child[i]), rh);
			child[i] = cell;
		}
	}
	
	public String toString()
	{
		return "center of charge: " + centerOfCharge + "\n cumulativeCharge: " + cumulativeCharge;
	}
	
	
	// recursively summarizes info about subtrees. 
	//this method sets the cumulative charge and the center of charge.
	public void computeCenterOfCharge() 
	{
		OctTreeNode ch;

		for(int i = 0; i < 8; i++)
		{
			ch = child[i];
			if(ch != null)
			{
				if(ch instanceof OctTreeLeafNode)
				{
					OctTreeLeafNode chl = (OctTreeLeafNode) ch;
					//System.out.println("Encountered a leaf node in the process of finding center of charge.");
					//System.out.println("This node has charge " + ch.charge + " and position " + ch.position);
					double c = chl.pointCharge.myState.charge;		
					cumulativeCharge += c;
					//System.out.println("current sumCharge: " + sumCharge);
					centerOfCharge = centerOfCharge.add(chl.position.scale(c));
				}
				else
				{
					OctTreeInternalNode ich = (OctTreeInternalNode) ch;
					ich.computeCenterOfCharge();
					cumulativeCharge += ich.cumulativeCharge;
					centerOfCharge = centerOfCharge.add(ich.centerOfCharge.scale(ich.cumulativeCharge));
				}
			}        
		}
		
		centerOfCharge = centerOfCharge.scale(1.0 / cumulativeCharge);

	}
}


