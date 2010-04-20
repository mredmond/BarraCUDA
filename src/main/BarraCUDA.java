/* This class is the main class (and should be compiled as such) that houses all of the functionality of the BarraCUDA project.
 * The general idea is that we maintain the global array bodyManager here that is accessible from all of the other classes.
 * This class also holds the physics engine object in it (which itself subcontains a numerical integrator and force-finder), as well as 
 * a two-dimensional graphical simulation. This class is flexible enough to allow a fairly easy overhaul to CUDA specifications.
 * The graphics module is designed as a plug and play style class, so it can be swapped out for a three-dimensional one soon.
 *
 * This program borrows code (under license) from 
 *  Martin Burtscher
 *  Center for Grid and Distributed Computing
 *  The University of Texas at Austin
 */

package main;
import graphics.twoDimensionalSim;
import physics.*;
import util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class BarraCUDA 
{
	public static final int NUM_PARTICLES = 3
	;
	public static double dt = 0.001;
	public static double t = 0;
	public static OctTreeLeafNode[] bodyManager = new OctTreeLeafNode[NUM_PARTICLES];
	public static Physics physicsEngine;
	public static boolean paused = false;
	private static Random gen = new Random();
	public static void main(String[] args)
	{
		twoDimensionalSim myGraphicsObj = new twoDimensionalSim(1280,1024);
		physicsEngine = new Physics();
		addAndInitializeRandomCharges();
		//loadChargesFromFile("particleInit.txt");
		


		//Main update loop. Does one physics iteration, then renders scene.
		while(true)
		{
			while(!paused)
			{
				physicsEngine.updateSimulation(t, dt);
				myGraphicsObj.repaint();
				t += dt;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	public static void loadChargesFromFile(String filename)
	{
		try {
			Scanner sc = new Scanner(new File(filename));
			sc.nextLine(); //skips the header line
			while(sc.hasNextLine())
			{
				int id = sc.nextInt();
				double charge = sc.nextDouble();
				double mass = sc.nextDouble();
				double radius = sc.nextDouble();
				PointCharge pc = new PointCharge(id,charge,mass,radius);
				pc.myState.position = new Vector(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
				pc.myState.momentum = new Vector(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
				pc.myState.efield = new Vector(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
				bodyManager[id] = new OctTreeLeafNode(pc);
				System.out.println("Read in " + pc);
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Does what it says on the tin: adds n random charges to the physics engine
	public static void addAndInitializeRandomCharges()
	{
		for(int i = 0; i < NUM_PARTICLES; i++)
		{
			double charge = Math.random();
			double chargeSignModifier = Math.random();
			double mass = 1; //this really isn't used at all
			double radius = 2;

			if(chargeSignModifier <= 0.5) charge = -charge;

			PointCharge pc = new PointCharge(i,charge,mass,radius);
			pc.myState.position = new Vector((gen.nextDouble() - 0.5)*600 + 1280/2 , (gen.nextDouble() - 0.5)*500 + 1024/2, 0); //uniformly distributed between -100 and 100
			pc.myState.momentum = new Vector(0, 0, 0);
			pc.myState.efield = new Vector(0,0,0);
			
			bodyManager[i] = new OctTreeLeafNode(pc);
			
		}
	}

}
