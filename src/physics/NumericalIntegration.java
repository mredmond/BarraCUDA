package physics;
import util.*;

public class NumericalIntegration
{	
	public Derivative evaluate(State initial, double t)
	{
		Derivative output = new Derivative();
		output.dx = initial.velocity;
		output.dv = initial.efield.scale(initial.charge);
		return output;
	}
	
	//overloading the evaluate() method to account for the other derivatives in RK4
	
	public Derivative evaluate(State initial, double t, double dt, Derivative d)
	{

		State state = initial; //arbitrary initialization to keep eclipse happy. i don't think this actually has to be here.
	    state.position = initial.position.add(d.dx.scale(dt));
	    state.velocity = initial.velocity.add(d.dv.scale(dt)); 

	    Derivative output = new Derivative();
	    output.dx = state.velocity;
	    output.dv = state.efield.scale(state.charge);
	    return output;
	}
	
	public void integrate(State state, double t, double dt)
    {
        Derivative a = evaluate(state, t);      
        Derivative b = evaluate(state, t+dt*0.5f, dt*0.5f, a);
        Derivative c = evaluate(state, t+dt*0.5f, dt*0.5f, b);
        Derivative d = evaluate(state, t+dt, dt, c);

        state.position = state.position.add((a.dx.add(b.dx).add(b.dx).add(c.dx).add(c.dx).add(d.dx)).scale(dt/6));
        state.velocity = state.velocity.add((a.dv.add(b.dv).add(b.dv).add(c.dv).add(c.dv).add(d.dv)).scale(dt/6));
        
   }
	
}


