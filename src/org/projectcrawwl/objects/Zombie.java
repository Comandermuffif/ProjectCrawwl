package org.projectcrawwl.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Zombie extends BasePlayer {
	Random random = new Random();
	Point waypoint;
	BasePlayer target;
	int timer = 0;
	ArrayList<Point> path  = new ArrayList<Point>();
	
	int refresh = 0;
	
	public Zombie(int tempX, int tempY){
		super(tempX,tempY, 0 , 100, 25);
		moveAngle = (float) (Math.random()*360);
		speed = .03;
	}
	public Zombie(){
		super();
		x = (float) (Math.random() * data.getMapX());
		y = (float) (Math.random() * data.getMapY());
		r = 25;
		facingAngle = 0;
		moveAngle = (float) (Math.random()*360);
		health = 100;
		speed = .03; //.03 units per millisecond or 30 units per second
	}
	//Draw everything here
	public void render(){
		super.render();
		
		GL11.glColor4d(255,127,0,128);
		for(Point p : path){
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset(), p.y*data.getGridY() + data.getMapYOffset());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset() + data.getGridX(),p.y*data.getGridY() + data.getMapYOffset());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset() + data.getGridX(),p.y*data.getGridY() + data.getMapYOffset() + data.getGridY());
				GL11.glVertex2d(p.x*data.getGridX() + data.getMapXOffset(), p.y*data.getGridY() + data.getMapYOffset() + data.getGridY());
				GL11.glEnd();
			}
		}
		
		GL11.glColor4d(0,255,0, health *(255/200) + 128);
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);{
	    	GL11.glVertex2f(renderX, renderY);
	    	  
	        for (float angle=0; angle<=Math.PI*2; angle+=((Math.PI*2)/32) )
	        {
	        	GL11.glVertex2f( (r)*(float)Math.cos(angle) + getRenderX(),
	        			(r)*(float)Math.sin(angle) + getRenderY());  
	        }
	          
	        GL11.glVertex2f(renderX + r, renderY);
	    }
	    GL11.glEnd();
	    
	    GL11.glBegin(GL11.GL_LINE);
		GL11.glColor3d(0, 0, 0);
		GL11.glVertex2f(renderX, renderY);
		GL11.glVertex2d(renderX + Math.sin(Math.toRadians(facingAngle))*r, renderY + Math.cos(Math.toRadians(facingAngle))*r);
		GL11.glEnd();
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
	}
}