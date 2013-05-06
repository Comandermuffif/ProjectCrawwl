package org.projectcrawwl.objects;

import org.lwjgl.opengl.GL11;

public class Corpse extends GameObject{
	public Corpse(float tempx, float tempy){
		x = tempx;
		y = tempy;
	}
	
	public void render(){
		
		GL11.glColor3d(1.0, 0.0, 0.0);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX-50, renderY-50);
		GL11.glVertex2d(renderX+50, renderY+50);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX-50, renderY+50);
		GL11.glVertex2d(renderX+50, renderY-50);
		GL11.glEnd();
	}
	
	public void update(){
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
	}
}
