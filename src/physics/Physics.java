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

	public void updateElectrofieldApproximation()
	{
		for(PointCharge pc1 : chargeManager)
		{
			//we want to know the electromagnetic field at each point charge (consider all OTHER pcs)
			//this is generated with coulomb's law

			Vector sum = new Vector(0,0,0); //create the efield acting on one charge

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
					double qi = pc2.myState.charge;

					Vector numerator = r.subtract(rHat).scale(qi);
					double inverseDenominator = Math.pow((r.subtract(rHat).length()), -3);

					sum = sum.add(numerator.scale(inverseDenominator)); //add up the other particles' effects
				}
			}

			pc1.myState.efield = sum;
		}
	}


	public void updateAll(double t, double dt) 
	{
		updateElectrofieldApproximation(); //just do this ONCE per update
		for(PointCharge pc : chargeManager)
		{
			System.out.println("Updating particle " + pc.idNum);
			Integrator.integrate(pc.myState, t, dt);
		}

	}


}
