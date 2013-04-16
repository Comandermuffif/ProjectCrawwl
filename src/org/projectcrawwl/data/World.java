package org.projectcrawwl.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class World {
	
	private float mapX = 2000;
	private float mapY = 1000;
	
	private float gridX = 10;//10
	private float gridY = 10;//10
	
	private float mapXOffset;// = (1280-600)/2;
	private float mapYOffset;// = (720-600)/2;
	
	static GameSettings settings = GameSettings.getInstance();
	
	private ArrayList<Point> rooms = new ArrayList<Point>();
	
	/**
	 * ArrayList of all collidable walls stored in absolute game position
	 */
	private ArrayList<ArrayList<ArrayList<Line2D.Float>>> lineWalls = new ArrayList<ArrayList<ArrayList<Line2D.Float>>>();
	
	public World(){
		
		for (int i = 0; i <= (mapX/gridX)/10; i++){
			ArrayList<ArrayList<Line2D.Float>> row = new ArrayList<ArrayList<Line2D.Float>>();
			for (int j = 0; j <= (mapY/gridY)/10; j++){
				ArrayList<Line2D.Float> row2 = new ArrayList<Line2D.Float>();
				row.add(row2);
			}
			lineWalls.add(row);
		}
		
		for (int i = 0; i < mapX/gridX; i++){
	         for (int j = 0; j < mapY/gridY; j++){
	        	 
	        	 if(i == 0 || j == 0 || i + 1 == mapX/gridX || j + 1 == mapY/gridY || i == 1 || j == 1 || i+2 == mapX/gridX || j+2 == mapY/gridY){
	        		 
	        		 addLineWall(new Line2D.Float(gridX*(i), gridY*(j), gridX*(i), gridY*(j+1)));
	        		 addLineWall(new Line2D.Float(gridX*(i), gridY*(j), gridX*(i+1), gridY*(j)));
	        		 addLineWall(new Line2D.Float(gridX*(i), gridY*(j+1), gridX*(i+1), gridY*(j+1)));
	        		 addLineWall(new Line2D.Float(gridX*(i+1), gridY*(j), gridX*(i+1), gridY*(j+1)));
	        		 
	        	 }
	         }
	    }
		
		addLineWall(new Line2D.Float(0,0,0,mapY));
		addLineWall(new Line2D.Float(0,0,mapX,0));
		addLineWall(new Line2D.Float(0,mapY,mapX,mapY));
		addLineWall(new Line2D.Float(mapX,0,mapX,mapY));
		
		for(int a = 0; a < Math.random()*5 + 5; a ++){
			generateRoom();
		}
		//connectRooms();
	}
	
	private static World instance = null;

	public static World getInstance()
	{
		if(instance == null)
			instance = new World();

		return instance;
	}
	
	public ArrayList<Line2D.Float> getLineWalls(int x, int y){
		
		return lineWalls.get((int)(x/gridX/10)).get((int)(y/gridY/10));
	}
	
	public void addLineWall(Line2D.Float line){
		for(int i = (int) Math.floor((line.x1/gridX)/10); i <= (int) Math.floor((line.x2/gridX)/10); i += 1){
			for(int j = (int) Math.floor((line.y1/gridY)/10); j <= (int) Math.floor((line.y2/gridY)/10); j += 1){
				lineWalls.get(i).get(j).add(line);
			}
		}
	}
	
	public float getMapX(){
		return mapX;
	}
	public float getMapY(){
		return mapY;
	}
	
	public float getGridX(){
		return gridX;
	}
	public float getGridY(){
		return gridY;
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
	
	public void generateRoom(){
		int w = (int) (5 + Math.random()*4);
		int h = (int) (4 + Math.random()*5);
		
		int x = (int) (Math.random() * (mapX/gridX - w - 2));
		int y = (int) (Math.random() * (mapY/gridY - h - 2));
		
		rooms.add(new Point(x + w/2,y + h/2));
		
		for(int a = x; a <= x + w; a ++){
			for(int b = y; b <= y + h; b ++){
				
				if(a == x || a == x+w || b == y){
					
					addLineWall(new Line2D.Float(gridX*(a), gridY*(b), gridX*(a), gridY*(b+1)));
					addLineWall(new Line2D.Float(gridX*(a), gridY*(b), gridX*(a+1), gridY*(b)));
					addLineWall(new Line2D.Float(gridX*(a), gridY*(b+1), gridX*(a+1), gridY*(b+1)));
					addLineWall(new Line2D.Float(gridX*(a+1), gridY*(b), gridX*(a+1), gridY*(b+1)));
					
				}
			}
		}
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
		int leftBound = (int) Math.floor(-mapXOffset/gridX);
		int rightBound = (int) Math.floor((-mapXOffset + settings.getScreenX())/gridX);
		int upBound = (int) Math.floor(-mapYOffset/gridY);
		int downBound = (int) Math.floor((-mapYOffset + settings.getScreenY())/gridY);
		
		if(leftBound < 0){
			leftBound = 0;
		}else if (leftBound >= mapX/gridX){
			leftBound = (int) (mapX/gridX - 1);
		}
		
		if(rightBound < 0){
			rightBound = 0;
		}else if (rightBound >= mapX/gridX){
			rightBound = (int) (mapX/gridX - 1);
		}
		
		if(upBound < 0){
			upBound = 0;
		}else if (upBound >= mapY/gridY){
			upBound = (int) (mapX/gridX - 1);
		}
		
		if(downBound < 0){
			downBound = 0;
		}else if (downBound >= mapY/gridY){
			downBound = (int) (mapY/gridY - 1);
		}
		
		GL11.glColor3d(.34,.23,.04);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex3f(leftBound*gridX + mapXOffset,upBound*gridY + mapYOffset,0);
		GL11.glVertex3f(rightBound*gridX + mapXOffset+ gridX,upBound*gridY + mapYOffset,0);
		GL11.glVertex3f(leftBound*gridX + mapXOffset,downBound*gridY + mapYOffset+gridY,0);
		GL11.glVertex3f(rightBound*gridX + mapXOffset+gridX,downBound*gridY + mapYOffset+gridY,0);
		GL11.glEnd();
		
		
		for(int i = (int) Math.floor(leftBound/10); i <= Math.floor(rightBound/10); i++){
			for(int j = (int) Math.floor(upBound/10); j <= Math.floor(downBound/10); j++){
				for(Line2D.Float x : lineWalls.get(i).get(j)){
					GL11.glColor3d(1.0, 0, 0);
					GL11.glLineWidth(1);
					GL11.glBegin(GL11.GL_LINES);
					
					GL11.glVertex3d(x.getX1() + mapXOffset, x.getY1() + mapYOffset, 1);
					GL11.glVertex3d(x.getX2() + mapXOffset, x.getY2() + mapYOffset, 1);
					
				
					GL11.glEnd();	
				}
			}
		}
		
		/*
		for(Line2D.Float x : lineWalls){
			//TODO prevent rendering of off screen walls
			GL11.glColor3d(1.0, 0, 0);
			GL11.glLineWidth(1);
			GL11.glBegin(GL11.GL_LINES);
			
			GL11.glVertex3d(x.getX1() + mapXOffset, x.getY1() + mapYOffset, 1);
			GL11.glVertex3d(x.getX2() + mapXOffset, x.getY2() + mapYOffset, 1);
			
		
			GL11.glEnd();	
		}*/
	}
	
	
}
