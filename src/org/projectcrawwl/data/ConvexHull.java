package org.projectcrawwl.data;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class ConvexHull implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Polygon polygon = new Polygon();
	
	World world;
	
	private Color color;
	
	ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
	
	private Point center = new Point();
	
	private double farthest = 0;
	
	public ConvexHull(){
		
		color = new Color((int) (255*Math.random()),(int) (255*Math.random()),(int) (255*Math.random()));
	}
	
	public double getFarthest(){
		return farthest;
	}
	
	public Point getCenter(){
		return center;
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
		
		float deltaX = 0;
		float deltaY = 0;
		
		for(int i = 0; i < polygon.npoints; i ++){
			deltaX += polygon.xpoints[i];
			deltaY += polygon.ypoints[i];
		}
		
		deltaX = deltaX/polygon.npoints;
		deltaY = deltaY/polygon.npoints;
		
		center.setLocation(deltaX, deltaY);
		
		farthest = 0;
		
		for(int i = 0; i < polygon.npoints; i ++){
			double temp = center.distance(polygon.xpoints[i], polygon.ypoints[i]);
			if(temp > farthest){
				farthest = temp;
			}
		}
		
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
		
		GL11.glColor4d(0,0,0,1);
		//GL11.glColor3d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		for(Line2D.Float temp : lines){
			GL11.glVertex2f(temp.x1 + world.getMapXOffset(), temp.y1 + world.getMapYOffset());
			GL11.glVertex2f(temp.x2 + world.getMapXOffset(), temp.y2 + world.getMapYOffset());
		}
		GL11.glEnd();
		
		/*
		GL11.glColor4d(1.0,0.0,0.0,0.5);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(center.x + world.getMapXOffset(), center.y + world.getMapYOffset());
		for(float angle = 0; angle <= Math.PI*2; angle +=(Math.PI*2/32)){
			GL11.glVertex2d(center.x + world.getMapXOffset() + Math.cos(angle)*(farthest*2), center.y + world.getMapYOffset()  + Math.sin(angle)*(farthest*2));
		}
		GL11.glVertex2d(center.x + world.getMapXOffset() + (farthest*2), center.y + world.getMapYOffset());

		GL11.glEnd();*/
		
		
	}
}