package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class World implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float mapX = 4000;
	private float mapY = 4000;
	
	private float mapXOffset;// = (1280-600)/2;
	private float mapYOffset;// = (720-600)/2;
	
	static GameSettings settings = GameSettings.getInstance();
	
	private ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
	
	public World(){
		
		int subset = 20;
		
		for(int i = 0; i < subset; i ++){
			{
				ConvexHull a = new ConvexHull();
				a.addPoint(0,(mapY/subset)*(i + 1));
				a.addPoint(5,(mapY/subset)*(i + 1));
				a.addPoint(5,(mapY/subset)*(i));
				a.addPoint(0,(mapY/subset)*(i));
				
				hulls.add(a);
			}
			
			{
				ConvexHull a = new ConvexHull();
				a.addPoint((mapX/subset)*(i),0);
				a.addPoint((mapX/subset)*(i),5);
				a.addPoint((mapX/subset)*(i+1),5);
				a.addPoint((mapX/subset)*(i+1),0);
				
				hulls.add(a);
			}
			
			{
				ConvexHull a = new ConvexHull();
				a.addPoint(mapX-5,(mapY/subset)*(i + 1));
				a.addPoint(mapX,(mapY/subset)*(i + 1));
				a.addPoint(mapX,(mapY/subset)*(i));
				a.addPoint(mapX-5,(mapY/subset)*(i));
				
				hulls.add(a);
			}
			
			{
				ConvexHull a = new ConvexHull();
				a.addPoint((mapX/subset)*(i),mapY);
				a.addPoint((mapX/subset)*(i+1),mapY);
				a.addPoint((mapX/subset)*(i+1),mapY-5);
				a.addPoint((mapX/subset)*(i),mapY-5);
				
				hulls.add(a);
			}
		}
		/*
		{
			ConvexHull a = new ConvexHull();
			a.addPoint(0,mapY);
			a.addPoint(5,mapY);
			a.addPoint(5,0);
			a.addPoint(0,0);
			
			hulls.add(a);
		}
		
		{
			ConvexHull a = new ConvexHull();
			a.addPoint(0,0);
			a.addPoint(0,5);
			a.addPoint(mapX,5);
			a.addPoint(mapX,0);
			
			hulls.add(a);
		}
		
		{
			ConvexHull a = new ConvexHull();
			a.addPoint(mapX-5,mapY);
			a.addPoint(mapX,mapY);
			a.addPoint(mapX,0);
			a.addPoint(mapX-5,0);
			
			hulls.add(a);
		}
		
		{
			ConvexHull a = new ConvexHull();
			a.addPoint(0,mapY);
			a.addPoint(mapX,mapY);
			a.addPoint(mapX,mapY-5);
			a.addPoint(0,mapY-5);
			
			hulls.add(a);
		}*/
		
		
		for(int i = 0; i < 10; i ++){
			int tempX = (int) (Math.random() * mapX);
			int tempY = (int) (Math.random() * mapY);
			
			boolean flag = true;
			

			ConvexHull a = new ConvexHull();
			a.addPoint(tempX, tempY);
			a.addPoint(tempX + 100,tempY + 100);
			a.addPoint(tempX + 200,tempY);
			a.addPoint(tempX + 100,tempY - 100);			
			
			for(ConvexHull hull : hulls){
				
				for(Line2D.Float line : hull.getLines()){
					for(Line2D.Float line2 : a.getLines()){
						if(line.intersectsLine(line2)){
							flag = false;
							break;
						}
					}
					if(!flag){
						break;
					}
				}
				if(!flag){
					break;
				}
			}
			if(flag){
				hulls.add(a);
			}
		}
		
		
	}
	
	public ArrayList<ConvexHull> getHulls(){
		return hulls;
	}
	
	private static World instance = null;

	public static World getInstance()
	{
		if(instance == null){
			instance = new World();
		}
		return instance;
	}
	
	public float getMapX(){
		return mapX;
	}
	public float getMapY(){
		return mapY;
	}
	
	public float getMapXOffset(){
		return mapXOffset;
	}
	public float getMapYOffset(){
		return mapYOffset;
	}
	
	public void setMapXOffset(float temp){
		mapXOffset = temp;
	}
	public void setMapYOffset(float temp){
		mapYOffset = temp;
	}
	
	
	public Point getLineLineIntersection(Line2D.Float a, Line2D.Float b) {
		double x1 = a.x1; double y1 = a.y1; double x2 = a.x2; double y2 = a.y2; double x3 = b. x1; double y3 = b.y1; double x4 = b.x2; double y4 = b.y2;
	    double det1And2 = det(x1, y1, x2, y2);
	    double det3And4 = det(x3, y3, x4, y4);
	    double x1LessX2 = x1 - x2;
	    double y1LessY2 = y1 - y2;
	    double x3LessX4 = x3 - x4;
	    double y3LessY4 = y3 - y4;
	    double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
	    if (det1Less2And3Less4 == 0){
	       // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
	       return null;
	    }
	    double x = (det(det1And2, x1LessX2, det3And4, x3LessX4) / det1Less2And3Less4);
	    double y = (det(det1And2, y1LessY2, det3And4, y3LessY4) / det1Less2And3Less4);
	    return new Point((int)x, (int)y);
	}
	protected double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}

	public void renderLights(){
		//I have no idea what I want to do here
	}
	
	public void renderBackground(){
		
		//Brown background
		
		GL11.glColor3d(.34,.23,.04);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex3f(mapXOffset, mapYOffset, 0);
		GL11.glVertex3f(mapXOffset, mapYOffset + mapY, 0);
		GL11.glVertex3f(mapXOffset + mapX, mapYOffset, 0);
		GL11.glVertex3f(mapXOffset + mapX, mapYOffset + mapY, 0);
		
		GL11.glEnd();
		
		for(ConvexHull x : hulls){
			x.render();
		}
	}
}
