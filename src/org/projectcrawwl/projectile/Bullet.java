package org.projectcrawwl.projectile;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.World;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.BloodStain;
import org.projectcrawwl.objects.GameObject;

public class Bullet extends GameObject{
	
	BasePlayer owner;
	public double damage;
	Point lastPos = new Point();
	
	BasePlayer lastHit = null;
	
	public int ownerID = -1;
	
	/**
	 * 
	 * @param tempX - X location
	 * @param tempY - Y location
	 * @param tempSpeed - the speed it loves at
	 * @param tempAngle - the angle (degrees) that it is facing
	 * @param tempD - Damage it does
	 * @param own - The person who shot it
	 */
	public Bullet(float tempX, float tempY, float tempSpeed, float tempAngle, double tempD, BasePlayer own){
		super(tempX, tempY);
		x = tempX;
		y = tempY;
		speed = tempSpeed;
		moveAngle = tempAngle;
		damage = tempD;
		owner = own;
		
		lastPos.setLocation(x, y);
		
		passThroughPlayers = true;
		
		farthest = 50;
		
		ownerID = owner.id;
	}
	
	public Bullet() {
		super();
	}

	public void render(){
		if(!isReady){return;}
		super.render();
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glColor3d(0,0,0);
		
		GL11.glVertex3d((x + Math.cos(Math.toRadians(moveAngle - 90))*2) + World.getMapXOffset(), (y + Math.sin(Math.toRadians(moveAngle - 90))*2) + World.getMapYOffset(), .5);
		
		GL11.glVertex3d((x + Math.cos(Math.toRadians(moveAngle + 90))*2) + World.getMapXOffset(), (y + Math.sin(Math.toRadians(moveAngle + 90))*2) + World.getMapYOffset(), .5);
		
		GL11.glColor4d(0,0,0,0);
		GL11.glVertex3d((x + Math.cos(Math.toRadians(moveAngle))*-50) + World.getMapXOffset(), (y + Math.sin(Math.toRadians(moveAngle))*-50) + World.getMapYOffset(), .5);
		
		GL11.glEnd();
		
		GL11.glLineWidth(1);
	}

	//Do all calculations here
	public void update(int delta){
		
		if(owner == null){
			owner = (BasePlayer) GameData.getObject(ownerID);
		}
		
		lastPos.setLocation(x, y);
		
		super.update(delta);
		
		Line2D.Float line = new Line2D.Float(x,y,lastPos.x,lastPos.y);
		
		//System.out.println(x + " : " + y+ " : " +lastPos.x+ " : " +lastPos.y );
		boolean flag = false;
		
		if(speed == 0){
			//System.out.println("Stopped");
			GameData.removeObject(this);
			
			
			for(int i = 0; i < 4; i ++){
				
				Random r = new Random();
				
				float a = (float) (r.nextGaussian()*60 + (moveAngle - 180));
				
				GameData.addObject(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 500));
			}
			
		}
		
		for(BasePlayer b : GameData.getPlayers()){
			Polygon shift = new Polygon(b.boundingBox.xpoints,b.boundingBox.ypoints,b.boundingBox.npoints);
			shift.translate((int) b.getX(),(int) b.getY());
			if(shift.contains(x, y)){
				if(lastHit != b && !b.equals(owner)){
					
					//System.out.println("Inside");
					b.damage(damage, owner);
					damage -= 10;
					lastHit = b;
					
					GameData.addObject(new BloodStain(x,y));
					
					for(int i = 0; i < 4; i ++){
						Random r = new Random();
						float a = (float) (r.nextGaussian()*60 + (moveAngle - 180));
						GameData.addObject(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 250, new Color(255,0,0)));
					}
					
					if(this instanceof DumbMissle){
						GameData.removeObject(this);
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
						GameData.addObject(new Particle(x, y, a , .05 + r.nextGaussian()*.01, 250, new Color(255,0,0)));
					}
					
					if(this instanceof DumbMissle){
						GameData.removeObject(this);
					}
					break;
				}
			}
			if(flag){break;}
		}
		if(damage <= 0){
			GameData.removeObject(this);
		}
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<Bullet>\n";
		{
				data += "\t<x>" + x + "</x>\n";
				data += "\t<y>" + y + "</y>\n";
				data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				data += "\t<speed>" + speed + "</speed>\n";
				data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				data += "\t<damage>" + damage + "</damage>\n";
				data += "\t<ownerID>" + ownerID + "</ownerID>\n";
				
		}
		data += "</Bullet>\n";
		
		return data;
	}
}
