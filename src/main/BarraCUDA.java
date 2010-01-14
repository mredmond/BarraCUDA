package main;
import java.util.ArrayList;
import physics.*;
import util.*;

public class BarraCUDA 
{
	public static void main(String[] args) 
	{
		//charge creation
		PointCharge charge0 = new PointCharge(0, 1);
		PointCharge charge1 = new PointCharge(1, -1.0);
		PointCharge charge2 = new PointCharge(2, -1.0);
		

		ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
		mainChargeManager.add(charge0);
		mainChargeManager.add(charge1);
		mainChargeManager.add(charge2);


		//engine initialization
		Physics physicsEngine = new Physics(mainChargeManager);

		//state initialization
		physicsEngine.initializeChargePosition(0, new Vector(0, 0, 0));
		physicsEngine.initializeChargeVelocity(0, new Vector(0, 0, 0));
		physicsEngine.initializeEField(0, new Vector(0,0,0));

		physicsEngine.initializeChargePosition(1, new Vector(1, 0, 0));
		physicsEngine.initializeChargeVelocity(1, new Vector(0, 0, 0));
		physicsEngine.initializeEField(1, new Vector(0,0,0));

		physicsEngine.initializeChargePosition(2, new Vector(-1, 0, 0));
		physicsEngine.initializeChargeVelocity(2, new Vector(0, 0, 0));
		physicsEngine.initializeEField(2, new Vector(0,0,0));
		
		
		//intial parameter check
		for(PointCharge c : mainChargeManager)
		{
			System.out.println(c);
		}

		double dt = 0.01;
		double width = 0.1;
		for(double t = 0.4; t <= 1.000; t+=dt)
		{
			System.out.println("Update# " + (Math.floor(t/dt) + 1));
			physicsEngine.updateAll(t, dt, width);

			for(PointCharge c : mainChargeManager)
			{
				System.out.println(c);
			}
			System.out.println("------------------------");
		}
	}
}
