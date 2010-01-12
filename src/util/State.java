package util;

public class State {
	// primary
	public Vector position;
	public Vector velocity;
	public Vector efield; //the value of the electric field at this position

	// constant
	public double charge;


	public State() {}

	public State(double charge) 
	{
		this.position = new Vector();
		this.velocity = new Vector();
		this.charge = charge;
	}

	public void setEfield(Vector efield)
	{
		this.efield = efield;
	}
	
	// interpolation used for animating inbetween states
	public State interpolate(State a, State b, double alpha) 
	{
		State interpolatedState = b;
		interpolatedState.position = a.position.scale(1 - alpha).add(b.position.scale(alpha));
		interpolatedState.velocity = a.velocity.scale(1 - alpha).add(b.velocity.scale(alpha));
		return interpolatedState;
	}
}
