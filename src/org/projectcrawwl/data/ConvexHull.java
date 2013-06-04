package org.projectcrawwl.data;

import java.awt.Color;
import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.objects.BasePlayer;

public class ConvexHull implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Polygon polygon = new Polygon();
	
	World world;
	
	ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
	
	private Point center = new Point();
	
	private double farthest = 0;
	
	private Color color = new Color((int)(Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255));//new Color(0,0,0);
	
	public ConvexHull(){	
	}
	public ConvexHull(Color c){
		color = c;
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
		GameData data = GameData.getInstance();
		GameSettings settings = GameSettings.getInstance();
		
		
		//Draw shadow, they are so sexy
		GL11.glColor4d(0,0,0,.75);
		
		
		double length = 2*data.zoom + settings.getScreenX();//world.getMapX();
		if(data.getPlayer() != null){
			BasePlayer player = data.getPlayer();
			for(Line2D.Float line : lines){
				boolean flag = false;
				Line2D.Float mid = new Line2D.Float(player.x, player.y, (line.x1 + line.x2)/2, (line.y1 + line.y2)/2);
				for(Line2D.Float l : lines){
					if(line.equals(l)){
						continue;
					}
					if(l.intersectsLine(mid)){
						flag = true;
						break;
					}
				}
				if(flag){
					
					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex2d(line.getX1() + world.getMapXOffset(),line.getY1() + world.getMapYOffset());
					GL11.glVertex2d(line.getX2() + world.getMapXOffset(), line.getY2() + world.getMapYOffset());
					
					double angle = Math.atan2(line.getY1() - player.y, line.getX1() - player.x);
					
					GL11.glVertex2d(line.getX1() + world.getMapXOffset() + Math.cos(angle)*length,line.getY1() + world.getMapYOffset() + Math.sin(angle)*length);
					
					angle = Math.atan2(line.getY2() - player.y, line.getX2() - player.x);
					
					GL11.glVertex2d(line.getX2() + world.getMapXOffset() + Math.cos(angle)*length, line.getY2() + world.getMapYOffset() + Math.sin(angle)*length);
					GL11.glEnd();
					
				}
			}
		}
		
		
//		for(int i = 0; i < polygon.npoints; i ++){
//			boolean flag = true;
//			double angle = Math.atan2(polygon.ypoints[i] - data.getPlayer().y, polygon.xpoints[i] - data.getPlayer().x);
//			Line2D.Float temp = new Line2D.Float(polygon.xpoints[i], polygon.ypoints[i], (float) (polygon.xpoints[i] + Math.cos(angle)*length), (float) (polygon.ypoints[i] + Math.sin(angle)*length));
//			for(Line2D.Float l : lines){
//				
//				if(l.getP1().equals(temp.getP1()) || l.getP1().equals(temp.getP2()) || l.getP2().equals(temp.getP1()) || l.getP2().equals(temp.getP2())){
//					continue;
//				}
//				
//				
//				if(l.intersectsLine(temp)){
//					flag = false;
//					break;
//				}
//			}
//			if(flag){
//				GL11.glVertex2d(temp.getX1() + world.getMapXOffset(), temp.getY1() + world.getMapYOffset());
//				GL11.glVertex2d(temp.getX2() + world.getMapXOffset(), temp.getY2() + world.getMapYOffset());
//			}
//		}
//		GL11.glEnd();
		
		//The hull
		GL11.glColor3d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255);
		/*
		if(data.getPlayer() != null){
			Point p = new Point((int) data.getPlayer().getX(), (int) data.getPlayer().getY());
			
			if(p.distance(center.x, center.y) < 255+farthest){
				GL11.glColor4d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255, (double)(p.distance(center.x, center.y)-farthest)/512 + .5);
			}
		}*/
		
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(Line2D.Float temp : lines){
			//Reordered, SO HAPPY
			GL11.glVertex2f(temp.x2 + world.getMapXOffset(), temp.y2 + world.getMapYOffset());
			GL11.glVertex2f(temp.x1 + world.getMapXOffset(), temp.y1 + world.getMapYOffset());
			
		}
		GL11.glEnd();
	}
}