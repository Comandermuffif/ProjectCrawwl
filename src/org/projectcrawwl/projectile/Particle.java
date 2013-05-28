package org.projectcrawwl.projectile;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
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
		
		GL11.glColor4d(((double)color.getRed()/255),((double)color.getGreen()/255),((double)color.getBlue()/255),((double)(lifetime)/setLife));
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(renderX-2, renderY);
		GL11.glVertex2d(renderX, renderY-2);
		GL11.glVertex2d(renderX+2, renderY);
		GL11.glVertex2d(renderX, renderY+2);
		GL11.glEnd();
	}
	
	/**
	 * Updates all of the information for this object, delta is time it took to complete last update cycle
	 */
	public void update(int delta){
		
		isReady = true;
		
		super.update(delta);
		
		lifetime -= delta;
		
		if(lifetime <= 0){
			data.removeParticle(this);
		}
		if(speed == 0){
			data.removeParticle(this);
		}
	}

}
