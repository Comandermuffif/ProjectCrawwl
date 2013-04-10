package org.projectcrawwl.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.Pathfinding;

public class Zombie extends BasePlayer {
	Random random = new Random();
	Point waypoint;
	BasePlayer target;
	int timer = 0;
	ArrayList<Point> path  = new ArrayList<Point>();
	Pathfinding pathfinding = Pathfinding.getInstance();
	
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

	public void getTarget(){
		//System.out.println("<-Updating Target-> <-" + System.currentTimeMillis() + "->");
		ArrayList<BasePlayer> ai = data.getFriendlies();
		target = null;
		if(ai.size() == 0){return;}
		double distToTarg = -1;
		for(BasePlayer a : ai){
			double dist = Math.pow(Math.pow(a.getX() - getX(), 2) + Math.pow(a.getY() - getY(), 2), .5);
			double angle = Math.toDegrees(Math.atan2(x - a.getX(), y - a.getY())) + 180;
			if(distToTarg == -1 && dist < sightRange && Math.abs(facingAngle - angle) < sightAngle/2){
				distToTarg = dist;
				target = a;
			}
			if(distToTarg > dist && dist < sightRange && Math.abs(facingAngle - angle) < sightAngle/2){
				distToTarg = dist;
				target = a;
			}
		}
	}
	
	public void getTarget2(){
		//System.out.println("<-Updating Target-> <-" + System.currentTimeMillis() + "->");
		ArrayList<BasePlayer> ai = data.getFriendlies();
		target = null;
		if(ai.size() == 0){return;}
		for(BasePlayer a : ai){
			if(view.contains(new Point((int)(a.getX()/data.getGridX()),(int)(a.getY()/data.getGridY())))){
				target = a;
			}
		}
	}
	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		double dist = 0;
		
		if(timer > 200){
			timer = 0;
			getTarget();
			if(target != null){
				//path = data.getPath(this, target);
				path = pathfinding.getPath(this, target);
			}
		}else{
			timer += delta;
		}
		
		if(path.size() < 2 && target == null){
			path = pathfinding.getPath(this);
		}
		
		if(target != null){
			facingAngle = (float) (90 - Math.toDegrees(Math.atan2(y - target.getY(), x - target.getX())) -180);
			dist = Math.pow(Math.pow(target.getX() - getX(), 2) + Math.pow(target.getY() - getY(), 2), .5);
			
			if(dist <= 25 + r + target.r){
				inventory.getWeapon().fire();
			}

		}else{
			if(target == null && waypoint != null){
				dist = Math.pow(Math.pow(waypoint.getX()*data.getGridX()  + data.getGridX()/2 - getX(), 2) + Math.pow(waypoint.getY()*data.getGridY()  + data.getGridY()/2 - getY(), 2), .5);
				facingAngle = 90 - moveAngle;
				//facingAngle = (float) (90 - Math.toDegrees(Math.atan2(y - data.getPlayer().getY(), x - data.getPlayer().getX()))-180);
			}
		}
		/*
		x = (float) (x + Math.cos(Math.toRadians(moveAngle)) * delta * speed);
		y = (float) (y + Math.sin(Math.toRadians(moveAngle)) * delta * speed);
		*/
		if(speed == 0){speed = .03;moveAngle = (float) (Math.random()*360);}
		
		if(waypoint != null){
			moveAngle = (float) (Math.toDegrees(Math.atan2(y - waypoint.getY()*data.getGridY() - data.getGridY()/2, x - waypoint.getX()*data.getGridX() - data.getGridX()/2))-180);
			dist = Math.pow(Math.pow(waypoint.getX()*data.getGridX()  + data.getGridX()/2 - getX(), 2) + Math.pow(waypoint.getY()*data.getGridY()  + data.getGridY()/2 - getY(), 2), .5);
		}else{
			if(path.size() != 0){
				waypoint = path.get(0);
			}
		}
		if(dist <= 1){
			if(path.size() > 1){
				path.remove(0);
				waypoint = path.get(0);
			}else{
				speed = 0;
			}
		}
	}
}