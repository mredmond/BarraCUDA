package physics;

import util.State;

public class PointCharge 
{
	public State myState;
	public int idNum;
	
	public PointCharge(int idNum, float charge)
	{
		this.idNum = idNum;
		myState = new State(charge);
	}
	
}
