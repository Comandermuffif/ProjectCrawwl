package org.projectcrawwl.objects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.ConvexHull;


public class Zombie extends BasePlayer {
	Random random = new Random();
	BasePlayer target;
	
	private int move = 0;
	
	public Zombie(int tempX, int tempY){
		super(tempX,tempY, 0 , 100, 25);
		moveAngle = (float) (Math.random()*360);
		speed = .03;
		
		createBoundingBox();
		
	}
	
	//Draw everything here
	public void render(){
		if(!isReady){return;}
		super.render();
		float ratio = ((float) (settings.getScreenY())/settings.getScreenX());
		//Re scale view so text is right side up
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, settings.getScreenX() + data.zoom, settings.getScreenY() + data.zoom*(ratio),-data.zoom*(ratio), -1, 1);
		
		
		//data.getFont().drawString((renderX - 25), settings.getScreenY() - (renderY + 40), "Zombie", Color.red);
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		
		GL11.glLoadIdentity();
		GL11.glOrtho(-data.zoom, settings.getScreenX()  + data.zoom, -data.zoom*(ratio), settings.getScreenY() + data.zoom*(ratio), -1, 1);
		
		/*
		 * The lines used to get target without walking into walls and getting caught
		if(target != null){
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(renderX + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*0 - Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*farthest + (target.x - x), renderY + Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*0 + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*farthest + (target.y - y));
			GL11.glVertex2d(renderX + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*0 - Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*farthest, renderY + Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*0 + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*farthest);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(renderX + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*0 - Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*-farthest + (target.x - x), renderY + Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*0 + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*-farthest + (target.y - y));
			GL11.glVertex2d(renderX + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*0 - Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*-farthest, renderY + Math.sin(Math.atan2(target.getY() - y, target.getX() - x))*0 + Math.cos(Math.atan2(target.getY() - y, target.getX() - x))*-farthest);
			GL11.glEnd();
		}
		*/
	}

	
	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		//updateViewCone();
		if(target == null){
			move -= delta;
			if(move <= 0){
				move = 500;
				moveAngle += random.nextGaussian()*(5);
			}
		}
		tempFacing = moveAngle;
		
		if(speed == 0){
			
			target = null;
			
			moveAngle += 1;
			speed = .03;
		}
		
		if(target == null){
			double dist = -1;
			for(BasePlayer friendly : data.getFriendlies()){
				Line2D.Float sight = new Line2D.Float(x, y, friendly.x, friendly.y);
				//Time to get crazy
				
				Line2D.Double sightL = new Line2D.Double(
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest,
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest,
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest + (friendly.x - x),
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*farthest + (friendly.y - y)
						);
				Line2D.Double sightR = new Line2D.Double(
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest,
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest,
						x + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 - Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest + (friendly.x - x),
						y + Math.sin(Math.atan2(friendly.getY() - y, friendly.getX() - x))*0 + Math.cos(Math.atan2(friendly.getY() - y, friendly.getX() - x))*-farthest + (friendly.y - y)
						);
				
				
				boolean flag = true;
				
				for(ConvexHull hull : world.getHulls()){
					for(Line2D.Float line : hull.getLines()){
						if(line.intersectsLine(sightL)){
							flag = false;
							break;
						}
						if(line.intersectsLine(sightR)){
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
			moveAngle = (float) (Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x)));
			
			if(target.health <= 0){
				target = null;
				return;
			}
			
			if(new Point((int)getX(),(int)getY()).distance(target.getX(), target.getY()) <= getFarthest() + target.getFarthest() + 30){
				if(target.health <= 0){
					target = null;
				}
				inventory.getWeapon().fire();
			}
		}
	}
}