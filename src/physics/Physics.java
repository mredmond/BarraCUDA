package physics;

import java.util.*;

import util.Vector;

public class Physics 
{

	public ArrayList<PointCharge> chargeManager;
	public NumericalIntegration Integrator;

	public Physics(ArrayList<PointCharge> chargeManagerIn)
	{
		this.chargeManager = chargeManagerIn;
		this.Integrator = new NumericalIntegration();
	}

	public void addCharge(int id, float charge)
	{
		chargeManager.add(id, new PointCharge(id, charge));
	}

	public void removeCharge(int id)
	{
		chargeManager.remove(id);
	}

	public void initializeChargePosition(int id, Vector positionIn)
	{
		chargeManager.get(id).myState.position = positionIn;
	}

	public void initializeChargeVelocity(int id, Vector velocityIn)
	{
		chargeManager.get(id).myState.velocity = velocityIn;
	}

	public void initializeEField(int id, Vector efieldIn)
	{
		chargeManager.get(id).myState.efield = efieldIn;
	}

	public void updateElectrofieldApproximation(double width)
	{
		//This method updates the efield vector for each point charge in the chargeManager.
		//It is used in the NumericalIntegration class for determining forces.
		for(PointCharge pc1 : chargeManager)
		{
			//create the efield acting on one charge
			Vector sum = new Vector(0,0,0); 

			for(PointCharge pc2: chargeManager)
			{
				if(pc2.idNum == pc1.idNum)
				{
					//skip this case ... don't want to add a particle to it's own e-field
				}
				else
				{
					//necessary variables for eField calc
					Vector r = pc1.myState.position;
					Vector rHat = pc2.myState.position;
					Vector rDiff = r.subtract(rHat); //a vector going from r to rHat
					double qi = pc2.myState.charge;

					if(rDiff.length() < 2*width) //only add if the particles are suitably far apart
					{
						//they're too close. set each one's efield and velocity vecs to zero
						pc1.myState.touchingOther = true;
						pc2.myState.touchingOther = true;
						System.out.println("Particle " + pc1.idNum + " is touching particle " + pc2.idNum + ".");
					}
					else
					{
						Vector numerator = rDiff.scale(qi);
						double inverseDenominator = Math.pow((rDiff.length()), -3);
						sum = sum.add(numerator.scale(inverseDenominator)); //add up the other particles' effects
						pc1.myState.efield = sum;
					}
				}
			}
			if(pc1.myState.touchingOther)
			{
				pc1.myState.efield.zero();
				pc1.myState.velocity.zero();
			}
		}
		
	}

	public void updateAll(double t, double dt, double width) 
	{
		updateElectrofieldApproximation(width); //just do this ONCE per update, otherwise you've got some problems
		for(PointCharge pc : chargeManager)
		{
			Integrator.integrate(pc.myState, t, dt);
		}

	}

}
