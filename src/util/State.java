package util;

public class State {
	// primary
	public Vector position;
	public Vector velocity;
	public float efield; //the value of the electric field at this position

	// constant
	public float charge;


	public State() {}

	public State(float charge) 
	{
		this.position = new Vector();
		this.velocity = new Vector();
		this.charge = charge;
	}

	public void setEfield(float efield)
	{
		this.efield = efield;
	}
	
	// interpolation used for animating inbetween states
	public State interpolate(State a, State b, float alpha) 
	{
		State interpolatedState = b;
		interpolatedState.position = a.position.scale(1 - alpha).add(b.position.scale(alpha));
		interpolatedState.velocity = a.velocity.scale(1 - alpha).add(b.velocity.scale(alpha));
		interpolatedState.recalculate();
		return interpolatedState;
	}

	public void recalculate() 
	{
		
	}
}
