package physics;

import util.State;

public class PointCharge 
{
	public State myState;
	public int idNum;
	
	public PointCharge(int idNum, double charge, double mass)
	{
		this.idNum = idNum;
		myState = new State(charge, mass);
	}
	
	public String toString()
	{
		return "pointCharge" + idNum + "\n charge: " + myState.charge + "\n mass: " + myState.mass + "\n position: " + myState.position.toString() + "\n velocity: " + myState.momentum.toString() + "\n efield: " + myState.efield.toString() + "\n";
	}
	
}
