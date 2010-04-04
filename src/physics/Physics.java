/* This class gives a higher-level interface access to the numerical integration.
 * It references the global array found in BarraCUDA.java, and it's responsible for building and managing the octree data structure.
 */
package physics;

import java.util.*;

import main.BarraCUDA;

import util.OctTreeInternalNode;
import util.OctTreeLeafNode;
import util.Vector;

public class Physics 
{
	public static double eps = 0.001; //perturbation constant
	public static double eps2 = eps*eps; 
	public static double tol = 0.50; //should be less than 0.57. tolerance for stopping recursion
	public static double itol2 = 1.0/ (tol*tol);
	public double rootDiameter = 2000.0; //the maximum width of the "universe"
	public Vector rootCenter = new Vector(0,0,0); //the literal center of the "universe"
	public static double GRAPHICS_EFIELD_SCALE_FACTOR = 10000;
	public NumericalIntegration Integrator;

	public Physics()
	{
		this.Integrator = new NumericalIntegration();
	}
	

	//used as a metric to test the accuracy of the simulation...
	//because efield is a conservative field, this shouldn't change much
	//between diff. iterations.

	public Vector updateMomentumChecksum()
	{
		Vector totalMomentum = new Vector(0,0,0);
		for(int i = 0; i < BarraCUDA.NUM_PARTICLES; i++)
		{
			OctTreeLeafNode currentNode = BarraCUDA.bodyManager[i];
			totalMomentum = totalMomentum.add(currentNode.pointCharge.myState.momentum);
		}
		return totalMomentum;
	}	
	
	public void updateSimulation(double t, double dt) 
	{
		OctTreeInternalNode root = new OctTreeInternalNode(rootCenter, rootDiameter*0.5f);
		//rebuild the tree
		double radius = rootDiameter * 0.5;
		for (int i = 0; i < BarraCUDA.NUM_PARTICLES; i++) 
		{
			root.insert(BarraCUDA.bodyManager[i], radius);
		}

		//update all of the information in the internal nodes
		root.computeCenterOfCharge();

		//update the forces on each particle
		for(int i = 0; i < BarraCUDA.NUM_PARTICLES; i++)
		{
			BarraCUDA.bodyManager[i].computeForce(root, radius);
		}

		//integrate the particles
		for(int i = 0; i < BarraCUDA.NUM_PARTICLES; i++)
		{	
			PointCharge pc = BarraCUDA.bodyManager[i].pointCharge;
			Integrator.integrate(pc.myState, t, dt);
		}
	}

}
