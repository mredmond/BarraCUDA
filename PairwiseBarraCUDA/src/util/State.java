/* The data structure for the numerical integrator.
 * Every pointCharge has a state, which contains the variables listed below.
 * Velocity (as a secondary variable) is not dealt with directly, but rather recomputed from momentum.
 * I'm anticipating a need to do rendertime-based interpolation, so I provide a method for alpha-based linear interpolation.
 */

package util;

import physics.PointCharge;

public class State {
	// primary
	public Vector position;
	public Vector momentum;
	public Vector efield; //the value of the electric field at this position

	//secondary
	public Vector velocity;
	
	// constant
	public double radius;
	public double charge;
	public double mass;
	public double inverseMass;


	public State() {}

	public State(double charge, double mass, double radius) 
	{
		this.position = new Vector();
		this.momentum = new Vector();
		this.charge = charge;
		this.mass = mass;
		this.radius = radius;
		this.inverseMass = 1 / mass;
		this.velocity = momentum.scale(inverseMass);
	}

	// interpolation used for animating inbetween states
	public State interpolate(State a, State b, double alpha) 
	{
		State interpolatedState = b;
		interpolatedState.position = a.position.scale(1 - alpha).add(b.position.scale(alpha));
		interpolatedState.momentum = a.momentum.scale(1 - alpha).add(b.momentum.scale(alpha));
		interpolatedState.efield = a.efield.scale(1 - alpha).add(b.efield.scale(alpha));
		return interpolatedState;
	}
	
	public void recalc()
	{
		velocity = momentum.scale(inverseMass);
	}
	
}
