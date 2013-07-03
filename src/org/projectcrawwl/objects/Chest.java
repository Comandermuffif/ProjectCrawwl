package org.projectcrawwl.objects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.GL11;
import org.projectcrawwl.data.GameData;
import org.projectcrawwl.data.GameSettings;

public class Chest extends GameObject {
	
	private static ArrayList<Chest> chests = new ArrayList<Chest>();
	
	/**
	 * Empty constructor
	 */
	public Chest(int x, int y){
		chests.add(this);
		this.x = x;
		this.y = y;
	}
	
	
	public void render(){
		
		float ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
		GL11.glLoadIdentity();
		GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		
		Collections.sort(chests);
		
		GL11.glColor3d(1.0, 0, 0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x-5, y-5);
		GL11.glVertex2d(x+5, y-5);
		GL11.glVertex2d(x+5, y+5);
		GL11.glVertex2d(x-5, y+5);
		GL11.glEnd();
		
		
		if(chests.get(0) == this){
			
			GameData.getFont().drawString(x, y + 10, "Empty");

			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			ratio = ((float) (GameSettings.getScreenY())/GameSettings.getScreenX());
			GL11.glLoadIdentity();
			GL11.glOrtho(-GameData.zoom, GameSettings.getScreenX()  + GameData.zoom, -GameData.zoom*(ratio), GameSettings.getScreenY() + GameData.zoom*(ratio), -1, 1);
		}
		
		
	}
}
