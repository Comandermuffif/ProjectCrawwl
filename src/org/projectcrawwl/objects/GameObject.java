package org.projectcrawwl.objects;

import java.awt.Point;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;

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
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
		
		float tempx = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
		float tempy = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
		
		Point p = new Point((int) (tempx/data.getGridX()),(int) (tempy/data.getGridY()));
		Boolean check = false;
		
		if(p.x < 0 || p.y < 0 || p.x >= data.getMapX()/data.getGridX() || p.y >= data.getMapY()/data.getGridY()){
			speed = 0;
		}else{
			if(data.getGrid().get(p.x).get(p.y) >= 0){
				check = true;
			}else{
				speed = 0;
			}
		}
		
		if(x < 0){x = 0; speed = 0;}else
		if(x > data.getMapX()){x = data.getMapX(); speed = 0;}else
		if(y < 0){y = 0; speed = 0;}else
		if(y > data.getMapY()){y = data.getMapY(); speed = 0;}else
		if(check){
			x = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
			y = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
		}
		
		//BOOP!
	}
}