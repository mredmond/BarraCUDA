package main;
import graphics.twoDimensionalSim;

import java.util.ArrayList;
import physics.*;
import util.*;

public class BarraCUDA 
{
	public static ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
	public static Physics physicsEngine;
	public static final int NUM_PARTICLES = 150;
	public static void main(String[] args)
	{
		twoDimensionalSim myGraphicsObj = new twoDimensionalSim();
		
		//charge creation: initializes three charges, all with unit mass (the third parameter is mass)
//		PointCharge charge0 = new PointCharge(0, 1.0, 1, 5);
//		PointCharge charge1 = new PointCharge(1, 1.0, 1, 5);
//		PointCharge charge2 = new PointCharge(2, -1.0, 1, 5);
//		
//		mainChargeManager.add(charge0);
//		mainChargeManager.add(charge1);
//		mainChargeManager.add(charge2);

		addRandomCharges(NUM_PARTICLES);

		
		//engine initialization
		physicsEngine = new Physics(mainChargeManager);
		
		initializeCharges(NUM_PARTICLES);
		
		
		//state initialization
//		physicsEngine.initializeChargePosition(0, new Vector(300, 200, 0));
//		physicsEngine.initializeChargeMomentum(0, new Vector(0, 0, 0));
//		physicsEngine.initializeEField(0, new Vector(0,0,0));
//
//		physicsEngine.initializeChargePosition(1, new Vector(400, 200, 0));
//		physicsEngine.initializeChargeMomentum(1, new Vector(0, 0, 0));
//		physicsEngine.initializeEField(1, new Vector(0,0,0));
//
//		physicsEngine.initializeChargePosition(2, new Vector(350, 150, 0));
//		physicsEngine.initializeChargeMomentum(2, new Vector(0, 0, 0));
//		physicsEngine.initializeEField(2, new Vector(0,0,0));
		
		double dt = 0.01;
		
		for(double t = 0.0; t <= 30.000; t+=dt)
		{
			physicsEngine.updateAll(t, dt);
			
			
			try 
			{
				Thread.sleep(10);
				//Thread.sleep(2000/NUM_PARTICLES);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			myGraphicsObj.repaint();
		}
	}
	
	public static void addRandomCharges(int n)
	{
		for(int i = 0; i < n; i++)
		{
			double charge = Math.random();
			double chargeSignModifier = Math.random();
			double mass = 1;
			double radius = 2;
			
			if(chargeSignModifier <= 0.5)
			{
				charge = -charge;
			}
			
			PointCharge pc = new PointCharge(i,charge,mass,radius);
			mainChargeManager.add(pc);
		}
	}
	
	public static void initializeCharges(int n)
	{
		for(int i = 0; i < n; i++)
		{
			double momx = Math.random();
			double momxMod = Math.random();
			double momy = Math.random();
			double momyMod = Math.random();
			if(momxMod <= 0.5)
			{
				momx = -momx;
			}
			if(momyMod <= 0.5)
			{
				momy = -momy;
			}
			
			physicsEngine.initializeChargePosition(i, new Vector(Math.rint(1000*Math.random() + 50), Math.rint(800*Math.random() + 50), 0));
			physicsEngine.initializeChargeMomentum(i, new Vector(momx, momy, 0));
			physicsEngine.initializeEField(i, new Vector(0,0,0));
		}
	}
}
