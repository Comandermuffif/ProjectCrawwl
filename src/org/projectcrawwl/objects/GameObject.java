package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;

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
		
		float tempx = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
		float tempy = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
		
		Line2D.Float temp = new Line2D.Float(x,y,tempx,tempy);
		Point nearest = new Point((int)tempx,(int)tempy);
		
		double dist = nearest.distance(new Point((int)x,(int)y));
		
		for(Line2D.Float x : world.getLineWalls((int)tempx, (int)tempy)){
			if(x.intersectsLine(temp)){
				Point q = world.getLineLineIntersection(x, temp);
				if(q!= null && nearest.distance(q) < dist){
					nearest = new Point(q.x,q.y);
					dist = q.distance(new Point((int)this.x,(int)this.y));
				}
			}
		}
		x = nearest.x;
		y = nearest.y;
		
	}
}