package org.projectcrawwl.projectile;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
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
	
	BasePlayer lastHit = null;
	
	
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
		
		//(212, 175, 55)
		
		GL11.glColor3d(.8313, .6867, .2156);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle))*-20, renderY + Math.sin(Math.toRadians(moveAngle))*-20);
		GL11.glEnd();
		
		GL11.glLineWidth(3);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glVertex2d(renderX + Math.cos(Math.toRadians(moveAngle))*-5, renderY + Math.sin(Math.toRadians(moveAngle))*-5);
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
			data.addPoint.add(new Point((int) x, (int) y));
			System.out.println("Stopped");
			data.removeProjectile(this);
		}
		
		for(BasePlayer b : temp){
			Polygon shift = new Polygon(b.boundingBox.xpoints,b.boundingBox.ypoints,b.boundingBox.npoints);
			shift.translate((int) b.getX(),(int) b.getY());
			if(shift.contains(x, y)){
				if(lastHit != b){
					System.out.println("Inside");
					data.addPoint.add(lastPos);
					b.damage(damage, owner);
					damage -= 10;
					lastHit = b;
					break;
				}
			}
			
			for(Line2D.Float bound : b.boundingLines){
				Line2D.Float temp1 = new Line2D.Float();
				
				temp1.x1 = (float) (bound.x1*Math.cos(Math.toRadians(b.facingAngle)) - bound.y1*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
				temp1.y1 = (float) (bound.x1*Math.sin(Math.toRadians(b.facingAngle)) + bound.y1*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
				temp1.x2 = (float) (bound.x2*Math.cos(Math.toRadians(b.facingAngle)) - bound.y2*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
				temp1.y2 = (float) (bound.x2*Math.sin(Math.toRadians(b.facingAngle)) + bound.y2*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
				
				if(line.intersectsLine(temp1)){
					System.out.println("Ht edge");
					data.addPoint.add(lastPos);
					b.damage(damage, owner);
					damage -= 10;
					lastHit = b;
					flag = true;
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
