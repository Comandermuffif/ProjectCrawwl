package org.projectcrawwl.data;

import java.awt.Color;
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
	
	private int x = 0;
	private int y = 0;
	
	public ConvexHull(){	
	}
	public ConvexHull(Color c){
		color = c;
	}
	
	/**
	 * 
	 * @param x - X coordinate
	 * @param y - Y coordinate
	 */
	public ConvexHull(int xx, int yy){
		this.x = xx;
		this.y = yy;
	}
	
	
	public double getFarthest(){
		return farthest;
	}
	
	public Point getCenter(){
		return center;
	}
	
	public void addPoint(Point k){
		polygon.addPoint(k.x + this.x, k.y + this.y);
		updateLines();
	}
	
	public void addPoint(double xx, double yy){
		polygon.addPoint((int) (xx + this.x),(int) (yy + this.y));
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
	
	public boolean isOnScreen(){
		
		GameSettings settings = GameSettings.getInstance();
		
		GameData data = GameData.getInstance();
		
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		
		if(getCenter().x + world.getMapXOffset() + getFarthest() < -data.zoom || getCenter().x + world.getMapXOffset() - getFarthest() > settings.getScreenX()  + data.zoom){
			return false;
		}
		if(getCenter().y + world.getMapYOffset() + getFarthest() < -data.zoom*(ratio) || getCenter().y + world.getMapYOffset() - getFarthest() > settings.getScreenY()  + data.zoom*(ratio)){
			return false;
		}
		
		return true;
	}
	
	public void renderShadow(){
		
		world = World.getInstance();
		GameData data = GameData.getInstance();
		GameSettings settings = GameSettings.getInstance();
		
		if(!isOnScreen()){
			return;
		}
		
		//Draw shadow, they are so sexy
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
					double angle = 0;
					
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glDepthFunc(GL11.GL_NOTEQUAL);
					
					GL11.glColor4d(0,0,0,.75);
					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex3d(line.getX1() + world.getMapXOffset(),line.getY1() + world.getMapYOffset(), .5);
					GL11.glVertex3d(line.getX2() + world.getMapXOffset(), line.getY2() + world.getMapYOffset(), .5);
					angle = Math.atan2(line.getY1() - player.y, line.getX1() - player.x);
					GL11.glVertex3d(line.getX1() + world.getMapXOffset() + Math.cos(angle)*length,line.getY1() + world.getMapYOffset() + Math.sin(angle)*length, .5);
					angle = Math.atan2(line.getY2() - player.y, line.getX2() - player.x);
					GL11.glVertex3d(line.getX2() + world.getMapXOffset() + Math.cos(angle)*length, line.getY2() + world.getMapYOffset() + Math.sin(angle)*length, .5);
					GL11.glEnd();
					
					GL11.glDisable(GL11.GL_DEPTH_TEST);
				}
			}
		}
	}
	
	public void renderHull(){
		
		world = World.getInstance();
		
		if(!isOnScreen()){
			return;
		}
		
		//The hull
		GL11.glColor3d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255);
		
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		//GL11.glBegin(GL11.GL_LINE_LOOP);
		for(Line2D.Float temp : lines){
			//Reordered, SO HAPPY
			GL11.glVertex3d(temp.x2 + world.getMapXOffset(), temp.y2 + world.getMapYOffset(), 0);
			GL11.glVertex3d(temp.x1 + world.getMapXOffset(), temp.y1 + world.getMapYOffset(), 0);
			
		}
		GL11.glEnd();
	}
}