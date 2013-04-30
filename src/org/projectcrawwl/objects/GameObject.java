package org.projectcrawwl.objects;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;

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
	
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();
	
	public World world = World.getInstance();
	
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
	//Draw everything here
	public void render(){
		
	}

	//Do all calculations here
	public void update(int delta){
		World world = World.getInstance();
		
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
		/*
		if(Math.abs(facingAngle - tempFacing) > 180){
			if(facingAngle < 0){
				facingAngle += 360;
			}
			if(tempFacing < 0){
				tempFacing += 360;
			}
		}*/
		
		
		
		if(facingAngle != tempFacing){
			//System.out.println(facingAngle +">"+tempFacing);
			
			if(Math.abs(facingAngle - tempFacing) > delta*(turnSpeed)){
				if(facingAngle - tempFacing > delta*(turnSpeed)){
					tempFacing = (float) (facingAngle - delta*(turnSpeed));
				}else{
					tempFacing = (float) (facingAngle + delta*(turnSpeed));
				}
			}
			
			boolean flag = true;
			for(ConvexHull k : world.getHulls()){
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