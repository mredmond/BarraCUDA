package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JFrame;

import main.BarraCUDA;

import physics.PointCharge;

public class twoDimensionalSim extends JFrame
{
	
	public twoDimensionalSim()
	{
		this.setBounds(0, 0, 1024, 768);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void setSize(Dimension d)
	{
		super.setSize(d);
		repaint();
	}
	
	public void paint(Graphics g)
	{
		ArrayList<PointCharge> charges = BarraCUDA.mainChargeManager;
		g.setColor(Color.white);
		g.fillRect(0,0,this.getBounds().width,this.getBounds().height);
		
		Iterator<PointCharge> myIter = charges.iterator();
		while(myIter.hasNext())
		{
			PointCharge pc = myIter.next();
			drawCharge(g, pc);
			drawEField(g, pc);
			//drawMomentum(g, pc);
		}
	}
	
	public void drawCharge(Graphics g, PointCharge pc)
	{
		if(pc.myState.charge >= 0)
		{
			g.setColor(Color.BLUE);
		}
		else if(pc.myState.charge <= 0)
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
		if(pc.myState.charge >= 0)
		{
			g.setColor(Color.BLUE);
		}
		else if(pc.myState.charge <= 0)
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
}
