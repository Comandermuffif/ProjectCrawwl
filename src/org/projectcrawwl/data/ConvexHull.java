package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
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
	
	public ArrayList<Line2D.Float> getLines(){
		
		ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
		
		float[] coord = new float[6];
		float[] lastCoord = new float[2];
		float[] firstCoord = new float[2];
		PathIterator pi = polygon.getPathIterator(null);
		
		pi.currentSegment(coord);
		
		pi.currentSegment(firstCoord); //Getting the first coordinate pair
        lastCoord[0] = firstCoord[0]; //Priming the previous coordinate pair
        lastCoord[1] = firstCoord[1];
		
		while(!pi.isDone()){
			final int type = pi.currentSegment(coord);
            switch(type) {
                case PathIterator.SEG_LINETO : {
                	lines.add(new Line2D.Float(coord[0], coord[1], firstCoord[0], firstCoord[1]));
                    lastCoord[0] = coord[0];
                    lastCoord[1] = coord[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    lines.add(new Line2D.Float(coord[0], coord[1], firstCoord[0], firstCoord[1]));   
                    break;
                }
            }
            pi.next();
		}
		
		return lines;
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