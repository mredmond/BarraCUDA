package physics;
import util.*;

public class NumericalIntegration
{	
	public Derivative evaluate(State initial, double t)
	{
		Derivative output = new Derivative();
		output.dx = initial.momentum;
		output.dv = initial.efield.scale(initial.charge);
		return output;
	}
	
	//overloading the evaluate() method to account for the other derivatives in RK4
	
	public Derivative evaluate(State initial, double t, double dt, Derivative d)
	{

		State state = initial;
	    state.position = initial.position.add(d.dx.scale(dt));
	    state.momentum = initial.momentum.add(d.dv.scale(dt)); 

	    Derivative output = new Derivative();
	    output.dx = state.momentum;
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
        state.momentum = state.momentum.add((a.dv.add(b.dv).add(b.dv).add(c.dv).add(c.dv).add(d.dv)).scale(dt/6));
        state.recalc();
        
   }
	
}


