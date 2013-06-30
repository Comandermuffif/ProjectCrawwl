package org.projectcrawwl.objects;

import java.awt.Point;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;

public class BloodStain extends GameObject{
	
	protected int totalTimer = 250;
	protected int timer = totalTimer;
	
	protected double r;
	
	public BloodStain(float tempx, float tempy){
		super(tempx, tempy);
		r = 5 + Math.random() * 15;
	}
	public BloodStain(){
		this(0,0);
	}
	
	public void render(){
		if(!isReady){return;}
		
		GL11.glColor3d(1.0, 0.0, 0.0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(renderX, renderY);
		for(float temp = 0; temp <= Math.PI*2; temp += (Math.PI*2 / 32)){
			GL11.glVertex2d(renderX + Math.cos(temp)*r*(1 - .5*(double) timer/totalTimer), renderY + Math.sin(temp)*r*(1 - .5*(double) timer/totalTimer));
		}
		GL11.glVertex2d(renderX + r*(1 - .5*(double) timer/totalTimer), renderY);
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
			data += "\t<Type>BloodStain</Type>\n";
			data += "\t<Information>\n";
			{
				data += "\t\t<x>" + x + "</x>\n";
				data += "\t\t<y>" + y + "</y>\n";
				data += "\t\t<r>" + r + "</r>\n";
				
				data += "\t\t<totalTimer>" + totalTimer + "</totalTimer>\n";
				data += "\t\t<timer>" + timer + "</timer>\n";
				
				
				data += "\t\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				
				data += "\t\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				
				data += "\t\t<speed>" + speed + "</speed>\n";
				
				data += "\t\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
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
