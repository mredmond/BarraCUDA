/* This class is the main class (and should be compiled as such) that houses all of the functionality of the BarraCUDA project.
 * The general idea is that we maintain the global variable mainChargeManager here that is accessible from all of the other classes.
 * This class also holds the physics engine object in it (which itself subcontains a numerical integrator and force-finder), as well as 
 * a two-dimensional graphical simulation. This class is flexible enough to allow a fairly easy overhaul to CUDA specifications.
 * The graphics module is design as a plug and play style class, so it can be swapped out for a three-dimensional one soon.
 */

package main;
import graphics.twoDimensionalSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import physics.*;
import util.*;

import java.util.Random;

public class BarraCUDA 
{
	public static ArrayList<PointCharge> mainChargeManager = new ArrayList<PointCharge>();
	public static Physics physicsEngine;
	public static final int NUM_PARTICLES = 200;
	public static double dt = 0.01;
	public static double t = 0;
	public static boolean paused = false;
	public static boolean showEfieldVector = false;
	public static boolean showMomentumVector = true;
	public static double fps = 0;
	private static Random gen = new Random();
	public static void main(String[] args)
	{
		twoDimensionalSim myGraphicsObj = new twoDimensionalSim();
		addAndInitializeRandomCharges();
		//loadChargesFromFile("particleInit.txt");
		physicsEngine = new Physics();


		//Main update loop. Does one physics iteration, then renders scene.
		while(true)
		{
			int i = 0;
			double accumulator = 0;
			while(!paused)
			{	
				if(i == 100)
				{
					fps = 100000/accumulator;
					i = 0;
					accumulator = 0;
				}
				double startTime = System.currentTimeMillis();
				physicsEngine.updateAll(t, dt);
				myGraphicsObj.repaint();
				double stopTime = System.currentTimeMillis();
				accumulator += (stopTime - startTime);
				t += dt;
				i++;
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
				mainChargeManager.add(id, pc);
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
			double radius = 1.5*charge + 2; //used for graphics

			if(chargeSignModifier <= 0.5) charge = -charge;

			PointCharge pc = new PointCharge(i,charge,mass,radius);
			pc.myState.position = new Vector((gen.nextDouble() - 0.5)*600 + 1280/2 , (gen.nextDouble() - 0.5)*500 + 1024/2, 0); //uniformly distributed between -100 and 100
			pc.myState.momentum = new Vector(0, 0, 0);
			pc.myState.efield = new Vector(0,0,0);
			
			mainChargeManager.add(i, pc);
			
		}
	}
}
