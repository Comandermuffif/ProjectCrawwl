package org.projectcrawwl.weapons;

import java.awt.Point;
import java.awt.geom.Line2D;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.World;
import org.projectcrawwl.objects.BasePlayer;
import org.projectcrawwl.objects.ConvexHull;

public class LaserRifle extends BaseRangedWeapon{
	
	private int range;
	
	private Line2D.Float line = new Line2D.Float();
	
	public LaserRifle(BasePlayer tempO){
		super(tempO);
		name = "Laser Rifle";
		velocity = 5;
		damage = 90;
		
		spread = 0;
		spreadAngle = 0;
		
		coolDown = 1000;
		currentCoolDown = coolDown;
		range = 1000;
	}
	
	public void render(){
		super.render();
		if(active && coolDown - currentCoolDown < coolDown*(.1)){
			GL11.glColor3d(1.0 * (currentCoolDown/coolDown), 0.0, 0.0);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(line.getX1() + GameData.getMapXOffset(), line.getY1() + GameData.getMapYOffset());
			GL11.glVertex2d(line.getX2() + GameData.getMapXOffset(), line.getY2() + GameData.getMapYOffset());
			GL11.glEnd();
		}
	}
	
	public void fire(){

		
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			
			Line2D.Float q = new Line2D.Float((float)(owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r + 5)), (float)(owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+5)), (float)(owner.x + Math.cos(Math.toRadians(owner.facingAngle))*(owner.r + range)), (float)(owner.y + Math.sin(Math.toRadians(owner.facingAngle))*(owner.r+range)));
			
			Line2D.Float tempL = (Line2D.Float) q.clone();
			
			line.setLine(q);
			
			double dist = q.getP1().distance(q.getP2());
			
			for(ConvexHull hull : World.getHulls()){
				
				if(q.ptSegDist(hull.getCenter()) > hull.getFarthest()*2){
					continue;
				}
				
				for(Line2D.Float tempLine : hull.getLines()){
					if(tempLine.intersectsLine(q)){
						Point temp = World.getLineLineIntersection(q, tempLine);
						double tempD = temp.distance(q.getP1());
						if(tempD < dist){
							dist = tempD;
							tempL.setLine(q.getP1(), temp);
						}
					}
				}
			}
			
			line.setLine(tempL);
			
			for(BasePlayer p : GameData.getAllPlayers()){
				boolean flag = false;
				for(Line2D.Float bound : p.boundingLines){
						
						Line2D.Float temp = new Line2D.Float();
						
						temp.x1 = (float) (bound.x1*Math.cos(Math.toRadians(p.facingAngle)) - bound.y1*Math.sin(Math.toRadians(p.facingAngle)) + p.x);
						temp.y1 = (float) (bound.x1*Math.sin(Math.toRadians(p.facingAngle)) + bound.y1*Math.cos(Math.toRadians(p.facingAngle)) + p.y);
						temp.x2 = (float) (bound.x2*Math.cos(Math.toRadians(p.facingAngle)) - bound.y2*Math.sin(Math.toRadians(p.facingAngle)) + p.x);
						temp.y2 = (float) (bound.x2*Math.sin(Math.toRadians(p.facingAngle)) + bound.y2*Math.cos(Math.toRadians(p.facingAngle)) + p.y);
								
								
						//Line2D.Float temp = new Line2D.Float(bound.x1 + tempx, bound.y1 + tempy, bound.x2 + tempx, bound.y2 + tempy);
						if(temp.intersectsLine(q)){
							flag = true;
							break;
						}
				}
				
				if(flag){
					p.damage(damage, owner);
				}
				
			}
			
		}
	}

}
