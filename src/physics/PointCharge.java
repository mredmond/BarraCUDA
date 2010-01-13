package physics;

import util.State;

public class PointCharge 
{
	public State myState;
	public int idNum;
	
	public PointCharge(int idNum, double charge)
	{
		this.idNum = idNum;
		myState = new State(charge);
	}
	
	public String toString()
	{
		return "pointCharge" + idNum + "\n position: " + myState.position.toString() + "\n velocity: " + myState.velocity.toString();
	}
	
}