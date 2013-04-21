package org.projectcrawwl.data;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class ConvexHull {
	
	private Polygon polygon = new Polygon();
	
	World world;
	
	private Color color;
	
	ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
	
	public ConvexHull(){
		
		color = new Color((int) (255*Math.random()),(int) (255*Math.random()),(int) (255*Math.random()));
	}
	
	public void addPoint(Point k){
		polygon.addPoint(k.x, k.y);
		updateLines();
	}
	
	public void addPoint(double x, double y){
		polygon.addPoint((int) x,(int) y);
		updateLines();
	}
	
	public Polygon getPolygon(){
		return polygon;
	}
	
	private void updateLines(){
		ArrayList<Line2D.Float> temp = new ArrayList<Line2D.Float>();
		
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
                	temp.add(new Line2D.Float(coord[0], coord[1], lastCoord[0], lastCoord[1]));
                    lastCoord[0] = coord[0];
                    lastCoord[1] = coord[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    temp.add(new Line2D.Float(coord[0], coord[1], firstCoord[0], firstCoord[1]));   
                    break;
                }
            }
            pi.next();
		}
		
		lines = temp;
	}
	
	public ArrayList<Line2D.Float> getLines(){
		
		return lines;
	}
	
	public void render(){
		
		world = World.getInstance();
		
		GL11.glColor4d(.2,.3,.7,1);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_POLYGON);
		for(Line2D.Float temp : lines){
			GL11.glVertex2f(temp.x1 + world.getMapXOffset(), temp.y1 + world.getMapYOffset());
			GL11.glVertex2f(temp.x2 + world.getMapXOffset(), temp.y2 + world.getMapYOffset());
		}
		
		
		GL11.glEnd();
		
		
	}
}