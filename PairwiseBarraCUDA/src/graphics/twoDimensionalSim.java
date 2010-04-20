/* This class encapsulates the graphics object that is printed to the screen after each physics update.
 * It contains a paint method, and tracks the global array that holds the pointCharges.
 * This is a plug-and-play implementation, designed to be flexibly replaced by an OpenGL implementation.
 *   */

package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import main.BarraCUDA;
import physics.PointCharge;
import util.Vector;

public class twoDimensionalSim extends JPanel implements ActionListener, ItemListener, MouseListener
{
	public twoDimensionalSim()
	{
		createAndShowGUI();	
	}
	public void setSize(Dimension d)
	{
		super.setSize(d);
		repaint();
	}
	//This method initializes all of the menus. At the moment, these menus are non-functional.
	public void createAndShowGUI()
	{
		JFrame frame = new JFrame();
		frame.setSize(1280, 1024);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Controls");

		JMenuItem pause = new JMenuItem("Pause ||");
		pause.addActionListener(this);
		pause.setActionCommand("pause");
		fileMenu.add(pause);

		JMenuItem play = new JMenuItem("Play  |>");
		play.addActionListener(this);
		play.setActionCommand("play");
		fileMenu.add(play);

		JMenuItem increase = new JMenuItem("Increase Scale Factor");
		increase.addActionListener(this);
		increase.setActionCommand("increase");
		fileMenu.add(increase);
		
		JMenuItem decrease = new JMenuItem("Decrease Scale Factor");
		decrease.addActionListener(this);
		decrease.setActionCommand("decrease");
		fileMenu.add(decrease);
		
		JMenuItem efield = new JMenuItem("Toggle Efield Display");
		efield.addActionListener(this);
		efield.setActionCommand("efield");
		fileMenu.add(efield);
		
		JMenuItem momentum = new JMenuItem("Toggle Momentum Display");
		momentum.addActionListener(this);
		momentum.setActionCommand("momentum");
		fileMenu.add(momentum);
		
		

		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.add(this);
		frame.setVisible(true);
		frame.addMouseListener(this);
	}

	//This method accesses the global arrayList held in the BarraCUDA main class. 
	//It will paint each charge, as well as its electric field vector (representing acceleration)
	//or its momentum vector (chosen by default... if you want the other one, uncomment it) 
	public void paint(Graphics g)
	{
		super.paint(g);
		ArrayList<PointCharge> charges = BarraCUDA.mainChargeManager;
		
		g.setColor(Color.white);
		g.fillRect(0,0,this.getBounds().width,this.getBounds().height);
		g.setColor(Color.black);
		g.drawString("Current Field Strength Scale: " + BarraCUDA.physicsEngine.GRAPHICS_EFIELD_SCALE_FACTOR, 50, 50);
		g.drawString("Current System Momentum: " + BarraCUDA.physicsEngine.updateMomentumChecksum().length(), 500, 50);
		g.drawString("Current Simulation Time: " + BarraCUDA.t, 900, 50);
		Iterator<PointCharge> myIter = charges.iterator();
		while(myIter.hasNext())
		{
			PointCharge pc = myIter.next();
			drawCharge(g, pc);
			if(BarraCUDA.showEfieldVector)
			{
				drawEField(g, pc);
			}
			if(BarraCUDA.showMomentumVector)
			{
				drawMomentum(g, pc);
			}
		}
	}

	//This method draws a charge according to its position and parity.
	public void drawCharge(Graphics g, PointCharge pc)
	{
		//double alpha = pc.myState.mass;
		if(pc.myState.charge > 0)
		{
			//g.setColor(new Color(0f,0f,1,(float) alpha));
			g.setColor(Color.blue);
		}
		else if(pc.myState.charge < 0)
		{
			//g.setColor(new Color(1,0f,0f,(float) alpha));
			g.setColor(Color.red);
		}
		else g.setColor(Color.LIGHT_GRAY);

		//Actually draw the particle
		g.fillOval((int) Math.round(pc.myState.position.x - pc.myState.radius), (int) Math.round(pc.myState.position.y - pc.myState.radius), (int) Math.round(2*pc.myState.radius), (int) Math.round(2*pc.myState.radius));
	}

	//Draw the electric field vector on any point charge.
	public void drawEField(Graphics g, PointCharge pc)
	{
		if(pc.myState.charge > 0) g.setColor(Color.BLUE);
		else if(pc.myState.charge < 0) g.setColor(Color.RED);
		else g.setColor(Color.LIGHT_GRAY);

		g.drawLine((int) Math.round(pc.myState.position.x), (int) Math.round(pc.myState.position.y), (int) Math.round(pc.myState.position.x + pc.myState.efield.x), (int) Math.round(pc.myState.position.y + pc.myState.efield.y));
	}

	//Draw the momentum on any point charge.
	public void drawMomentum(Graphics g, PointCharge pc)
	{
		g.setColor(Color.black);
		g.drawLine((int) Math.round(pc.myState.position.x), (int) Math.round(pc.myState.position.y), (int) Math.round(pc.myState.position.x + pc.myState.momentum.x), (int) Math.round(pc.myState.position.y + pc.myState.momentum.y));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		String command = arg0.getActionCommand();
		if(command.equals("play"))
		{
			BarraCUDA.paused = false;
		}
		else if(command.equals("increase"))
		{
			BarraCUDA.physicsEngine.GRAPHICS_EFIELD_SCALE_FACTOR *= 1.10;
		}
		else if(command.equals("decrease"))
		{
			BarraCUDA.physicsEngine.GRAPHICS_EFIELD_SCALE_FACTOR *= 0.9;
		}
		else if(command.equals("pause"))
		{
			BarraCUDA.paused = true;
		}
		else if(command.equals("efield"))
		{
			BarraCUDA.showEfieldVector = !BarraCUDA.showEfieldVector;
		}
		else if(command.equals("momentum"))
		{
			BarraCUDA.showMomentumVector = !BarraCUDA.showMomentumVector;
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		System.out.println("Button num " + arg0.getButton() + " was pressed.");
		BarraCUDA.paused = true;
		double charge = (arg0.getButton() == 1) ? 1.0 : -1.0;
		int posX = arg0.getX() - 3; //offsets for title bars
		int posY = arg0.getY() - 52;
		int id = BarraCUDA.mainChargeManager.size();
		BarraCUDA.mainChargeManager.add(id, new PointCharge(id, charge, 1, 2));
		BarraCUDA.mainChargeManager.get(id).myState.position = new Vector((double) posX, (double) posY, 0);
		BarraCUDA.mainChargeManager.get(id).myState.momentum = new Vector(0, 0, 0);
		BarraCUDA.mainChargeManager.get(id).myState.efield = new Vector(0, 0, 0);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
