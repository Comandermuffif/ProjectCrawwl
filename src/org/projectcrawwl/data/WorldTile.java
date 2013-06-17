package org.projectcrawwl.data;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class WorldTile {
	
	private ArrayList<ConvexHull> hulls = new ArrayList<ConvexHull>();
	
	public int width = 500;
	public int height = 500;
	
	private int x;
	private int y;
	
	World w = World.getInstance();
	
	public WorldTile(){
		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void renderHulls(){
		
		GL11.glColor4d(1,1,1,1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x + w.getMapXOffset(), y + w.getMapYOffset());
		GL11.glVertex2d(x + width + w.getMapXOffset(), y + w.getMapYOffset());
		GL11.glVertex2d(x + width + w.getMapXOffset(), y + height + w.getMapYOffset());
		GL11.glVertex2d(x + w.getMapXOffset(), y + height + w.getMapYOffset());
		GL11.glEnd();
		
		for(ConvexHull h : hulls){
			h.renderHull();
		}
	}
	
	public void renderShadows(){
		for(ConvexHull h : hulls){
			h.renderShadow();
		}
	}

}
