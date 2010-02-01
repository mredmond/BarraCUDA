/* The derivative structure used by the RK4 integrator. Very simple.
 * 
 * 
 */

package util;
public class Derivative
{
	public Vector dx; //velocity
	public Vector dv; //acceleration
	
	public Derivative(){};
	
	public Derivative(Vector velocity, Vector acceleration)
	{
		this.dx = velocity;
		this.dv = acceleration;
	}
}
