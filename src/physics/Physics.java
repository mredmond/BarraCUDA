package physics;

import java.util.*;

import util.Vector;

public class Physics 
{

	public ArrayList<PointCharge> chargeManager = new ArrayList<PointCharge>();
	
	public void addCharge(int id, float charge)
	{
		
	}
	
	public void removeCharge(int id)
	{
		
	}
	
	public void setChargePosition(int id, Vector position)
	{
		
	}
	
	public void setChargeVelocity(int id, Vector velocity)
	{
		
	}
	
	public void updateElectrofieldApproximation()
	{
		for(PointCharge pc1 : chargeManager)
		{
			//we want to know the electromagnetic field at each point charge (consider all OTHER pcs)
			//this is generated with coulomb's law
			ArrayList<PointCharge> otherCharges = new ArrayList<PointCharge>();
			otherCharges.addAll(chargeManager);
			otherCharges.remove(pc1.idNum);
			
			Vector sum = new Vector(0,0,0); //create the efield acting on one charge
			
			for(PointCharge pc2: otherCharges)
			{
				//necessary variables for eField calc
				Vector r = pc1.myState.position;
				Vector rHat = pc2.myState.position;
				double qi = pc2.myState.charge;
				
				Vector numerator = r.subtract(rHat).scale(qi);
				double inverseDenominator = Math.pow((r.subtract(rHat).length()), -3);
				
				sum = sum.add(numerator.scale(inverseDenominator)); //add up the other particles' effects
				
			}
			
			double scaleFactor = 8987551787.9979; //this is 1/(4*pi*e_0), roughly 9 x 10^9 
			
			Vector efield = sum.scale(scaleFactor);
			
			pc1.myState.setEfield(efield);
		}
	}
	
	
	public void update(float t, float dt) 
	{
		updateElectrofieldApproximation();
		integrate();
		recalculate();
	}
	
	
}
