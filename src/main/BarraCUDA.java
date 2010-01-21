package main;
import graphics.twoDimensionalSim;

import java.util.ArrayList;
import physics.*;
import util.*;

public class BarraCUDA 
{
	public static ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
	public static void main(String[] args)
	{
		twoDimensionalSim myGraphicsObj = new twoDimensionalSim();
		
		//charge creation: initializes three charges, all with unit mass (the third parameter is mass)
		PointCharge charge0 = new PointCharge(0, 1.0, 1, 2);
		PointCharge charge1 = new PointCharge(1, 1.0, 1, 2);
		PointCharge charge2 = new PointCharge(2, 1.0, 1, 2);
		

		mainChargeManager.add(charge0);
		mainChargeManager.add(charge1);
		mainChargeManager.add(charge2);


		//engine initialization
		Physics physicsEngine = new Physics(mainChargeManager);

		//state initialization
		
		physicsEngine.initializeChargePosition(0, new Vector(300, 200, 0));
		physicsEngine.initializeChargeMomentum(0, new Vector(0, 0, 0));
		physicsEngine.initializeEField(0, new Vector(0,0,0));

		physicsEngine.initializeChargePosition(1, new Vector(400, 200, 0));
		physicsEngine.initializeChargeMomentum(1, new Vector(0, 0, 0));
		physicsEngine.initializeEField(1, new Vector(0,0,0));

		physicsEngine.initializeChargePosition(2, new Vector(350, 150, 0));
		physicsEngine.initializeChargeMomentum(2, new Vector(0, 0, 0));
		physicsEngine.initializeEField(2, new Vector(0,0,0));
		
		
		//initial parameter check
		

		double dt = 0.01;
		
		for(double t = 0.0; t <= 5.000; t+=dt)
		{
			physicsEngine.updateAll(t, dt);
			for(PointCharge c : mainChargeManager)
			{
				System.out.println(c);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myGraphicsObj.repaint();
		}
		
	}
}
