package org.projectcrawwl.projectile;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;
import org.projectcrawwl.data.World;
import org.projectcrawwl.objects.GameObject;

public class Particle extends GameObject{
	
	/** The current time in milliseconds this will exist */
	private int lifetime;
	
	/** The time in milliseconds this particle exists */
	private int setLife;
	
	private Color color = new Color(0,0,0);
	
	/**
	 * Create a particle effect that dissipates over time
	 * @param X - x coordinate
	 * @param Y - y coordinate
	 * @param A - Angle in degrees that it is moving
	 * @param S - the speed it moves at
	 * @param L - How long it lives, in milliseconds
	 */
	public Particle(float X, float Y, float A, double S, int L){
		super(X,Y);
		x = X;
		y = Y;
		moveAngle = A;
		speed = S;
		lifetime = L;
		setLife = L;
	}
	
	/**
	 * Create a particle effect that dissipates over time
	 * @param X - x coordinate
	 * @param Y - y coordinate
	 * @param A - Angle in degrees that it is moving
	 * @param S - the speed it moves at
	 * @param L - How long it lives, in milliseconds
	 * @param c - the color of the particle
	 */
	public Particle(float X, float Y, float A, double S, int L, Color c){
		this(X,Y,A,S,L);
		color = c;
	}
	
	/**
	 * Everything to be drawn
	 */
	public void render(){
		super.render();
		if(!isReady){
			return;
		}
		
		double angle = 0;
		double length = 0;
		
		double height =  ((double)1000/975 - 1);
		
		Point2D.Double p = new Point2D.Double(GameSettings.getScreenX()/2 - World.getMapXOffset(), GameSettings.getScreenY()/2 - World.getMapYOffset());
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glColor4d(((double)color.getRed()/255),((double)color.getGreen()/255),((double)color.getBlue()/255),((double)(lifetime)/setLife));
		
		angle = Math.atan2((y) - p.y, (x + 2) - p.x);
		length = Math.pow(Math.pow((x + 2) - p.x, 2) + Math.pow((y) - p.y, 2), .5) * height;
		GL11.glVertex3d((x + 2) + World.getMapXOffset() + Math.cos(angle)*length, (y) + World.getMapYOffset() + Math.sin(angle)*length, .5);
		
		angle = Math.atan2((y) - p.y, (x - 2) - p.x);
		length = Math.pow(Math.pow((x - 2) - p.getX(), 2) + Math.pow((y) - p.getY(), 2), .5) * height;
		GL11.glVertex3d((x - 2) + World.getMapXOffset() + Math.cos(angle)*length, (y) + World.getMapYOffset() + Math.sin(angle)*length, .5);
		
		angle = Math.atan2((y + 2) - p.y, (x) - p.x);
		length = Math.pow(Math.pow((x) - p.getX(), 2) + Math.pow((y + 2) - p.getY(), 2), .5) * height;
		GL11.glVertex3d((x) + World.getMapXOffset() + Math.cos(angle)*length, (y + 2) + World.getMapYOffset() + Math.sin(angle)*length, .5);
		
		angle = Math.atan2((y - 2) - p.y, (x) - p.x);
		length = Math.pow(Math.pow((x) - p.getX(), 2) + Math.pow((y - 2) - p.getY(), 2), .5) * height;
		GL11.glVertex3d((x) + World.getMapXOffset() + Math.cos(angle)*length, (y - 2) + World.getMapYOffset() + Math.sin(angle)*length, .5);
		
		GL11.glEnd();
		
		
//		GL11.glColor4d(((double)color.getRed()/255),((double)color.getGreen()/255),((double)color.getBlue()/255),((double)(lifetime)/setLife));
//		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
//		GL11.glVertex2d(renderX-2, renderY);
//		GL11.glVertex2d(renderX, renderY-2);
//		GL11.glVertex2d(renderX+2, renderY);
//		GL11.glVertex2d(renderX, renderY+2);
//		GL11.glEnd();
	}
	
	/**
	 * Updates all of the information for this object, delta is time it took to complete last update cycle
	 */
	public void update(int delta){
		
		isReady = true;
		
		super.update(delta);
		
		lifetime -= delta;
		
		if(lifetime <= 0){
			GameData.removeParticle(this);
		}
		if(speed == 0){
			GameData.removeParticle(this);
		}
	}
	
	@Override
	public String toXML(){
		String data = "";
		
		data += "<Particle>\n";
		{
				data += "\t<x>" + x + "</x>\n";
				data += "\t<y>" + y + "</y>\n";
				
				data += "\t<facingAngle>" + facingAngle + "</facingAngle>\n";
				
				data += "\t<moveAngle>" + moveAngle + "</moveAngle>\n";
				
				data += "\t<speed>" + speed + "</speed>\n";
				
				data += "\t<turnSpeed>" + turnSpeed + "</turnSpeed>\n";
				
				data += "\t<color>" + color.getRGB() + "</color>\n";
				
				data += "\t<lifetime>" + lifetime + "</lifetime>\n";
				data += "\t<setLife>" + setLife + "</setLife>\n";

		}
		data += "</Particle>\n";
		
		return data;
	}
}
