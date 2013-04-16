package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;

import org.projectcrawwl.data.ConvexHull;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;

public class GameObject {
	public float x;
	public float y;
	public float r;
	public float facingAngle;
	public float moveAngle;
	public double speed;
	
	public float renderX;
	public float renderY;
	
	GameData data = GameData.getInstance();
	GameSettings settings = GameSettings.getInstance();
	
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
		r = tempR;
	}
	public GameObject(float tempX, float tempY){
		x = tempX;
		y = tempY;
		facingAngle = 0;
		r = 10;
	}
	public GameObject(){
		x = 0;
		y = 0;
		facingAngle = 0;
		r = 10;
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
		//BEEP!
	}

	//Do all calculations here
	public void update(int delta){
		World world = World.getInstance();
		
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
		
		
		if(speed != 0){
			float tempx = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
			float tempy = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
			
			Line2D.Float temp = new Line2D.Float(x,y,tempx,tempy);
			Point nearest = new Point((int)tempx,(int)tempy);
			
			boolean flag = true;
			
			for(ConvexHull k : world.getHulls()){
				if(k.getPolygon().contains(nearest)){
					flag = false;
					break;
				}
			}
			if(flag){
				x = nearest.x;
				y = nearest.y;
			}else{
				speed = 0;
			}
		}
		
		
	
		
	}
}