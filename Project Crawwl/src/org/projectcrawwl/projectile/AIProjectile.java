package org.projectcrawwl.projectile;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.objects.BasePlayer;

public class AIProjectile extends BaseProjectile{
	
	private float r;
	
	public AIProjectile(float tempX, float tempY, float tempSpeed, float tempAngle, BasePlayer tempO){
		super(tempX, tempY,tempSpeed, tempAngle, 10, tempO);
		r = 5;
		damage = 10;
	}
	
	public void render(){
		super.render();
		
		GL11.glColor3d(255,0,255);
		
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
	}

	//Do all calculations here
	public void update(int delta){
		super.update(delta);
	}
}
