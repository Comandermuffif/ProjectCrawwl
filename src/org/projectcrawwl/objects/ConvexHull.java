package org.projectcrawwl.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;

public class ConvexHull extends GameObject{

	private Polygon polygon = new Polygon();
	
	ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
	
	private Point center = new Point();
	
	private double farthest = 0;
	
	private Color color = new Color((int)(100 + Math.random()*40),(int) (0 + Math.random()*40),(int) (100+Math.random()*80));//new Color(0,0,0);
	
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
	
	@Override
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
	
	public void updateLines(){
		
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
	
	public Color getColor(){
		return color;
	}
	
	public ArrayList<Line2D.Float> getLines(){
		
		return lines;
	}
	
	public boolean isOnScreen(){
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		
		if(getCenter().x + World.getMapXOffset() + getFarthest() < -GameData.zoom || getCenter().x + World.getMapXOffset() - getFarthest() > GameSettings.getScreenX()  + GameData.zoom){
			return false;
		}
		if(getCenter().y + World.getMapYOffset() + getFarthest() < -GameData.zoom*(ratio) || getCenter().y + World.getMapYOffset() - getFarthest() > GameSettings.getScreenY()  + GameData.zoom*(ratio)){
			return false;
		}
		
		return true;
	}
	
	public void renderShadow(){
		
		if(!isOnScreen()){
			return;
		}
		
		//Draw shadow, they are so sexy
		double length = 2*GameData.zoom + GameSettings.getScreenX();//world.getMapX();
		if(GameData.getPlayer() != null){
			BasePlayer player = GameData.getPlayer();
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
					
					//GL11.glEnable(GL11.GL_DEPTH_TEST);
					//GL11.glDepthFunc(GL11.GL_NOTEQUAL);
					
					GL11.glColor4d(0,0,0,1);
					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex3d(line.getX1() + World.getMapXOffset(),line.getY1() + World.getMapYOffset(), .5);
					GL11.glVertex3d(line.getX2() + World.getMapXOffset(), line.getY2() + World.getMapYOffset(), .5);
					angle = Math.atan2(line.getY1() - player.y, line.getX1() - player.x);
					GL11.glVertex3d(line.getX1() + World.getMapXOffset() + Math.cos(angle)*length,line.getY1() + World.getMapYOffset() + Math.sin(angle)*length, .5);
					angle = Math.atan2(line.getY2() - player.y, line.getX2() - player.x);
					GL11.glVertex3d(line.getX2() + World.getMapXOffset() + Math.cos(angle)*length, line.getY2() + World.getMapYOffset() + Math.sin(angle)*length, .5);
					GL11.glEnd();
					
					//GL11.glDisable(GL11.GL_DEPTH_TEST);
				}
			}
		}
	}
	
	public void render(){
		
		if(!isOnScreen()){
			return;
		}
		
		renderShadow();
		
		//The hull
		
		//GL11.glBegin(GL11.GL_LINE_LOOP);
		for(Line2D.Float line : lines){
			boolean flag = true;
			
			//Line2D.Float mid = new Line2D.Float(player.x, player.y, (line.x1 + line.x2)/2, (line.y1 + line.y2)/2);
			Line2D.Float mid = new Line2D.Float(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset(), (line.x1 + line.x2)/2, (line.y1 + line.y2)/2);
			
			
			for(Line2D.Float l : lines){
				if(line.equals(l)){
					continue;
				}
				if(l.intersectsLine(mid)){
					flag = false;
				}
			}
			if(flag){
				double angle = 0;
				
				double length = 0;
				
				GL11.glColor3d((double)(color.getRed())/255, (double)(color.getBlue())/255, (double)(color.getGreen())/255);
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
				//GL11.glBegin(GL11.GL_LINES);
				
				GL11.glVertex3d(line.getX1() + World.getMapXOffset(), line.getY1() + World.getMapYOffset(), .5);
				GL11.glVertex3d(line.getX2() + World.getMapXOffset(), line.getY2() + World.getMapYOffset(), .5);
				
				angle = Math.atan2(line.y1 - mid.y1, line.x1 - mid.x1);
				length = Math.pow(Math.pow(line.getX1() - mid.x1, 2) + Math.pow(line.getY1() - mid.y1, 2), .5) * ((double)1000/900 - 1);
				
				GL11.glVertex3d(line.getX1() + World.getMapXOffset() + Math.cos(angle)*length, line.getY1() + World.getMapYOffset() + Math.sin(angle)*length, .5);
				
				angle = Math.atan2(line.y2 - mid.y1, line.x2 - mid.x1);
				length = Math.pow(Math.pow(line.getX2() - mid.x1, 2) + Math.pow(line.getY2() - mid.y1, 2), .5) * ((double)1000/900 - 1);
				
				GL11.glVertex3d(line.getX2() + World.getMapXOffset() + Math.cos(angle)*length, line.getY2() + World.getMapYOffset() + Math.sin(angle)*length, .5);
				GL11.glEnd();
				
				GL11.glColor4d(0,0,0,1);
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glVertex3d(line.getX1() + World.getMapXOffset(), line.getY1() + World.getMapYOffset(), .5);
				
				angle = Math.atan2(line.y1 - mid.y1, line.x1 - mid.x1);
				length = Math.pow(Math.pow(line.getX1() - mid.x1, 2) + Math.pow(line.getY1() - mid.y1, 2), .5) * ((double)1000/900 - 1);
				
				GL11.glVertex3d(line.getX1() + World.getMapXOffset() + Math.cos(angle)*length, line.getY1() + World.getMapYOffset() + Math.sin(angle)*length, .5);
				
				GL11.glEnd();
				
				
				
				GL11.glColor4d(0,0,0,1);
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glVertex3d(line.getX2() + World.getMapXOffset(), line.getY2() + World.getMapYOffset(), .5);
				
				angle = Math.atan2(line.y2 - mid.y1, line.x2 - mid.x1);
				length = Math.pow(Math.pow(line.getX2() - mid.x1, 2) + Math.pow(line.getY2() - mid.y1, 2), .5) * ((double)1000/900 - 1);
				
				GL11.glVertex3d(line.getX2() + World.getMapXOffset() + Math.cos(angle)*length, line.getY2() + World.getMapYOffset() + Math.sin(angle)*length, .5);
				GL11.glEnd();
				
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glVertex3d(line.getX1() + World.getMapXOffset(),line.getY1() + World.getMapYOffset(), .5);
				
				GL11.glVertex3d(line.getX2() + World.getMapXOffset(), line.getY2() + World.getMapYOffset(), .5);
				
				GL11.glEnd();
				
			}
		}
		
		//Black cap
		
		GL11.glColor4d(0,0,0,1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(Line2D.Float line : lines){
			
			Line2D.Float mid = new Line2D.Float(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset(), (line.x1 + line.x2)/2, (line.y1 + line.y2)/2);
			
			double angle = 0;
			
			double length = 0;
			
			angle = Math.atan2(line.y1 - mid.y1, line.x1 - mid.x1);
			length = Math.pow(Math.pow(line.getX1() - mid.x1, 2) + Math.pow(line.getY1() - mid.y1, 2), .5) * ((double)1000/900 - 1);
			
			GL11.glVertex3d(line.getX1() + World.getMapXOffset() + Math.cos(angle)*length, line.getY1() + World.getMapYOffset() + Math.sin(angle)*length, .5);
			
			angle = Math.atan2(line.y2 - mid.y1, line.x2 - mid.x1);
			length = Math.pow(Math.pow(line.getX2() - mid.x1, 2) + Math.pow(line.getY2() - mid.y1, 2), .5) * ((double)1000/900 - 1);
			
			GL11.glVertex3d(line.getX2() + World.getMapXOffset() + Math.cos(angle)*length, line.getY2() + World.getMapYOffset() + Math.sin(angle)*length, .5);				
		}
		GL11.glEnd();
	
		
	}
	
	//@Override
	protected Point2D.Double getNearestNEW(){
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		
		Point2D.Double pp = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		Point2D.Double q = new Point2D.Double(center.x,center.y);
		
		for(Line2D.Float l : lines){
			
			Point2D.Double p = new Point2D.Double(l.getX1(), l.getY1());
			
			if(q.x == center.x || q.y == center.y){
				q = (Point2D.Double) p.clone();
			}else{
				if(Math.abs(pp.x - p.x)  < Math.abs(pp.x - q.x) || Math.abs(pp.y - p.y)/(ratio)  < Math.abs(pp.y - q.y)/(ratio)){
					q = (Point2D.Double) p.clone();
				}
				if(Math.abs(pp.x - p.x)  < Math.abs(pp.y - q.y)/(ratio) || Math.abs(pp.y - p.y)/(ratio)  < Math.abs(pp.x - q.x)){
					q = (Point2D.Double) p.clone();
				}
			}
		}
		
		return q;
	}
	
	//@Override
	protected Point2D.Double getNearest(){

		Point2D.Double pp = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		Point2D.Double q = new Point2D.Double(center.x,center.y);
		
		for(Line2D.Float l : boundingLines){
			
			Point2D.Double p = new Point2D.Double();
			
			p.x = l.x1;
			p.y = l.y1;
			
			if(q.x == center.x || q.y == center.y){
				q = (Point2D.Double) p.clone();
			}else{
				if(pp.distance(p) < pp.distance(q)){
					q = (Point2D.Double) p.clone();
				}
			}
		}
		
		return q;
		
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<Point> getPoints(){
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(int i = 0; i < polygon.npoints; i ++){
			points.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
		}
		
		return points;
	}
	
	@Override
	public double getCompareTo(){
		double d = -1;
		
		Point2D.Double p = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		for(Line2D.Float temp : lines){
			double dd = temp.ptSegDist(p);
			
			if(d == -1){
				d = dd;
			}else if(dd < d){
				d = dd;
			}
		}
		
		return d;
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "\t\t\t\t<ConvexHull>\n";
		{
			
			data += "\t\t\t\t\t<x>" + x + "</x>\n";
			data += "\t\t\t\t\t<y>" + y + "</y>\n";
			
			data += "\t\t\t\t\t<color>" + color.getRGB() + "</color>\n";
			
			data += "\t\t\t\t\t<points>\n";
			{
				for(Point p : this.getPoints()){
					data += "\t\t\t\t\t\t<point>\n";
					{
						data += "\t\t\t\t\t\t\t<pX>" + p.x + "</pX>\n";
						data += "\t\t\t\t\t\t\t<pY>" + p.y + "</pY>\n";
					}
					data += "\t\t\t\t\t\t</point>\n";
				}
			}
			data += "\t\t\t\t\t</points>\n";
				
			
		}
		data += "\t\t\t\t</ConvexHull>\n";
		
		return data;
	}
}
