/* This class is the main class (and should be compiled as such) that houses all of the functionality of the BarraCUDA project.
 * The general idea is that we maintain the global variable mainChargeManager here that is accessible from all of the other classes.
 * This class also holds the physics engine object in it (which itself subcontains a numerical integrator and force-finder), as well as 
 * a two-dimensional graphical simulation. This class is flexible enough to allow a fairly easy overhaul to CUDA specifications.
 * The graphics module is design as a plug and play style class, so it can be swapped out for a three-dimensional one soon.
 */

package main;
import graphics.twoDimensionalSim;
import java.util.ArrayList;
import physics.*;
import util.*;

public class BarraCUDA 
{
	public static ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
	public static Physics physicsEngine;
	public static final int NUM_PARTICLES = 130;
	public static void main(String[] args)
	{
		//Makes a new graphics object to render into
		twoDimensionalSim myGraphicsObj = new twoDimensionalSim();
		
//		Manual charge creation
//		PointCharge charge0 = new PointCharge(0, 1.0, 1, 5);
//		PointCharge charge1 = new PointCharge(1, 1.0, 1, 5);
//		PointCharge charge2 = new PointCharge(2, -1.0, 1, 5);
//		
//		mainChargeManager.add(charge0);
//		mainChargeManager.add(charge1);
//		mainChargeManager.add(charge2);

		//Auto Initialization
		addRandomCharges(NUM_PARTICLES);
		
		//physics engine object initialization
		physicsEngine = new Physics(mainChargeManager);

//		Manual state initialization
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
		
		//Auto Initialization
		initializeCharges(NUM_PARTICLES);
				
		
		//Main update loop. Does one physics iteration, then renders scene.
		double dt = 0.01;
		for(double t = 0.0; t <= 30.000; t+=dt)
		{
			physicsEngine.updateAll(t, dt);
			try 
			{
				Thread.sleep(10);
				//Thread.sleep(2000/NUM_PARTICLES); //scales wait time to deal with number of particles? not very useful
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			myGraphicsObj.repaint();
		}
	}
	
	//Does what it says on the tin: adds n random charges to the physics engine
	public static void addRandomCharges(int n)
	{
		for(int i = 0; i < n; i++)
		{
			double charge = Math.random();
			double chargeSignModifier = Math.random();
			double mass = 1;
			double radius = 5;
			
			if(chargeSignModifier <= 0.5)
			{
				charge = -charge;
			}
			
			PointCharge pc = new PointCharge(i,charge,mass,radius);
			mainChargeManager.add(pc);
		}
	}
	
	//Again, name says it all. Sets the charges with random positions, zeroes their efield vector, and gives them a random momentum
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
				
			//The magic numbers in the first line here are scaled to deal with the resolution 1280 x 1024, but can be adjusted accordingly.
			physicsEngine.initializeChargePosition(i, new Vector(Math.rint(1000*Math.random() + 50), Math.rint(800*Math.random() + 50), 0));
			physicsEngine.initializeChargeMomentum(i, new Vector(momx, momy, 0));
			physicsEngine.initializeEField(i, new Vector(0,0,0));
		}
	}
}
