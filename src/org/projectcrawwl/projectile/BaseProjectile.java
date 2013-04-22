package org.projectcrawwl.projectile;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.GameObject;

public class BaseProjectile extends GameObject{
	
	BasePlayer owner;
	public float r;
	public float damage;
	GameData data = GameData.getInstance();
	Point lastPos = new Point();
	
	
	public BaseProjectile(float tempX, float tempY, float tempSpeed, float tempAngle, float tempD, BasePlayer own){
		super(tempX, tempY);
		x = tempX;
		y = tempY;
		speed = tempSpeed;
		moveAngle = tempAngle;
		r = 5;
		damage = tempD;
		owner = own;
		
		lastPos.setLocation(x, y);
	}
	
	public void render(){
		super.render();
		GL11.glColor3d(201, 192, 187);
		
		/*
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
	      {
	    	  GL11.glVertex2f(renderX, renderY);
	    	  
	        for (float angle=0; angle<=Math.PI*2; angle+=((Math.PI*2)/32) )
	        {
	        	GL11.glVertex2f( (r)*(float)Math.cos(angle) + renderX,
	        			(r)*(float)Math.sin(angle) + renderY);  
	        }
	        
	        GL11.glVertex2f(renderX + r, renderY);
	      }
	   GL11.glEnd();*/
		
		//(212, 175, 55)
		
		GL11.glColor3d(.8313, .6867, .2156);
		GL11.glLineWidth(3);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glVertex2d(lastPos.x + data.getMapXOffset(), lastPos.y + data.getMapYOffset());
		GL11.glEnd();
		GL11.glLineWidth(1);
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		lastPos.setLocation(x, y);
		
		
		ArrayList<BasePlayer> temp = data.getAllPlayers();
		
		for(BasePlayer b : temp){
			double dist = Math.pow(Math.pow(b.getX() - getX(), 2) + Math.pow(b.getY() - getY(), 2), .5);
			
			if(dist+.001 < r + b.r){
				b.damage(damage, owner);
				data.removeProjectile(this);
			}
			
		}
		
		if(speed == 0){
			data.removeProjectile(this);
		}
	}
}
