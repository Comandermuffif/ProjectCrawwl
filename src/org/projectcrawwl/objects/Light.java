package org.projectcrawwl.objects;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class Light extends GameObject{
	
	private Color color = new Color(255, 127, 0);
	
	private int range = 300;
	
	public Light(int tempX, int tempY){
		x = tempX;
		y = tempY;
	}
	
	public void render(){
		GL11.glDepthMask(true);
		GL11.glClearDepth(1f);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		//Alpha
		{
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			GL11.glColor4f(0f, 0f, 0f, .5f);
			
			GL11.glVertex3f(x, y, 0);
			
			GL11.glColor4f(0f, 0f, 0f, 0f);
			
			for(float angle = 0; angle <= Math.PI*2; angle += ((Math.PI*2)/32)){
				GL11.glVertex3f(range * (float)Math.cos(angle) + x, range * (float)Math.sin(angle) + x, 0);
			}
			
			GL11.glVertex3f(x, y, 0);
			
			GL11.glEnd();
		}
		
		//Color
	}

}
