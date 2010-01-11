package util;


public class Derivative
{
	public Vector velocity;
	public Vector acceleration;
	
	public Derivative(){};
	
	public Derivative(Vector velocity, Vector acceleration)
	{
		this.velocity = velocity;
		this.acceleration = acceleration;
	}
}
