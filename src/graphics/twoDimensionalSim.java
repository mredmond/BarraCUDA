package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.*;

import main.BarraCUDA;

import physics.PointCharge;

public class twoDimensionalSim extends JPanel implements ActionListener, ItemListener
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
	
	public void createAndShowGUI()
	{
		JFrame frame = new JFrame();
		//frame.setBounds(0, 0, 1024, 768);
		frame.setSize(1024, 768);
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
		
		JMenuItem step = new JMenuItem("Step Forward  ->");
		step.addActionListener(this);
		step.setActionCommand("step");
		fileMenu.add(step);
		
		menuBar.add(fileMenu);
		
		frame.setJMenuBar(menuBar);
		
		

		frame.add(this);
		
		frame.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		ArrayList<PointCharge> charges = BarraCUDA.mainChargeManager;
		g.setColor(Color.white);
		g.fillRect(0,0,this.getBounds().width,this.getBounds().height);
		
		Iterator<PointCharge> myIter = charges.iterator();
		while(myIter.hasNext())
		{
			PointCharge pc = myIter.next();
			drawCharge(g, pc);
			//drawEField(g, pc);
			drawMomentum(g, pc);
		}
	}
	
	public void drawCharge(Graphics g, PointCharge pc)
	{
		if(pc.myState.charge > 0)
		{
			g.setColor(Color.BLUE);
		}
		else if(pc.myState.charge < 0)
		{
			g.setColor(Color.RED);
		}
		else 
		{
			g.setColor(Color.LIGHT_GRAY);
		}
		
		//draws the particle
		g.fillOval((int) Math.round(pc.myState.position.x - pc.myState.radius), (int) Math.round(pc.myState.position.y - pc.myState.radius), (int) Math.round(2*pc.myState.radius), (int) Math.round(2*pc.myState.radius));
	}
	
	public void drawEField(Graphics g, PointCharge pc)
	{
		if(pc.myState.charge > 0)
		{
			g.setColor(Color.BLUE);
		}
		else if(pc.myState.charge < 0)
		{
			g.setColor(Color.RED);
		}
		else 
		{
			g.setColor(Color.LIGHT_GRAY);
		}
		g.drawLine((int) Math.round(pc.myState.position.x), (int) Math.round(pc.myState.position.y), (int) Math.round(pc.myState.position.x + pc.myState.efield.x), (int) Math.round(pc.myState.position.y + pc.myState.efield.y));
	}
	
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
			//do play event handling
		}
		else if(command.equals("step"))
		{
			//do step handling
		}
		else if(command.equals("pause"))
		{
			//do pause handling
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
}
