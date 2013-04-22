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
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(facingAngle))*r, renderY + Math.sin(Math.toRadians(facingAngle))*r);
		GL11.glEnd();
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		facingAngle = moveAngle;
		
		Line2D.Float line = new Line2D.Float(x, y,(float) (x + Math.cos(Math.toRadians(moveAngle))*(r+20)), (float) (y + Math.sin(Math.toRadians(moveAngle))*(r+20)));
		Line2D.Float lineL = new Line2D.Float(x, y,(float) (x + Math.cos(Math.toRadians(moveAngle+5))*(r+20)), (float) (y + Math.sin(Math.toRadians(moveAngle+5))*(r+20)));
		Line2D.Float lineR = new Line2D.Float(x, y,(float) (x + Math.cos(Math.toRadians(moveAngle-5))*(r+20)), (float) (y + Math.sin(Math.toRadians(moveAngle-5))*(r+20)));
		
		
		for(ConvexHull hull : world.getHulls()){
			for(Line2D.Float qq : hull.getLines()){
				if(line.intersectsLine(qq)){
					if(world.getLineLineIntersection(lineL, qq).distance(x,y) < world.getLineLineIntersection(lineR, qq).distance(x, y)){
						moveAngle -= 5;
					}else{

						moveAngle += 5;
					}
					break;
				}
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