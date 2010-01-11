package physics;

import java.util.ArrayList;

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
	
	public void coulombElectrofieldApprox()
	{
		for(PointCharge pc : chargeManager)
		{
			//we want to know the electromagnetic field at each point charge
			//this is generated with coulomb's law
			
			
			
			pc.myState.setEfield(efield)
		}
	}
	
	
	public void update(float t, float dt) 
	{
		coulombElectrofieldApprox();
		integrate();
		recalculate();
	}
	
	
}
