/* This class gives a higher-level interface access to the numerical integration.
 * It holds a chargeManager identical to the one found in the BarraCUDA.java class, and all of its operations
 * are done on this arrayList. Most of the methods are self-explanatory, but updateElectorFieldApproximation is not.
 * This method basically uses Coulomb's Law to determine the force on each particle (taking into consideration the effects of all of the other particles)
 * and will also do (very very very basic) collision detection. The collision detection is merely there to prevent weird things like interpenetration from happening 
 * (and they still do happen pretty frequently, so ... yeah).
 */
package physics;

import java.util.*;

import main.BarraCUDA;

import util.OctTreeInternalNode;
import util.OctTreeNode;
import util.OctTreeLeafNode;
import util.Vector;

public class Physics 
{
	public static double eps = 1;
	public static double eps2 = eps*eps;
	public static double tol = 0.35; //should be less than 0.57 apparently? //tolerance for stopping recursion
	public static double itol2 = 1.0/ (tol*tol);
	public double rootDiameter = 2.0E5;
	public Vector rootCenter = new Vector(0,0,0);
	public double GRAPHICS_EFIELD_SCALE_FACTOR = 10000; //this is roughly analogous to the constant value K, except instead of 9 x 10^9, I use a smaller value
	public ArrayList<PointCharge> chargeManager;
	public OctTreeLeafNode[] bodyManager;
	public NumericalIntegration Integrator;

	public Physics(ArrayList<PointCharge> chargeManagerIn, OctTreeLeafNode[] bodyManagerIn)
	{
		this.chargeManager = chargeManagerIn;
		this.bodyManager = bodyManagerIn;
		this.Integrator = new NumericalIntegration();
	}
	
	public void updateBodyManager(OctTreeLeafNode[] bodyManagerIn)
	{
		this.bodyManager = bodyManagerIn;
	}

	public void addCharge(int id, double charge, double mass, double radius)
	{
		chargeManager.add(id, new PointCharge(id, charge, mass, radius));
	}

	public void removeCharge(int id)
	{
		chargeManager.remove(id);
	}

	public void initializeChargePosition(int id, Vector positionIn)
	{
		chargeManager.get(id).myState.position = positionIn;
	}

	public void initializeChargeMomentum(int id, Vector momentumIn)
	{
		chargeManager.get(id).myState.momentum = momentumIn;
	}

	public void initializeEField(int id, Vector efieldIn)
	{
		chargeManager.get(id).myState.efield = efieldIn;
	}


	//used as a metric to test the accuracy of the simulation...
	//because efield is a conservative field, this shouldn't change much
	//between diff. iterations.

	public Vector updateMomentumChecksum()
	{
		Vector totalMomentum = new Vector(0,0,0);
		for(PointCharge pc1 : chargeManager)
		{
			totalMomentum = totalMomentum.add(pc1.myState.momentum);
		}
		return totalMomentum;
	}	
	public double updateEnergyTotalChecksum()
	{
		double totalEnergy = 0;
		totalEnergy += updatePotentialEnergy();
		totalEnergy += updateKineticEnergy();
		return totalEnergy;

	}	
	public double updatePotentialEnergy()
	{
		//computes potential energy U = 1/(4*pi*e_0) * (pairwise sum over particle's charge/distance)
		double prescaledEnergy = 0;
		for(PointCharge pc1: chargeManager)
		{
			for(PointCharge pc2: chargeManager)
			{
				if(!pc1.equals(pc2))
				{
					prescaledEnergy += (pc1.myState.charge * pc2.myState.charge)/((pc1.myState.position.subtract(pc2.myState.position)).length());
				}
			}
		}

		prescaledEnergy /= 2; //divided by two because each pair is actually counted twice.
		return GRAPHICS_EFIELD_SCALE_FACTOR*prescaledEnergy;

	}
	public double updateKineticEnergy()
	{
		double prescaledEnergy = 0;
		for(PointCharge pc : chargeManager)
		{
			prescaledEnergy += 0.5 * pc.myState.mass * pc.myState.velocity.length() * pc.myState.velocity.length();
		}
		return prescaledEnergy;
	}
	public void updateSimulation(double t, double dt) 
	{
		OctTreeInternalNode root = OctTreeInternalNode.newNode(rootCenter);
		updateBodyManager(BarraCUDA.mainBodyManager);
		//is this correct? I think I'm having issues translating between two data structures.
		
		
		//build the tree
		double radius = rootDiameter * 0.5;
		for (int i = 0; i < BarraCUDA.NUM_PARTICLES; i++) 
		{
			root.insert(bodyManager[i], radius);
		}

		root.computeCenterOfCharge();

		//update the forces
		for(int i = 0; i < BarraCUDA.NUM_PARTICLES; i++)
		{
			bodyManager[i].computeForce(root, rootDiameter);
		}

		//integrate the particles
		for(PointCharge pc : chargeManager)
		{
			Integrator.integrate(pc.myState, t, dt);
		}


		System.out.println("Momentum: " + updateMomentumChecksum() + " Magnitude: " + updateMomentumChecksum().length());
	}

}
