package org.projectcrawwl.projectile;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.objects.GameObject;

public class Particle extends GameObject{
	
	/** The time in milliseconds this particle exists */
	private int lifetime;
	
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
	}
	
	/**
	 * Everything to be drawn
	 */
	public void render(){
		if(!isReady){
			return;
		}
		
		GL11.glColor3d(1, 0, 0);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(renderX, renderY);
		GL11.glVertex2d(0, 0);
		GL11.glEnd();
	}
	
	/**
	 * Updates all of the information for this object, delta is time it took to complete last update cycle
	 */
	public void update(int delta){
		
		super.update(delta);
		
		lifetime -= delta;
		
		if(lifetime <= 0){
			data.removeParticle(this);
		}
	}

}
