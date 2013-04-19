package org.projectcrawwl.objects;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.weapons.SMG;
import org.projectcrawwl.weapons.Shotgun;

public class Friendly extends BasePlayer {
	
	BasePlayer target = this;
	
	public Friendly(int tempX, int tempY){
		super(tempX,tempY);
		x = tempX;
		y = tempY;
		r = 25;
		facingAngle = 0;
		health = 100;
		inventory.addWeapon(new Shotgun(this));
		//weapon = new SMG(this);
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
	}
	
	//Draw everything here
	public void render(){
		super.render();
		
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
		GL11.glVertex2d(renderX + Math.sin(Math.toRadians(facingAngle))*r, renderY + Math.cos(Math.toRadians(facingAngle))*r);
		GL11.glEnd();
		
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
		
		ArrayList<BasePlayer> ai = data.getAI();
		double distToTarg = -1;
		
		for(BasePlayer a : ai){
			double dist = Math.pow(Math.pow(a.getX() - getX(), 2) + Math.pow(a.getY() - getY(), 2), .5);
			if(distToTarg == -1){
				distToTarg = dist;
				target = a;
			}
			if(distToTarg > dist){
				distToTarg = dist;
				target = a;
			}
		}
		inventory.getWeapon().fire();
		facingAngle = (float) (Math.toDegrees(Math.atan2(x - target.getX(), y - target.getY())) + 180);
	}
}