package org.projectcrawwl.weapons;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.objects.BasePlayer;

public class BaseMeleeWeapon extends BaseWeapon{
	
	public float range;
	public float angle;
	
	private Polygon area = new Polygon();
	private ArrayList<Line2D.Float> lines = new ArrayList<Line2D.Float>();
	
	/*
	 * TODO create a collision cone for weapon using polygon
	 * Similar to bounding box
	 */
	
	/**
	 * A generic Melee Weapon
	 * 
	 * @param tempR range
	 * @param tempA angle
	 * @param tempD damage
	 */
	protected BaseMeleeWeapon(BasePlayer tempO){
		super("BaseMeleeWeapon", 0);
		range = 25;
		angle = 90;
		damage = 10;
		owner = tempO;
		active = false;
		
		coolDown = 250;
		currentCoolDown = coolDown;
		
		//createArea();
		
	}
	
	protected void createArea(){
		
		area.reset();
		lines.clear();
		
		area.addPoint(0,0);
		for(float temp = -angle/2; temp < angle/2; temp += 2){
			area.addPoint((int) (range * Math.cos(Math.toRadians(temp))), (int) (range * Math.sin(Math.toRadians(temp))));
		}
		area.addPoint((int) (range * Math.cos(Math.toRadians(angle/2))), (int) (range * Math.sin(Math.toRadians(angle/2))));
		
		ArrayList<Line2D.Float> temp = new ArrayList<Line2D.Float>();
		
		float[] coord = new float[6];
		float[] lastCoord = new float[2];
		float[] firstCoord = new float[2];
		PathIterator pi = area.getPathIterator(null);
		
		pi.currentSegment(coord);
		
		pi.currentSegment(firstCoord); //Getting the first coordinate pair
        lastCoord[0] = firstCoord[0]; //Priming the previous coordinate pair
        lastCoord[1] = firstCoord[1];
		
		while(!pi.isDone()){
			final int type = pi.currentSegment(coord);
            switch(type) {
                case PathIterator.SEG_LINETO : {
                	temp.add(new Line2D.Float(coord[0], coord[1], lastCoord[0], lastCoord[1]));
                    lastCoord[0] = coord[0];
                    lastCoord[1] = coord[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    temp.add(new Line2D.Float(coord[0], coord[1], firstCoord[0], firstCoord[1]));   
                    break;
                }
            }
            pi.next();
		}
		
		lines = temp;
	}
	
	public void render(){
		super.render();
		if(active){
			GL11.glColor3f((float) (currentCoolDown/coolDown), (float) (currentCoolDown/coolDown), 0);
			
			GL11.glLineWidth(1);
			GL11.glBegin(GL11.GL_LINES);
			for(Line2D.Float temp : lines){
				GL11.glVertex2d(temp.x1*Math.cos(Math.toRadians(owner.facingAngle)) - temp.y1*Math.sin(Math.toRadians(owner.facingAngle)) + owner.renderX, temp.x1*Math.sin(Math.toRadians(owner.facingAngle)) + temp.y1*Math.cos(Math.toRadians(owner.facingAngle)) + owner.renderY);
				GL11.glVertex2d(temp.x2*Math.cos(Math.toRadians(owner.facingAngle)) - temp.y2*Math.sin(Math.toRadians(owner.facingAngle)) + owner.renderX, temp.x2*Math.sin(Math.toRadians(owner.facingAngle)) + temp.y2*Math.cos(Math.toRadians(owner.facingAngle)) + owner.renderY);
			}
			
			
			GL11.glEnd();
			
		}
	}
	
	public void update(int delta){
		super.update(delta);
		if(active){
			currentCoolDown -= delta;
		}
		if(currentCoolDown <= 0){
			active = false;
			currentCoolDown = coolDown;
		}
		//BOOP!
	}
	
	public void fire(){
		if(active == false){
			active = true;
			currentCoolDown = coolDown;
			
			ArrayList<BasePlayer> temp = GameData.getAllPlayers();
			
			for(BasePlayer b : temp){
				
				
				if( b.getCenter().distance(owner.getCenter()) - (b.getFarthest() + owner.getFarthest() + range) > 0){
					continue;
				}
				
				if(b == owner){
					continue;
				}
				
				Polygon shift = new Polygon();
				
				for(int i = 0; i < area.npoints; i ++){
					shift.addPoint((int) (area.xpoints[i]*Math.cos(Math.toRadians(owner.facingAngle)) - area.ypoints[i]*Math.sin(Math.toRadians(owner.facingAngle))), (int) (area.xpoints[i]*Math.sin(Math.toRadians(owner.facingAngle)) + area.ypoints[i]*Math.cos(Math.toRadians(owner.facingAngle))));
				}

				shift.translate((int) owner.getCenter().x, (int) owner.getCenter().y);
				
				if(shift.contains(b.x, b.y)){
					b.damage(damage, owner);
					continue;
				}
				
				
				
				for(Line2D.Float bound : lines){
					
					Line2D.Float temp1 = new Line2D.Float();
					
					temp1.x1 = (float) (bound.x1*Math.cos(Math.toRadians(owner.facingAngle)) - bound.y1*Math.sin(Math.toRadians(owner.facingAngle)) + owner.x);
					temp1.y1 = (float) (bound.x1*Math.sin(Math.toRadians(owner.facingAngle)) + bound.y1*Math.cos(Math.toRadians(owner.facingAngle)) + owner.y);
					temp1.x2 = (float) (bound.x2*Math.cos(Math.toRadians(owner.facingAngle)) - bound.y2*Math.sin(Math.toRadians(owner.facingAngle)) + owner.x);
					temp1.y2 = (float) (bound.x2*Math.sin(Math.toRadians(owner.facingAngle)) + bound.y2*Math.cos(Math.toRadians(owner.facingAngle)) + owner.y);
					
					
					for(Line2D.Float target : b.boundingLines){
						Line2D.Float temp2 = new Line2D.Float();
						
						temp2.x1 = (float) (target.x1*Math.cos(Math.toRadians(b.facingAngle)) - target.y1*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
						temp2.y1 = (float) (target.x1*Math.sin(Math.toRadians(b.facingAngle)) + target.y1*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
						temp2.x2 = (float) (target.x2*Math.cos(Math.toRadians(b.facingAngle)) - target.y2*Math.sin(Math.toRadians(b.facingAngle)) + b.x);
						temp2.y2 = (float) (target.x2*Math.sin(Math.toRadians(b.facingAngle)) + target.y2*Math.cos(Math.toRadians(b.facingAngle)) + b.y);
						
						if(temp2.intersectsLine(temp1)){
							b.damage(damage, owner);
							continue;
						}
					}
				}
			}
		}
	}
	@Override
	public String toXML() {
		String data = "";
		
		data += "\t\t<BaseMeleeWeapon>\n";
		{
			data += "\t\t\t<name>" + name + "</name>\n";
			data += "\t\t\t<damage>" + damage + "</damage>\n";
			data += "\t\t\t<active>" + active + "</active>\n";
			
			data += "\t\t\t<coolDown>" + coolDown + "</coolDown>\n";
			data += "\t\t\t<currentCoolDown>" + currentCoolDown + "</currentCoolDown>\n";
			
			data += "\t\t\t<automatic>" + automatic + "</automatic>\n";
		}
		data += "\t\t</BaseMeleeWeapon>\n";
		
		return data;
	}
	
}
