package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
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
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		//Re scale view so text is right side up
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, settings.getScreenX() + data.zoom, settings.getScreenY() + data.zoom*(ratio),-data.zoom*(ratio), -1, 1);
		
		
		data.getFont().drawString((renderX - 25), settings.getScreenY() - (renderY + 40), "Zombie", Color.red);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, settings.getScreenX()  + data.zoom, -data.zoom*(ratio), settings.getScreenY() + data.zoom*(ratio), -1, 1);
		
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		//updateViewCone();
		
		tempFacing = moveAngle;
		
		/*
		currentCoolDown -= delta;
		
		if(currentCoolDown < 0){
			currentCoolDown = coolDown;
			target = null;
		}*/
		
		
		
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
			
			target = null;
			
			
			
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
			double dist = -1;
			for(BasePlayer friendly : data.getFriendlies()){
				Line2D.Float sight = new Line2D.Float(x, y, friendly.x, friendly.y);
				
				boolean flag = true;
				
				for(ConvexHull hull : world.getHulls()){
					for(Line2D.Float line : hull.getLines()){
						if(line.intersectsLine(sight)){
							flag = false;
							break;
						}
					}
					if(!flag){break;}
				}
				if(flag){
					double tempD = sight.getP1().distance(sight.getP2());
					if(dist == -1){
						target = friendly;
						dist = tempD;
					}else if(tempD < dist){
						target = friendly;
						dist = tempD;
					}
				}
			}
			
		}else{
			if(new Point((int)getX(),(int)getY()).distance(target.getX(), target.getY()) <= 15 + r){
				inventory.getWeapon().fire();
			}
			moveAngle = (float) (Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x)));
		}
	}
}