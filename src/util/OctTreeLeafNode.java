package util;

import physics.Physics;
import physics.PointCharge;

public class OctTreeLeafNode extends OctTreeNode
{
	public PointCharge myPc;
	
	public OctTreeLeafNode(PointCharge pc)
	{
		this.myPc = pc;
		this.position = pc.myState.position;
		this.charge = pc.myState.charge;
	}
	
	public void setPC(PointCharge pc)
	{
		myPc = pc;
	}
	
	public void computeForce(OctTreeInternalNode root, double size)
	{
		recurseForce(root, size * size * Physics.itol2);
	}
	public void recurseForce(OctTreeNode n, double dsq)
	{
		double force;
		Vector dHat = n.position.subtract(this.position); //distance from this node to the next
		double drsq = dHat.length2(); 
		if (drsq < dsq) 
		{
			if (n instanceof OctTreeInternalNode) 
			{ 
				// n is a cell
				OctTreeInternalNode in = (OctTreeInternalNode) n;
				dsq *= 0.25;
				if (in.child[0] != null) 
				{
					recurseForce(in.child[0], dsq);
					if (in.child[1] != null) 
					{
						recurseForce(in.child[1], dsq);
						if (in.child[2] != null) 
						{
							recurseForce(in.child[2], dsq);
							if (in.child[3] != null) 
							{
								recurseForce(in.child[3], dsq);
								if (in.child[4] != null) 
								{
									recurseForce(in.child[4], dsq);
									if (in.child[5] != null) 
									{
										recurseForce(in.child[5], dsq);
										if (in.child[6] != null) 
										{
											recurseForce(in.child[6], dsq);
											if (in.child[7] != null) 
											{
												recurseForce(in.child[7], dsq);
											}
										}
									}
								}
							}
						}
					}
				}
			} 
			else 
			{ 
				// n is a body
				if (n != this) 
				{
					drsq += Physics.eps2; //perturbation
					double invRootDistance = 1 / Math.sqrt(drsq);
					force = n.charge * this.charge * invRootDistance * invRootDistance * invRootDistance;
					myPc.myState.efield.add(dHat.scale(force));
				}
			}
		} 
		else 
		{ 
			// node is far enough away, don't recurse any deeper
			drsq += Physics.eps2;
			double invRootDistance = 1 / Math.sqrt(drsq);
			force = n.charge * this.charge * invRootDistance * invRootDistance * invRootDistance;
			myPc.myState.efield.add(dHat.scale(force));
		}
	}
}
