package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.ConvexHull;

public class Zombie extends BasePlayer {
	Random random = new Random();
	BasePlayer target;
	
	
	public Zombie(int tempX, int tempY){
		super(tempX,tempY, 0 , 100, 25);
		moveAngle = (float) (Math.random()*360);
		speed = .03;
		
		createBoundingBox();
		
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
		
		createBoundingBox();
		
	}
	//Draw everything here
	public void render(){
		super.render();
		
		/*
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
	    GL11.glEnd();*/
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		tempFacing = moveAngle;
		
		//TODO move side collide lines to edge of circle
		
		//temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(tempFacing)) - bound.y1*Math.sin(Math.toRadians(tempFacing)) + x);
		//temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(tempFacing)) + bound.y1*Math.cos(Math.toRadians(tempFacing)) + y);
		
		Line2D.Float line = new Line2D.Float(x, y,(float) (x + Math.cos(Math.toRadians(moveAngle))*(r+100)),
				(float) (y + Math.sin(Math.toRadians(moveAngle))*(r+100)));
		Line2D.Float lineL = new Line2D.Float((float) ((0)*Math.cos(Math.toRadians(tempFacing)) - (25)*Math.sin(Math.toRadians(tempFacing)) + x),
				(float) ((0)*Math.sin(Math.toRadians(tempFacing)) + (25)*Math.cos(Math.toRadians(tempFacing)) + y),
				(float) ((100)*Math.cos(Math.toRadians(tempFacing)) - (30)*Math.sin(Math.toRadians(tempFacing)) + x),
				(float) ((100)*Math.sin(Math.toRadians(tempFacing)) + (30)*Math.cos(Math.toRadians(tempFacing)) + y));
		Line2D.Float lineR = new Line2D.Float((float) ((0)*Math.cos(Math.toRadians(tempFacing)) - (-25)*Math.sin(Math.toRadians(tempFacing)) + x),
				(float) ((0)*Math.sin(Math.toRadians(tempFacing)) + (-25)*Math.cos(Math.toRadians(tempFacing)) + y),
				(float) ((100)*Math.cos(Math.toRadians(tempFacing)) - (-30)*Math.sin(Math.toRadians(tempFacing)) + x),
				(float) ((100)*Math.sin(Math.toRadians(tempFacing)) + (-30)*Math.cos(Math.toRadians(tempFacing)) + y));
		
		GL11.glColor3d(.8313, .6867, .2156);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(lineL.x1 + renderX - x, lineL.y1 + renderY - y);
		GL11.glVertex2f(lineL.x2 + renderX - x, lineL.y2 + renderY - y);
		GL11.glEnd();
		
		GL11.glColor3d(.8313, .6867, .2156);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(lineR.x1 + renderX - x, lineR.y1 + renderY - y);
		GL11.glVertex2f(lineR.x2 + renderX - x, lineR.y2 + renderY - y);
		GL11.glEnd();
		
		for(ConvexHull hull : world.getHulls()){
			for(Line2D.Float qq : hull.getLines()){
				if(lineL.intersectsLine(qq)){
					moveAngle -= 5;
					break;
				}else
				if(lineL.intersectsLine(qq)){
					moveAngle += 5;
					break;
				}
				
				/*
				if(line.intersectsLine(qq)){
					if(world.getLineLineIntersection(lineL, qq).distance(x,y) < world.getLineLineIntersection(lineR, qq).distance(x, y)){
						moveAngle -= 5;
					}else{

						moveAngle += 5;
					}
					break;
				}*/
			}
			if(moveAngle != facingAngle){
				break;
			}
		}
		
		
		if(target == null){
			for(BasePlayer temp : data.getFriendlies()){
				if(viewCone.contains(temp.getX(), temp.getY())){
					target = temp;
					break;
				}
			}
			
		}else{
			
			if(new Point((int)getX(),(int)getY()).distance(target.getX(), target.getY()) <= 15 + r){
				inventory.getWeapon().fire();
			}
			
			if(viewCone.contains(target.getX(), target.getY())){
				moveAngle = (float) (Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x)));
			}else{
				target = null;
			}
			
		}
		
		
		
	}
}