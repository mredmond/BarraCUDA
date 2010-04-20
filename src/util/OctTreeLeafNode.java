package util;

import physics.Physics;
import physics.PointCharge;

public class OctTreeLeafNode extends OctTreeNode
{
	public PointCharge pointCharge;
	
	public OctTreeLeafNode(PointCharge _pc)
	{
		this.pointCharge = _pc;
		this.position = pointCharge.myState.position;
	}
		
	
	//This wrapper method will walk down the entire tree to compute the force on this particular leaf node. 
	public void computeForce(OctTreeInternalNode root, double size)
	{
		recurseForce(root, size * size * Physics.itol2);
	}
	
	
	//This method computes the force on a leaf node recursively with the FMM model. 
	//Details on exactly how this is done are included in the paper.
	public void recurseForce(OctTreeNode n, double dsq)
	{
		double force;
		Vector dHat = n.position.subtract(this.position); //distance from this node to the next
		double drsq = dHat.length2(); 
		if (drsq < dsq) 
		{
			if (n instanceof OctTreeInternalNode) //it's not a leaf
			{ 
				OctTreeInternalNode in = (OctTreeInternalNode) n;
				dsq *= 0.25;
				if (in.child[0] != null) 
				{
					recurseForce(in.child[0], dsq);
				}
				if (in.child[1] != null) 
				{
					recurseForce(in.child[1], dsq);
				}
				if (in.child[2] != null) 
				{
					recurseForce(in.child[2], dsq);
				}
				if (in.child[3] != null) 
				{
					recurseForce(in.child[3], dsq);
				}
				if (in.child[4] != null) 
				{
					recurseForce(in.child[4], dsq);
				}
				if (in.child[5] != null) 
				{
					recurseForce(in.child[5], dsq);
				}
				if (in.child[6] != null) 
				{
					recurseForce(in.child[6], dsq);
				}	
				if (in.child[7] != null) 
				{
					recurseForce(in.child[7], dsq);
				}
			} 
			else 
			{ 
				//it's a leaf, so cast it to a leaf node
				OctTreeLeafNode ln = (OctTreeLeafNode) n;
				if (ln != this) 
				{
					drsq += Physics.eps2; //perturbation turned on or off here
					double invRootDistance = 1 / Math.sqrt(drsq);
					force = ln.pointCharge.myState.charge * this.pointCharge.myState.charge * invRootDistance * invRootDistance * invRootDistance * Physics.GRAPHICS_EFIELD_SCALE_FACTOR;
					
					pointCharge.myState.efield = pointCharge.myState.efield.add(dHat.scale(force));
//					System.out.println("Added efield vector " + dHat.scale(force) + " to pointCharge " + this.pointCharge.idNum);
				}
			}
		} 
		else 
		{ 
			// node is far enough away, don't recurse any deeper
			//drsq += Physics.eps2;  //perturbation turned on or off here, should probably be turned off at long range
			double invRootDistance = 1 / Math.sqrt(drsq);
			if(n instanceof OctTreeInternalNode) //n is a far away internal node
			{
				OctTreeInternalNode in = (OctTreeInternalNode) n;
				force = in.cumulativeCharge * this.pointCharge.myState.charge * invRootDistance * invRootDistance * invRootDistance * Physics.GRAPHICS_EFIELD_SCALE_FACTOR;
				pointCharge.myState.efield = pointCharge.myState.efield.add(dHat.scale(force));
				//System.out.println("Added efield vector " + dHat.scale(force) + " to pointCharge " + this.pointCharge.idNum);
			}
			else //n is a far away leaf
			{
				OctTreeLeafNode ln = (OctTreeLeafNode) n;
				force = ln.pointCharge.myState.charge * this.pointCharge.myState.charge * invRootDistance * invRootDistance * invRootDistance * Physics.GRAPHICS_EFIELD_SCALE_FACTOR;
				pointCharge.myState.efield = pointCharge.myState.efield.add(dHat.scale(force));
				//System.out.println("Added efield vector " + dHat.scale(force) + " to pointCharge " + this.pointCharge.idNum);
			}
		}
	}
}
