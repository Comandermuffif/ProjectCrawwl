package org.projectcrawwl.objects;

import org.projectcrawwl.data.GameData;
import org.projectcrawwl.weapons.SMG;
import org.projectcrawwl.weapons.Shotgun;

public class Friendly extends BasePlayer {
	
	BasePlayer target = null;
	
	public Friendly(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		r = 25;
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
		r = 25;
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
		
		for(BasePlayer a : GameData.getAI()){
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
}