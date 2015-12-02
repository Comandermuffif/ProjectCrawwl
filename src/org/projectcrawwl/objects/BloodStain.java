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
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<BloodStain>\n";
		{
				data += "\t<x>" + x + "</x>\n";
				data += "\t<y>" + y + "</y>\n";
				data += "\t<r>" + r + "</r>\n";
				
				data += "\t<totalTimer>" + totalTimer + "</totalTimer>\n";
				data += "\t<timer>" + timer + "</timer>\n";
				
				
				data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				
				data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				
				data += "\t<speed>" + speed + "</speed>\n";
				
				data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
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
		}
		data += "</BloodStain>\n";
		
		return data;
	}
}
