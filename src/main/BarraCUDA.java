package main;

import java.util.ArrayList;

import physics.*;
import util.*;

public class BarraCUDA 
{

	public static void main(String[] args) 
	{
		//charge creation
		PointCharge charge0 = new PointCharge(0, -1.34);
		PointCharge charge1 = new PointCharge(1, -2.01);
		PointCharge charge2 = new PointCharge(2, 0.56);
		PointCharge charge3 = new PointCharge(3, 4.12);
		ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
		mainChargeManager.add(charge0);
		mainChargeManager.add(charge1);
		mainChargeManager.add(charge2);
		mainChargeManager.add(charge3);
		
		//engine intialization
		Physics physicsEngine = new Physics(mainChargeManager);
		
		
		//state initialization
		physicsEngine.initializeChargePosition(0, new Vector(1, 2, 2));
		physicsEngine.initializeChargeVelocity(0, new Vector(-1.00, 0, 0.85));
		physicsEngine.initializeEField(0, new Vector(0,0,0));
		
		physicsEngine.initializeChargePosition(1, new Vector(-2, 1, 0));
		physicsEngine.initializeChargeVelocity(1, new Vector(0.14, 0.42, 0.34));
		physicsEngine.initializeEField(1, new Vector(0,0,0));
		
		physicsEngine.initializeChargePosition(2, new Vector(0, 1, 2));
		physicsEngine.initializeChargeVelocity(2, new Vector(1.14, 0.15, 0.16));
		physicsEngine.initializeEField(2, new Vector(0,0,0));
		
		physicsEngine.initializeChargePosition(3, new Vector(2, 0, 0));
		physicsEngine.initializeChargeVelocity(3, new Vector(-1.00, 0, 0.76));
		physicsEngine.initializeEField(3, new Vector(0,0,0));
		
		//intial parameter check
		System.out.println(charge0);
		System.out.println(charge1);
		System.out.println(charge2);
		System.out.println(charge3);
		
		physicsEngine.updateAll(0, 0.001);
		
		System.out.println(charge0);
		System.out.println(charge1);
		System.out.println(charge2);
		System.out.println(charge3);

		physicsEngine.updateAll(0.001, 0.001);
		
		System.out.println(charge0);
		System.out.println(charge1);
		System.out.println(charge2);
		System.out.println(charge3);
		

	}

}
