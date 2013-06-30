package org.projectcrawwl.objects;

import java.awt.Point;

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
		
		if(timer > 0){
			timer -= delta;
		}
		
		isReady = true;
		
		renderX = x + GameData.getMapXOffset();
		renderY = y + GameData.getMapYOffset();
	}
	
	public String toXML(){
		String data = "";
		
		data += "<Object>\n";
		{
			data += "\t<Type>Corpse</Type>\n";
			data += "\t<Information>\n";
			{
				data += "\t\t<x>" + x + "</x>\n";
				data += "\t\t<y>" + y + "</y>\n";
				data += "\t\t<dots>" + dots + "</dots>\n";
				data += "\t\t<totalTimer>" + totalTimer + "</totalTimer>\n";
				data += "\t\t<timer>" + timer + "</timer>\n";
				data += "\t\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				data += "\t\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				data += "\t\t<speed>" + speed + "</speed>\n";
				data += "\t\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
				data += "\t\t<angles>\n";
				{
					for(int i = 0; i < dots; i ++){
						data += "\t\t\t<angle>" + angles[i] + "</angle>\n";
					}
				}
				data += "\t\t</angles>\n";

				data += "\t\t<dists>\n";
				{
					for(int i = 0; i < dots; i ++){
						data += "\t\t\t<dist>" + dist[i] + "</dist>\n";
					}
				}
				data += "\t\t</dists>\n";
				
				data += "\t\t<radii>\n";
				{
					for(int i = 0; i < dots; i ++){
						data += "\t\t\t<radious>" + radious[i] + "</radious>\n";
					}
				}
				data += "\t\t</radii>\n";
				
				data += "\t\t<boundingBox>\n";
				{
					for(Point p : this.getPoints()){
						data += "\t\t\t<point>\n";
						{
							data += "\t\t\t\t<pX>" + p.x + "</pX>\n";
							data += "\t\t\t\t<pY>" + p.y + "</pY>\n";
						}
						data += "\t\t\t</point>\n";
					}
				}
				data += "\t\t</boundingBox>\n";
				
			}
			data += "\t</Information>\n";
		}
		data += "</Object>\n";
		
		return data;
	}
	
}
