package org.projectcrawwl.objects;

import java.awt.Point;
import java.util.Random;


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
		
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		updateViewCone();
		
		tempFacing = moveAngle;
		/*
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
		GL11.glEnd();*/
		
		if(speed == 0){
			moveAngle += 1;
			speed = .03;
		}
		/*
		for(ConvexHull hull : world.getHulls()){
			for(Line2D.Float qq : hull.getLines()){
				if(lineL.intersectsLine(qq)){
					moveAngle -= 5;
					break;
				}
				if(lineR.intersectsLine(qq)){
					moveAngle += 5;
					break;
				}
			}
			if(moveAngle != facingAngle){
				break;
			}
		}*/
		
		
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