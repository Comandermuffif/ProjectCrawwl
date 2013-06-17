package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.ConvexHull;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;

public class GameObject {
	public float x;
	public float y;
	public float r;
	public float facingAngle;
	
	public float tempFacing;
	
	public float moveAngle;
	public double speed;
	
	public float renderX;
	public float renderY;
	
	public double turnSpeed = 0.1;
	
	public Polygon boundingBox = new Polygon();
	public ArrayList<Line2D.Float> boundingLines = new ArrayList<Line2D.Float>();
	
	public GameData data = GameData.getInstance();
	public GameSettings settings = GameSettings.getInstance();
	
	private Point center = new Point();
	
	protected double farthest = 0;
	
	public boolean passThroughPlayers = false;
	
	public World world = World.getInstance();
	
	public boolean isReady = false;
	
	
	/**
	 * A generic Game Object
	 * 
	 * @param tempX The x position
	 * @param tempY The y position
	 * @param tempA Facing angle
	 * @param tempH Health
	 */
	public GameObject(float tempX, float tempY, float tempA, float tempR){
		x = tempX;
		y = tempY;
		facingAngle = tempA;
		tempFacing = facingAngle;
		r = tempR;
	}
	
	
	public GameObject(float tempX, float tempY){
		x = tempX;
		y = tempY;
		facingAngle = 0;
		tempFacing = facingAngle;
		r = 10;
	}
	public GameObject(){
		x = 0;
		y = 0;
		facingAngle = 0;
		tempFacing = facingAngle;
		r = 10;
	}
	
	public void addPoint(Point k){
		boundingBox.addPoint(k.x, k.y);
		updateData();
	}
	
	public ArrayList<Line2D.Float> getLines(){
		return boundingLines;
	}
	
	public Polygon getPolygon(){
		return boundingBox;
	}
	
	public void addPoint(double x, double y){
		boundingBox.addPoint((int) x,(int) y);
		updateData();
	}
	
	public void updateData(){
		float deltaX = 0;
		float deltaY = 0;
		
		for(int i = 0; i < boundingBox.npoints; i ++){
			deltaX += boundingBox.xpoints[i];
			deltaY += boundingBox.ypoints[i];
		}
		
		deltaX = deltaX/boundingBox.npoints;
		deltaY = deltaY/boundingBox.npoints;
		
		center.setLocation(deltaX, deltaY);
		
		farthest = 0;
		
		for(int i = 0; i < boundingBox.npoints; i ++){
			double temp = center.distance(boundingBox.xpoints[i], boundingBox.ypoints[i]);
			if(temp > farthest){
				farthest = temp;
			}
		}
	}
	
	public void updateLines(){
		ArrayList<Line2D.Float> temp = new ArrayList<Line2D.Float>();
		
		float[] coord = new float[6];
		float[] lastCoord = new float[2];
		float[] firstCoord = new float[2];
		PathIterator pi = boundingBox.getPathIterator(null);
		
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
		
		boundingLines = temp;
		updateData();
	}
	
	public ArrayList<Line2D.Float> boundingBox(){
		return boundingLines;
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public float getRenderX(){
		return renderX;
	}
	public float getRenderY(){
		return renderY;
	}
	
	public void setX(int tempX){
		x = tempX;
	}
	public void setY(int tempY){
		y = tempY;
	}
	
	public double getFarthest(){
		return farthest;
	}
	
	public Point getCenter(){
		return new Point((int) (center.x + x), (int) (center.y + y));
	}
	/**
	 * Flag to tell whether an object is visible on screen
	 * @return
	 */
	public boolean isOnScreen(){
		
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		
		if(renderX + getFarthest() < -data.zoom || renderX - getFarthest() > settings.getScreenX()  + data.zoom){
			return false;
		}
		if(renderY  + getFarthest() < -data.zoom*(ratio) || renderY - getFarthest() > settings.getScreenY()  + data.zoom*(ratio)){
			return false;
		}
		
		return true;
	}
	
	//Draw everything here
	public void render(){
		if(!isReady){
			return;
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		
		GL11.glColor4d(.3,.3,.3,1);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(Line2D.Float temp : boundingLines){
			GL11.glVertex2d(temp.x2*Math.cos(Math.toRadians(facingAngle)) - temp.y2*Math.sin(Math.toRadians(facingAngle)) + renderX, temp.x2*Math.sin(Math.toRadians(facingAngle)) + temp.y2*Math.cos(Math.toRadians(facingAngle)) + renderY);
			GL11.glVertex2d(temp.x1*Math.cos(Math.toRadians(facingAngle)) - temp.y1*Math.sin(Math.toRadians(facingAngle)) + renderX, temp.x1*Math.sin(Math.toRadians(facingAngle)) + temp.y1*Math.cos(Math.toRadians(facingAngle)) + renderY);
		}
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
	}

	//Do all calculations here
	public void update(int delta){
		if(!isReady){isReady = true;}
		
		World world = World.getInstance();
		
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
		
		if(facingAngle != tempFacing){
			if(Math.abs(facingAngle - tempFacing) > delta*(turnSpeed)){
				if(facingAngle - tempFacing > delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) < 180){
					tempFacing = (float) (facingAngle - delta*(turnSpeed));
				}else if(facingAngle - tempFacing < delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) < 180){
					tempFacing = (float) (facingAngle + delta*(turnSpeed));
				}else if(facingAngle - tempFacing > delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) > 180){
					tempFacing = (float) (facingAngle + delta*(turnSpeed));
				}else if(facingAngle - tempFacing < delta*(turnSpeed) && Math.abs(facingAngle - tempFacing) > 180){
					tempFacing = (float) (facingAngle - delta*(turnSpeed));
				}
				
				if(tempFacing < -180){
					tempFacing += 360;
				}
				if(tempFacing > 180){
					tempFacing -= 360;
				}
				
			}
			
			boolean flag = true;
			for(ConvexHull k : world.getHulls()){
				Point p = new Point((int) x, (int) y);
				if(p.distance(k.getCenter()) > k.getFarthest() + getFarthest()){
					continue;
				}
				
				for(Line2D.Float q : k.getLines()){
					for(Line2D.Float bound : boundingBox()){
						
						Line2D.Float temp = new Line2D.Float();
						
						temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(tempFacing)) - bound.y1*Math.sin(Math.toRadians(tempFacing)) + x);
						temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(tempFacing)) + bound.y1*Math.cos(Math.toRadians(tempFacing)) + y);
						temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(tempFacing)) - bound.y2*Math.sin(Math.toRadians(tempFacing)) + x);
						temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(tempFacing)) + bound.y2*Math.cos(Math.toRadians(tempFacing)) + y);
								
								
						//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
						if(temp.intersectsLine(q)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				if(!flag){break;}
			}
			if(flag){
				facingAngle = tempFacing;
			}
		}
		
		if(speed != 0){
			float tempx = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
			float tempy = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
			
			boolean flag = true;
			
			Line2D.Float tempLine = new Line2D.Float(x,y,tempx,tempy);
			
			for(ConvexHull k : world.getHulls()){
				
				if(tempLine.ptSegDist(k.getCenter()) > k.getFarthest() + getFarthest()){
					continue;
				}
				
				
				
				if(k.getPolygon().contains(tempx, tempy)){
					flag = false;
					break;
				}
				
				for(Line2D.Float q : k.getLines()){
					
					if(q.intersectsLine(tempLine)){
						flag = false;
						break;
					}
				}
				
				for(Line2D.Float q : k.getLines()){
					for(Line2D.Float bound : boundingBox()){
						
						Line2D.Float temp = new Line2D.Float();
						
						temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(facingAngle)) - bound.y1*Math.sin(Math.toRadians(facingAngle)) + tempx);
						temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(facingAngle)) + bound.y1*Math.cos(Math.toRadians(facingAngle)) + tempy);
						temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(facingAngle)) - bound.y2*Math.sin(Math.toRadians(facingAngle)) + tempx);
						temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(facingAngle)) + bound.y2*Math.cos(Math.toRadians(facingAngle)) + tempy);
								
								
						//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
						if(temp.intersectsLine(q)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				
				
				if(!flag){break;}
				
				
			}
			if(flag){
				
				x = tempx;
				y = tempy;
				
			}else{
				speed = 0;
			}
		}
	}
}