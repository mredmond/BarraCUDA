/* This class gives a higher-level interface access to the numerical integration.
 * It holds a chargeManager identical to the one found in the BarraCUDA.java class, and all of its operations
 * are done on this arrayList. Most of the methods are self-explanatory, but updateElectorFieldApproximation is not.
 * This method basically uses Coulomb's Law to determine the force on each particle 
 * (taking into consideration the effects of all of the other particles)
 */
package physics;

import java.util.*;
import util.Vector;
import main.BarraCUDA;
public class Physics 
{
	public double eps = 1; //half the radius of the particles
	public double GRAPHICS_EFIELD_SCALE_FACTOR = 10000; 
	//this is roughly analogous to the constant value K, except instead of 9 x 10^9, I use a smaller value
	public NumericalIntegration Integrator;

	public Physics()
	{
		this.Integrator = new NumericalIntegration();
	}

	public void updateElectrofieldApproximation()
	{
		//This method updates the efield vector for each point charge in the chargeManager.
		//It is used in the NumericalIntegration class for determining forces.
		Iterator<PointCharge> outerIter = BarraCUDA.mainChargeManager.iterator();
		while(outerIter.hasNext())
		{
			PointCharge pc1 = outerIter.next();
			//create the efield acting on one charge
			Vector sum = new Vector(0,0,0); 
			Iterator<PointCharge> innerIterator = BarraCUDA.mainChargeManager.iterator();
			while(innerIterator.hasNext())
			{
				PointCharge pc2 = (PointCharge) innerIterator.next();
				if(pc2.idNum != pc1.idNum) //don't want to add a particle to its own e-field
				{

					//necessary variables for eField calc
					Vector r = pc1.myState.position;
					Vector rHat = pc2.myState.position;
					Vector rDiff = r.subtract(rHat); //a vector going from r to rHat
					double qi = pc2.myState.charge;

					double inverseDenominator = Math.pow((rDiff.length() + eps), -3);
					sum = sum.add(rDiff.scale(qi * inverseDenominator)); //add up the other particles' effects
					pc1.myState.efield = sum.scale(GRAPHICS_EFIELD_SCALE_FACTOR); 
					//arbitrary scale factor to make graphics work. 
				}
			}
		}

	}


	//used as a metric to test the accuracy of the simulation...
	//because efield is a conservative field, this shouldn't change much
	//between diff. iterations.

	public Vector updateMomentumChecksum()
	{
		Vector totalMomentum = new Vector(0,0,0);
		for(PointCharge pc1 : BarraCUDA.mainChargeManager)
		{
			totalMomentum = totalMomentum.add(pc1.myState.momentum);
		}
		return totalMomentum;
	}	

	public void updateAll(double t, double dt) 
	{
		updateElectrofieldApproximation();
		for(PointCharge pc : BarraCUDA.mainChargeManager)
		{
			Integrator.integrate(pc.myState, t, dt);
		}
	}

}
