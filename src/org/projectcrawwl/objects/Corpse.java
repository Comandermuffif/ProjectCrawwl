package org.projectcrawwl.objects;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;

public class Corpse extends BloodStain{
	
	private int dots = 5;
	
	private double[] angles = new double[dots];
	private double[] dist = new double[dots];
	private double[] radious = new double[dots];
	
	public Corpse(float tempx, float tempy){
		x = tempx;
		y = tempy;
		for(int i = 0; i < dots; i ++){
			angles[i] = Math.random() * 360;
			dist[i] = 30 + Math.random()*30;
			radious[i] = 10 + Math.random()*15;
			
			if(dist[i] + radious[i] > farthest){
				farthest = dist[i] + radious[i];
			}
		}
	}
	
	public void render(){
		if(!isReady){return;}
		
		GL11.glColor3d(1.0, 0.0, 0.0);
		
		for(int i = 0; i < dots; i ++){
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			GL11.glVertex2d(renderX + Math.cos(Math.toRadians(angles[i]))*dist[i], renderY + Math.sin(Math.toRadians(angles[i]))*dist[i]);
			for(float temp = 0; temp <= Math.PI*2; temp += (Math.PI*2 / 32)){
				GL11.glVertex2d(renderX + Math.cos(temp)*radious[i]*(1 - .25*(double) timer/totalTimer) + Math.cos(Math.toRadians(angles[i]))*dist[i], renderY + Math.sin(temp)*radious[i]*(1 - .25*(double) timer/totalTimer) + Math.sin(Math.toRadians(angles[i]))*dist[i]);
			}
			GL11.glVertex2d(renderX + radious[i]*(1 - .25*(double) timer/totalTimer) + Math.cos(Math.toRadians(angles[i]))*dist[i], renderY + Math.sin(Math.toRadians(angles[i]))*dist[i]);
			GL11.glEnd();
		}
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(renderX, renderY);
		for(float temp = 0; temp <= Math.PI*2; temp += (Math.PI*2 / 32)){
			GL11.glVertex2d(renderX + Math.cos(temp)*30*(1 - .5*(double) timer/totalTimer), renderY + Math.sin(temp)*30*(1 - .5*(double) timer/totalTimer));
		}
		GL11.glVertex2d(renderX + 30*(1 - .5*(double) timer/totalTimer), renderY);
		GL11.glEnd();
		
	}
	
	public void update(int delta){
		
		data = GameData.getInstance();
		
		if(timer > 0){
			timer -= delta;
		}
		
		isReady = true;
		
		renderX = x + data.getMapXOffset();
		renderY = y + data.getMapYOffset();
	}
}
