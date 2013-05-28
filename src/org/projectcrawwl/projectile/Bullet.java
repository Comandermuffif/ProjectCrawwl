package org.projectcrawwl.projectile;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.GameObject;

public class Bullet extends GameObject{
	
	BasePlayer owner;
	public float damage;
	GameData data = GameData.getInstance();
	Point lastPos = new Point();
	
	BasePlayer lastHit = null;
	
	/**
	 * 
	 * @param tempX - X location
	 * @param tempY - Y location
	 * @param tempSpeed - the speed it loves at
	 * @param tempAngle - the angle (degrees) that it is facing
	 * @param tempD - Damage it does
	 * @param own - The person who shot it
	 */
	public Bullet(float tempX, float tempY, float tempSpeed, float tempAngle, float tempD, BasePlayer own){
		super(tempX, tempY);
		x = tempX;
		y = tempY;
		speed = tempSpeed;
		moveAngle = tempAngle;
		r = 5;
		damage = tempD;
		owner = own;
		
		lastPos.setLocation(x, y);
		
		passThroughPlayers = true;
		
		farthest = 50;
		
	}
	
	public void render(){
		if(!isReady){return;}
		super.render();
		
		//(212, 175, 55)
		//Old color
		//GL11.glColor3d(.8313, .6867, .2156);
		
		/*
		GL11.glColor3d(0,0,0);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glColor4d(0,0,0,0);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle))*-20, renderY + Math.sin(Math.toRadians(moveAngle))*-20);
		GL11.glEnd();
		
		GL11.glColor3d(0,0,0);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glVertex2d(owner.renderX,owner.renderY);
		GL11.glEnd();*/
		
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glColor3d(0,0,0);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle - 90))*2, renderY + Math.sin(Math.toRadians(moveAngle - 90))*2);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle))*2, renderY + Math.sin(Math.toRadians(moveAngle))*2);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle + 90))*2, renderY + Math.sin(Math.toRadians(moveAngle + 90))*2);
		GL11.glColor4d(0,0,0,0);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle))*-50, renderY + Math.sin(Math.toRadians(moveAngle))*-50);
		
		GL11.glEnd();
		
		GL11.glLineWidth(1);
	}

	//Do all calculations here
	public void update(int delta){
		
		
		lastPos.setLocation(x, y);
		
		super.update(delta);
		
		Line2D.Float line = new Line2D.Float(x,y,lastPos.x,lastPos.y);
		
		ArrayList<BasePlayer> temp = data.getAllPlayers();
		//System.out.println(x + " : " + y+ " : " +lastPos.x+ " : " +lastPos.y );
		boolean flag = false;
		
		if(speed == 0){
			//System.out.println("Stopped");
			data.removeProjectile(this);
			
			
			for(int i = 0; i < 4; i ++){
				
				Random r = new Random();
				
				float a = (float) (r.nextGaussian()*60 + (moveAngle - 180));
				
				data.addParticle(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 500));
			}
			
			
			
			
			
			
			
			
//			Audio onFire = null;
//			try {
//				int i = (int) Math.random()*10;
//				onFire = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/fx_bullet_impact_concrete_1" + (i + 6) + ".wav"));
//			} catch (IOException e) {e.printStackTrace();}
//			onFire.playAsSoundEffect(1.0f, 1.0f, false);
		}
		
		for(BasePlayer b : temp){
			Polygon shift = new Polygon(b.boundingBox.xpoints,b.boundingBox.ypoints,b.boundingBox.npoints);
			shift.translate((int) b.getX(),(int) b.getY());
			if(shift.contains(x, y)){
				if(lastHit != b && !b.equals(owner)){
					
					//System.out.println("Inside");
					b.damage(damage, owner);
					damage -= 10;
					lastHit = b;
					
					for(int i = 0; i < 4; i ++){
						Random r = new Random();
						float a = (float) (r.nextGaussian()*60 + (moveAngle - 180));
						data.addParticle(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 250, new Color(255,0,0)));
					}
					
					if(this instanceof DumbMissle){
						data.removeProjectile(this);
					}
					break;
				}
			}
			
			for(Line2D.Float bound : b.boundingLines){
				Line2D.Float temp1 = new Line2D.Float();
				
				temp1.x1 = (float) (bound.x1*Math.cos(Math.toRadians(b.facingAngle)) - bound.y1*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
				temp1.y1 = (float) (bound.x1*Math.sin(Math.toRadians(b.facingAngle)) + bound.y1*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
				temp1.x2 = (float) (bound.x2*Math.cos(Math.toRadians(b.facingAngle)) - bound.y2*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
				temp1.y2 = (float) (bound.x2*Math.sin(Math.toRadians(b.facingAngle)) + bound.y2*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
				
				if(line.intersectsLine(temp1) && lastHit != b  && !b.equals(owner)){
					//System.out.println("Hit edge");
					b.damage(damage, owner);
					damage -= 10;
					lastHit = b;
					flag = true;
					
					for(int i = 0; i < 4; i ++){
						Random r = new Random();
						float a = (float) (r.nextGaussian()*60 + (moveAngle - 180));
						data.addParticle(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 250, new Color(255,0,0)));
					}
					
					if(this instanceof DumbMissle){
						data.removeProjectile(this);
					}
					break;
				}
			}
			if(flag){break;}
		}
		if(damage <= 0){
			data.removeProjectile(this);
		}
	}
}
