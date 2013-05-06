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
			/*
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d((owner.r + range)*Math.cos(Math.toRadians(owner.facingAngle - angle/2 + angle*(currentCoolDown/coolDown))) + owner.renderX, (owner.r + range)*Math.sin(Math.toRadians(owner.facingAngle - angle/2 + angle*(currentCoolDown/coolDown))) + owner.renderY);
			GL11.glVertex2d(owner.renderX, owner.renderY);
			GL11.glEnd();*/
			
			GL11.glLineWidth(1);
			GL11.glBegin(GL11.GL_LINES);
			for(Line2D.Float temp : lines){
				GL11.glVertex2d(temp.x1*Math.cos(Math.toRadians(owner.facingAngle)) - temp.y1*Math.sin(Math.toRadians(owner.facingAngle)) + owner.renderX, temp.x1*Math.sin(Math.toRadians(owner.facingAngle)) + temp.y1*Math.cos(Math.toRadians(owner.facingAngle)) + owner.renderY);
				GL11.glVertex2d(temp.x2*Math.cos(Math.toRadians(owner.facingAngle)) - temp.y2*Math.sin(Math.toRadians(owner.facingAngle)) + owner.renderX, temp.x2*Math.sin(Math.toRadians(owner.facingAngle)) + temp.y2*Math.cos(Math.toRadians(owner.facingAngle)) + owner.renderY);
			}
			
			
			GL11.glEnd();
			
			/*
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		      {
		    	  GL11.glVertex2f(owner.getRenderX(), owner.getRenderY());
		    	  
		        for (double x= Math.toRadians(owner.facingAngle - angle/2); x<=Math.toRadians(owner.facingAngle + angle/2); x+=((Math.PI*2)/64) )
		        {
		        	GL11.glVertex2f( (owner.r + range)*(float)Math.cos(x) + owner.getRenderX(),
		        			(owner.r + range)*(float)Math.sin(x) + owner.getRenderY());  
		        }
		        GL11.glVertex2f( (owner.r + range)*(float)Math.cos(Math.toRadians(owner.facingAngle + angle/2)) + owner.getRenderX(),
	        			(owner.r + range)*(float)Math.sin(Math.toRadians(owner.facingAngle + angle/2)) + owner.getRenderY());  
		        //GL11.glVertex2f(owner.getRenderX()+range, owner.getRenderY());
		      }
		   GL11.glEnd();
		   */
			
		}
	}
	
	public void update(int delta){
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
			
			GameData data = GameData.getInstance();
			ArrayList<BasePlayer> temp = data.getAllPlayers();
			
			for(BasePlayer b : temp){
				if(b == owner){
					continue;
				}
				boolean flag = false;
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
							flag = true;
							break;
						}
					}
					if(flag){break;}
				}
			}
		}
	}
}
