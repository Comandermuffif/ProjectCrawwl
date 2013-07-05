package org.projectcrawwl.objects;

import java.awt.Point;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.weapons.BaseWeapon;
import org.projectcrawwl.weapons.SMG;
import org.projectcrawwl.weapons.Shotgun;

public class Friendly extends BasePlayer {
	
	BasePlayer target = null;
	
	public Friendly(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		facingAngle = 0;
		health = 100;
		inventory.addWeapon(new Shotgun(this));
		inventory.addWeapon(new SMG(this));
		
		this.createBoundingBox();
	}
	public Friendly(){
		super();
		x = 0;
		y = 0;
		facingAngle = 0;
		health = 100;
		inventory.addWeapon(new Shotgun(this));
		inventory.addWeapon(new SMG(this));
		
		this.createBoundingBox();
	}
	
	public void createBoundingBox(){
		
		//this.addPoint(0, 50);
		
		addPoint(50,0);
		addPoint(-25,25);
		addPoint(-25,-25);
		
		updateLines();
	}
	
	//Draw everything here
	public void render(){
		super.render();
		/*
		GL11.glColor3d(1*(health/100),(127*(health)/100)/255 ,0);
		
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
		GL11.glEnd();*/
		
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		double distToTarg = -1;
		
		for(BasePlayer a : GameData.getZombies()){
			double dist = Math.pow(Math.pow(a.getX() - getX(), 2) + Math.pow(a.getY() - getY(), 2), .5);
			
			if(dist > 400){
				continue;
			}
			
			if(distToTarg == -1){
				distToTarg = dist;
				target = a;
			}
			if(distToTarg > dist){
				distToTarg = dist;
				target = a;
			}
		}
		
		if(target != null){
			
			if(target.health <= 0){
				target = null;
				return;
			}
			
			inventory.setWeapon(1);
			inventory.getWeapon().fire();
			tempFacing = (float) (Math.toDegrees(Math.atan2(target.getY() - y, target.getX() - x)));
		}
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<Friendly>\n";
		{
			data += "\t<x>" + x + "</x>\n";
			data += "\t<y>" + y + "</y>\n";
			data += "\t<id>" + id + "</id>\n";
			
			data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
			
			data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
			
			data += "\t<speed>" + speed + "</speed>\n";
			
			data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
			
			data += "\t<health>" + health + "</health>\n";
			
			data += "\t<level>" + level + "</level>\n";
			
			data += "\t<kills>" + kills + "</kills>\n";
			
			data += "\t<xp>" + xp + "</xp>\n";
			
			data += "\t<boundingBox>\n";
			{
				for(Point p : this.getPoints()){
					data += "\t\t<point>\n";
					{
						data += "\t\t\t<pX>" + p.x + "</pX>\n";
						data += "\t\t\t<pY>" + p.y + "</pY>\n";
					}
					data += "\t\t</point>\n";
				}
			}
			data += "\t</boundingBox>\n";
			
			data += "\t<Inventory>\n";
			{
				for(BaseWeapon w : inventory.getWeapons()){
					data += w.toXML();
				}
			}
			data += "\t</Inventory>\n";
			
		}
		data += "</Friendly>\n";
		
		return data;
	}
	
}