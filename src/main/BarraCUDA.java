package main;
import java.util.ArrayList;
import physics.*;
import util.*;

public class BarraCUDA 
{
	public static void main(String[] args) 
	{
		//charge creation: initializes three charges, all with unit mass (the third parameter is mass)
		PointCharge charge0 = new PointCharge(0, 1.0, 1);
		PointCharge charge1 = new PointCharge(1, -1.0, 1);
		//PointCharge charge2 = new PointCharge(2, -1.0, 1);
		

		ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
		mainChargeManager.add(charge0);
		mainChargeManager.add(charge1);
		//mainChargeManager.add(charge2);


		//engine initialization
		Physics physicsEngine = new Physics(mainChargeManager);

		//state initialization
		
		physicsEngine.initializeChargePosition(0, new Vector(-3, 0, 0));
		physicsEngine.initializeChargeMomentum(0, new Vector(1, 0, 0));
		physicsEngine.initializeEField(0, new Vector(0,0,0));

		physicsEngine.initializeChargePosition(1, new Vector(3, 0, 0));
		physicsEngine.initializeChargeMomentum(1, new Vector(-1, 0, 0));
		physicsEngine.initializeEField(1, new Vector(0,0,0));

		//physicsEngine.initializeChargePosition(2, new Vector(-1, 0, 0));
		//physicsEngine.initializeChargeVelocity(2, new Vector(0, 0, 0));
		//physicsEngine.initializeEField(2, new Vector(0,0,0));
		
		
		//initial parameter check
		for(PointCharge c : mainChargeManager)
		{
			System.out.println(c);
		}

		double dt = 0.01;
		double width = 0.5; //represents the radius of a particle modeled as 1 pixel in diameter
		
		double t0 = System.currentTimeMillis();
		
		for(double t = 0.0; t <= 1.000; t+=dt)
		{
			System.out.println("Update #" + (Math.floor(t/dt) + 1));
			double start = System.nanoTime();
			physicsEngine.updateAll(t, dt, width);
			double stop = System.nanoTime();
			
			for(PointCharge c : mainChargeManager)
			{
				System.out.println(c);
			}
			System.out.println("Update took " + (stop - start) + " nanoseconds.");
		}
		
		double t1 = System.currentTimeMillis();
		System.out.println("Entire operation took " + (t1 - t0) + " milliseconds");
	}
}
