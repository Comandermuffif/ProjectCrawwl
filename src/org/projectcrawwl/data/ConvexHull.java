package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class ConvexHull {
	private ArrayList<Point> border = new ArrayList<Point>();
	
	private Polygon polygon = new Polygon();
	
	World world;
	
	public ConvexHull(){
		
	}
	
	public void addPoint(Point k){
		border.add(k);
		polygon.addPoint(k.x, k.y);
	}
	
	public void addPoint(double x, double y){
		border.add(new Point((int)x,(int)y));
		polygon.addPoint((int) x,(int) y);
	}
	
	public Polygon getPolygon(){
		return polygon;
	}
	
	public void render(){
		
		world = World.getInstance();
		
		//GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glColor4d(1.0, 0, 0,.5);
		for(Point p : border){
			GL11.glVertex2d(p.x + world.getMapXOffset(),p.y + world.getMapYOffset());
		}
		GL11.glEnd();
		
		
	}
}