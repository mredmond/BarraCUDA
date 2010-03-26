/* This class is the main class (and should be compiled as such) that houses all of the functionality of the BarraCUDA project.
 * The general idea is that we maintain the global variable mainChargeManager here that is accessible from all of the other classes.
 * This class also holds the physics engine object in it (which itself subcontains a numerical integrator and force-finder), as well as 
 * a two-dimensional graphical simulation. This class is flexible enough to allow a fairly easy overhaul to CUDA specifications.
 * The graphics module is design as a plug and play style class, so it can be swapped out for a three-dimensional one soon.
 *
 * This program borrows code (under license) from 
 *  Martin Burtscher
 *  Center for Grid and Distributed Computing
 *  The University of Texas at Austin
 */

package main;
import graphics.twoDimensionalSim;
import java.util.ArrayList;
import physics.*;
import util.*;

import java.util.Random;

public class BarraCUDA 
{
	public static final int NUM_PARTICLES = 4;
	public static double dt = 0.01; //the choice of DT does nothing but control the SPEED of the simulation; it does not affect the accuracy.
	public static double t = 0;
	public static ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
	public static OctTreeLeafNode[] mainBodyManager = new OctTreeLeafNode[BarraCUDA.NUM_PARTICLES];
	public static Physics physicsEngine;
	public static boolean paused = false;
	private static Random gen = new Random();
	public static void main(String[] args)
	{
		//twoDimensionalSim myGraphicsObj = new twoDimensionalSim();
		physicsEngine = new Physics(mainChargeManager, mainBodyManager);
		addRandomCharges(NUM_PARTICLES);
		initializeCharges(NUM_PARTICLES);
		initializeBodyManager(NUM_PARTICLES);
		


		//Main update loop. Does one physics iteration, then renders scene.
		while(true)
		{
			while(!paused)
			{
				physicsEngine.updateSimulation(t, dt);
//				myGraphicsObj.repaint();
				t += dt;
			}
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
			double radius = 2;

			if(chargeSignModifier <= 0.5)
			{
				charge = -charge;
			}

			PointCharge pc = new PointCharge(i,charge,mass,radius);
			mainChargeManager.add(pc);
		}
	}

	//Again, name says it all. Sets the charges with random positions, zeroes their efield vector, puts them in a body node
	public static void initializeCharges(int n)
	{
		for(int i = 0; i < n; i++)
		{
			//The magic numbers in the first line here are scaled to deal with the resolution 1280 x 1024, but can be adjusted accordingly.
			physicsEngine.initializeChargePosition(i, new Vector(gen.nextGaussian()*10000, gen.nextGaussian()*10000, gen.nextGaussian()*10000));
			physicsEngine.initializeChargeMomentum(i, new Vector(0, 0, 0));
			physicsEngine.initializeEField(i, new Vector(0,0,0));
		}
	}
	
	public static void initializeBodyManager(int n)
	{
		for(int i = 0; i < n; i++)
		{
			mainBodyManager[i] = new OctTreeLeafNode(mainChargeManager.get(i));
		}
	}
}
